package com.example.doodle_war.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doodle_war.R;
import com.example.doodle_war.base;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class login_activity extends AppCompatActivity {

    EditText UserEmail,UserPassword;
    Button login;
    TextView newuser,forgotpasword;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);
        UserEmail=findViewById(R.id.email);
        UserPassword=findViewById(R.id.password);
        login=findViewById(R.id.login);
        newuser=findViewById(R.id.newuser);
        forgotpasword=findViewById(R.id.forgotpassword);
        firebaseAuth = FirebaseAuth.getInstance();
        loadingBar =  new ProgressDialog(this);

        //Go to user registration
        newuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(login_activity.this,user_registration.class);
                startActivity(in);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String email = UserEmail.getText().toString();
                String password = UserPassword.getText().toString();
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(login_activity.this,"Please enter Email",Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(password))
                {
                    Toast.makeText(login_activity.this,"Please enter Password",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    loadingBar.setTitle("Login");
                    loadingBar.setMessage("Logging You In Your Doodle Account");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    firebaseAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful())
                                    {   Intent in = new Intent(login_activity.this, base.class);
                                        in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(in);
                                        finish();
                                        Toast.makeText(login_activity.this,"You have Logged In successfully",Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else
                                    {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(login_activity.this,"Error Occured"+message,Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
            }
        });

    }
    private void userLogin()
    {
    }
}