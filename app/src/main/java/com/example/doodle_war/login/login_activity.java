package com.example.doodle_war.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.doodle_war.R;

public class login_activity extends AppCompatActivity {

    EditText email,password;
    Button login;
    TextView newuser,forgotpasword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        login=findViewById(R.id.login);
        newuser=findViewById(R.id.newuser);
        forgotpasword=findViewById(R.id.forgotpassword);

        //Go to user registration
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(login_activity.this,user_registration.class);
                startActivity(in);
            }
        });

    }
}