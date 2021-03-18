package com.example.doodle_war.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doodle_war.R;
import com.example.doodle_war.base;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class SetupActivity extends AppCompatActivity {
    private EditText UserName,FullName,Bio;
    private Button SaveButton;
    private CircularImageView ProfileImage;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;
    String currentUserID;
    private ProgressDialog loadingBar;
    final static int gallery_pick = 1;
    private StorageReference UserProfileImageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child(" Profile Images");

        UserName = (EditText) findViewById(R.id.username);
        FullName = (EditText) findViewById(R.id.FullName);
        Bio = (EditText) findViewById(R.id.bio);
        SaveButton = (Button) findViewById(R.id.saveButton);
        ProfileImage = (CircularImageView) findViewById(R.id.ProfileImage);
        loadingBar = new ProgressDialog(this);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccountInfo();
            }
        });
        ProfileImage.setOnClickListener(new View.OnClickListener() {
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
        if(requestCode==gallery_pick && resultCode==RESULT_OK && data != null)
        {
            Uri ImageURI = data.getData();
            CropImage.activity(ImageURI)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
            if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
            {

                CropImage.ActivityResult result = CropImage.getActivityResult(data);

                if (requestCode == RESULT_OK)
                {
                    loadingBar.setTitle("Saving Profile Image");
                    loadingBar.setMessage("Please wait Your Profile Image is Being Saved");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);
                    Uri resultURI = result.getUri();
                    StorageReference filePath = UserProfileImageRef.child(currentUserID+".jpg");
                    filePath.putFile(resultURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful())
                            {
                                Intent setupIn = new  Intent(SetupActivity.this,SetupActivity.class);
                                startActivity(setupIn);
                                Toast.makeText(SetupActivity.this,"Profile Image is Stored Successfully",Toast.LENGTH_SHORT).show();
                                final String downloadURl = task.getResult().getStorage().getDownloadUrl().toString();
                                userRef.child("profileimage").setValue(downloadURl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful())
                                                {
                                                    Toast.makeText(SetupActivity.this,"Profile Image stored to database Successfully",Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                }
                                                else
                                                {
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(SetupActivity.this,"Error Occurred" + message,Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                }
                                            }
                                        });
                        }
                    }
                    });
                }

                else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
                {
                    Toast.makeText(SetupActivity.this,"Image could not be displayed try again",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        }
    }

    private void SaveAccountInfo() {

        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();
        String bio = Bio.getText().toString();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this,"Enter Your UserName",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this,"Enter Your Full Name",Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(bio))
        {
            Toast.makeText(this,"Enter Your Bio",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Your Information");
            loadingBar.setMessage("Please wait Your Account is Getting Ready");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            HashMap usermap = new HashMap();
            usermap.put("username",username);
            usermap.put("fullname",fullname);
            usermap.put("bio",bio);
            usermap.put("gender","Male");
            usermap.put("dob","none");
            userRef.updateChildren(usermap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        sendUserToBaseActivity();
                        Toast.makeText(SetupActivity.this,"Account Created Successfully",Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this,"Error Occurred" + message,Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });

        }
    }

    private void sendUserToBaseActivity() {
        Intent in=new Intent(SetupActivity.this,base.class);
        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(in);
        finish();
    }
}
