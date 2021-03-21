package com.example.doodle_war.drawerlayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import com.example.doodle_war.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class profile extends AppCompatActivity {
    private TextView userName,userFullName,userBio,userDOB;
    private CircularImageView userProfileImage;
    private DatabaseReference profileUserRef;
    private FirebaseAuth firebaseAuth;
    private String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid();
        profileUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);

        userName = findViewById(R.id.my_username);
        userFullName = findViewById(R.id.my_profile_full_name);
        userBio = findViewById(R.id.my_profile_bio);
        userDOB = findViewById(R.id.my_dob);
        userProfileImage = findViewById(R.id.my_profile_pic);

        profileUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    String myProfileImage = snapshot.child("profileimage").getValue().toString();
                    String myUserName = snapshot.child("username").getValue().toString();
                    String myProfileName = snapshot.child("fullname").getValue().toString();
                    String myBio = snapshot.child("bio").getValue().toString();
                    String myDOB = snapshot.child("dob").getValue().toString();

                    Picasso.get().load(myProfileImage).placeholder(R.drawable.man).into(userProfileImage);
                    userName.setText("@"+myUserName);
                    userFullName.setText(myProfileName);
                    userBio.setText(myBio);
                    userDOB.setText("DOB: "+myDOB);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}