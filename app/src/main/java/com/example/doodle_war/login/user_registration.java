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
import android.widget.Toast;

import com.example.doodle_war.R;
import com.example.doodle_war.base;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class user_registration extends AppCompatActivity {

    EditText UserEmail, UserPassword, UserConfirmpassword;
    Button CreateUser;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);
        firebaseAuth = FirebaseAuth.getInstance();
        UserEmail = findViewById(R.id.email);
        UserPassword = findViewById(R.id.password);
        UserConfirmpassword = findViewById(R.id.confirmpassword);
        CreateUser = findViewById(R.id.createuser);
        loadingBar =  new ProgressDialog(this);

        CreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateNewAccount();
            }


        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser current_user=firebaseAuth.getCurrentUser();
        if(current_user != null)
        {
            Intent in=new Intent(user_registration.this,base.class);
            in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(in);
        }
    }

    private void CreateNewAccount() {
        String email = UserEmail.getText().toString();
        String password = UserPassword.getText().toString();
        String confirmPassword = UserConfirmpassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Write Your Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Write Your Email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Please confirm your Password", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Both password doesn't match", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please wait account is creating");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);
            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                            {
                                sendUserToSetupActivity();
                                Toast.makeText(user_registration.this,"Authentication Successful",Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                            else
                            {
                                String message = task.getException().getMessage();
                                Toast.makeText(user_registration.this,"Error Occured" + message,Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });

        }
    }

    private void sendUserToSetupActivity() {
        Intent SetupIntent = new Intent(user_registration.this, SetupActivity.class);
        SetupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(SetupIntent);
        finish();
    }
}