package com.example.doodle_war.drawerlayout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.doodle_war.R;

public class feedback extends AppCompatActivity {
    private EditText et_sub, et_msg;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        et_sub=findViewById(R.id.et_sub);
        et_msg=findViewById(R.id.et_msg);
        send=findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String recipients[]={"sankhyadharparas@gmail.com","pradyumnp29@srmist.edu.in"};
                String sub=et_sub.getText().toString();
                String msg=et_msg.getText().toString();

                Intent in=new Intent(Intent.ACTION_SEND);
                in.putExtra(Intent.EXTRA_EMAIL, recipients);
                in.putExtra(Intent.EXTRA_SUBJECT, sub);
                in.putExtra(Intent.EXTRA_TEXT, msg);
                in.setType("message/rfc822");
                startActivity(Intent.createChooser(in,"choose an email client"));

            }
        });
    }
}