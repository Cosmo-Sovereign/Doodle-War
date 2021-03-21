package com.example.doodle_war.drawerlayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.canhub.cropper.CropImage;
import com.canhub.cropper.CropImageView;
import com.example.doodle_war.R;
import com.example.doodle_war.login.SetupActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class setting extends AppCompatActivity {
    private Toolbar toolbar;
    private EditText userName,userFullName,userBio,userGender,userDOB;
    private Button updateAccountSettingButton;
    private CircularImageView userProfileImage;
    private DatabaseReference settingUserRef;
    private FirebaseAuth firebaseAuth;
    private String currentUserID;
    final static int gallery_pick = 1;
    Uri ImageURI;
    private ProgressDialog loadingBar;
    private StorageReference UserProfileImageRef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        settingUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");

        toolbar = findViewById(R.id.settingToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Account Setting");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userName = findViewById(R.id.settingUserName);
        userFullName = findViewById(R.id.settingUserFullName);
        userBio = findViewById(R.id.settingBio);
        userGender = findViewById(R.id.settingUserGender);
        userDOB = findViewById(R.id.settingUserDOB);
        updateAccountSettingButton = findViewById(R.id.settingSaveButton);
        userProfileImage = findViewById(R.id.settingProfileImage);
        loadingBar = new ProgressDialog(this);


        settingUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String myProfileImage = snapshot.child("profileimage").getValue().toString();
                    String myUserName = snapshot.child("username").getValue().toString();
                    String myProfileName = snapshot.child("fullname").getValue().toString();
                    String myBio = snapshot.child("bio").getValue().toString();
                    String myDOB = snapshot.child("dob").getValue().toString();
                    String myGender = snapshot.child("gender").getValue().toString();

                    Picasso.get().load(myProfileImage).placeholder(R.drawable.man).into(userProfileImage);
                    userName.setText(myUserName);
                    userFullName.setText(myProfileName);
                    userBio.setText(myBio);
                    userDOB.setText(myDOB);
                    userGender.setText(myGender);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        updateAccountSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateAccountInfo();
            }
        });

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent,gallery_pick);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //conditions for picture
        if(requestCode == gallery_pick && resultCode == RESULT_OK && data != null) {
            ImageURI = data.getData();
            //crop the image
            CropImage.activity(ImageURI)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        //get cropped image
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            //store the cropped image into result
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode== RESULT_OK)
            {
                loadingBar.setTitle("Saving Profile Image");
                loadingBar.setMessage("Please wait Your Profile Image is Being Saved");
                loadingBar.setCanceledOnTouchOutside(true);
                loadingBar.show();

                Uri resultURI = result.getUri();
                final StorageReference filePath = UserProfileImageRef.child(currentUserID+".jpg");
                filePath.putFile(resultURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadURL = uri.toString();
                                settingUserRef.child("profileimage").setValue(downloadURL).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful())
                                        {
                                            //Intent setupIn = new  Intent(SetupActivity.this,SetupActivity.class);
                                            //startActivity(setupIn);
                                            Toast.makeText(setting.this,"Profile Image stored to database Successfully",Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                        else
                                        {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(setting.this,"Error Occurred" + message,Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                            }
                        });
                    }
                });
            }

            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Toast.makeText(setting.this,"Image could not be displayed try again",Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }
    }

    private void ValidateAccountInfo()
    {
        String username = userName.getText().toString();
        String profilename = userFullName.getText().toString();
        String bio = userBio.getText().toString();
        String dob = userDOB.getText().toString();
        String gender = userGender.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this,"Please Enter Your UserName",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(profilename))
        {
            Toast.makeText(this,"Please Enter Your Full Name",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(bio))
        {
            Toast.makeText(this,"Please Enter Your Bio",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(dob))
        {
            Toast.makeText(this,"Please Enter Your Date of Birth",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(gender))
        {
            Toast.makeText(this,"Please Enter Your Gender",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Profile Image");
            loadingBar.setMessage("Please wait Your Profile Image is Being Saved");
            loadingBar.setCanceledOnTouchOutside(true);
            loadingBar.show();
            UpdateAccountInfo(username,profilename,bio,dob,gender);
        }

    }

    private void UpdateAccountInfo(String username, String profilename, String bio, String dob, String gender)
    {
        HashMap userMap = new HashMap();
        userMap.put("username",username);
        userMap.put("fullname",profilename);
        userMap.put("bio",bio);
        userMap.put("dob",dob);
        userMap.put("gender",gender);
        settingUserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(setting.this,"Account Information Updated Successfully",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
                else
                {
                    Toast.makeText(setting.this,"Error Occurred During Updating Account Setting",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }
}