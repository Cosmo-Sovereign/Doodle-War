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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class base extends AppCompatActivity {

    ImageView draw;
    private DrawerLayout drawer;
    private NavigationView nav_draw;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
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