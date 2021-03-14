package com.example.doodle_war;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.doodle_war.drawerlayout.feedback;
import com.example.doodle_war.drawerlayout.help;
import com.example.doodle_war.drawerlayout.profile;
import com.example.doodle_war.drawerlayout.setting;
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

public class base extends AppCompatActivity {

    ImageView draw;
    private DrawerLayout drawer;
    private NavigationView nav_draw;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;

    //toggle
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        firebaseAuth= FirebaseAuth.getInstance();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because
        // each
        // menu should be considered as top level destinations.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        drawer=findViewById(R.id.drawer);
        nav_draw=findViewById(R.id.nav_draw);
        toolbar=findViewById(R.id.toolbar);
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
                        Toast.makeText(base.this, "Logout", Toast.LENGTH_SHORT).show();
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

}