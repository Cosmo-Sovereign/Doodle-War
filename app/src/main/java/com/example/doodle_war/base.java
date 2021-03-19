package com.example.doodle_war;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doodle_war.drawerlayout.feedback;
import com.example.doodle_war.drawerlayout.help;
import com.example.doodle_war.drawerlayout.profile;
import com.example.doodle_war.drawerlayout.setting;
import com.example.doodle_war.login.SetupActivity;
import com.example.doodle_war.paint.PaintView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import com.example.doodle_war.login.login_activity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

public class base extends AppCompatActivity {

    ImageView draw;
    private DrawerLayout drawer;
    private NavigationView nav_draw;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userRef;
    private CircularImageView navHeaderProfileImage;
    private TextView navHeaderUserName;
    String currentUserID;

    //toggle
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    //To check that user has login in application or not
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser current_user=firebaseAuth.getCurrentUser();

        if(current_user == null)
        {
            Intent in=new Intent(base.this,login_activity.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
        }
        else
        {
            CheckUserExistence();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        firebaseAuth= FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
            currentUserID = firebaseAuth.getCurrentUser().getUid();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because
        // each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        drawer=findViewById(R.id.drawer);
        nav_draw=findViewById(R.id.nav_draw);
        toolbar=findViewById(R.id.toolbar);
        View navHeader = nav_draw.inflateHeaderView(R.layout.draw_header);
        navHeaderProfileImage = navHeader.findViewById(R.id.navProfileImage);
        navHeaderUserName = navHeader.findViewById(R.id.navUserName);
        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                if(snapshot.exists())
                {
                    if(snapshot.hasChild("fullname"))
                    {
                        String username = snapshot.child("fullname").getValue().toString();
                        navHeaderUserName.setText(username);
                    }

                    if(snapshot.hasChild("profileimage"))
                    {
                        String image = snapshot.child("profileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.man).into(navHeaderProfileImage);
                    }
                    else
                    {
                        Toast.makeText(base.this,"PRofile doesn't exist",Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setSupportActionBar(toolbar);
        toggle=new ActionBarDrawerToggle(this,drawer,R.string.open,R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.mani);


        nav_draw.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.profile:
                        Intent in1=new Intent(base.this, profile.class);
                        startActivity(in1);
                        break;
                    case R.id.sitting:
                        Intent in2=new Intent(base.this, setting.class);
                        startActivity(in2);
                        break;
                    case R.id.help:
                        Intent in3=new Intent(base.this, help.class);
                        startActivity(in3);
                        break;
                    case R.id.feedback:
                        Intent in4=new Intent(base.this, feedback.class);
                        startActivity(in4);
                        break;
                    case R.id.logout:
                        Toast.makeText(base.this, "Logged Out", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        Intent in=new Intent(base.this,login_activity.class);
                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(in);
                        finish();
                        break;
                }
                return true;
            }
        });
        //draw
        draw=findViewById(R.id.draw);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(base.this, PaintView.class);
                startActivity(in);
            }
        });

    }
    private void CheckUserExistence() {
        final String current_user_id = firebaseAuth.getCurrentUser().getUid();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(current_user_id))
                {
                    sendUserToSetupActivity();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendUserToSetupActivity() {
        Intent SetupIn=new Intent(base.this, SetupActivity.class);
        SetupIn.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupIn);
        finish();
    }

}