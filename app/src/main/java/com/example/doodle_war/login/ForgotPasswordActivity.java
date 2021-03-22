package com.example.doodle_war.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.doodle_war.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private Button sendResetPasswordEmailButton;
    private EditText resetEmailInput;
    private Toolbar toolbar;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();


        toolbar = findViewById(R.id.forgotPasswordToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Reset Password");

        sendResetPasswordEmailButton = findViewById(R.id.resetPasswordButton);
        resetEmailInput = findViewById(R.id.resetPasswordEmail);

        sendResetPasswordEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = resetEmailInput.getText().toString();
                if(TextUtils.isEmpty(userEmail))
                {
                    Toast.makeText(ForgotPasswordActivity.this,"Please enter your valid email address",Toast.LENGTH_SHORT).show();

                }
                else
                {
                    firebaseAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(ForgotPasswordActivity.this,"Reset Password link sent to "+userEmail+". Follow the Link to Reset Password",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPasswordActivity.this,login_activity.class));
                            }
                            else
                            {
                                String message = task.getException().toString();
                                Toast.makeText(ForgotPasswordActivity.this,"Error Occurred: " + message,Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

    }
}