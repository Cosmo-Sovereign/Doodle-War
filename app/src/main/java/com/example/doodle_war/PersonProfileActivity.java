package com.example.doodle_war;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class PersonProfileActivity extends AppCompatActivity {
    private TextView userName,userFullName,userBio,userDOB;
    private CircularImageView userProfileImage;
    private Button sendFriendRequestButton,declineFriendRequestButton;
    private DatabaseReference profileUserRef,usersRef;
    private FirebaseAuth firebaseAuth;
    private String senderUserID,receiverUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        usersRef =FirebaseDatabase.getInstance().getReference().child("Users");


        InitializeFields();

        usersRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
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

    private void InitializeFields() {

        userName = findViewById(R.id.person_username);
        userFullName = findViewById(R.id.person_full_name);
        userBio = findViewById(R.id.person_profile_bio);
        userDOB = findViewById(R.id.person_dob);
        userProfileImage = findViewById(R.id.person_profile_pic);
        sendFriendRequestButton = findViewById(R.id.person_send_friend_request_button);
        declineFriendRequestButton = findViewById(R.id.person_decline_friend_request_button);


    }
}