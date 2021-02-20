package com.example.doodle_war;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class PaintView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BrushView view=new BrushView(this);
        setContentView(view);
        addContentView(view.btnEraseAll, view.params);
    }
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}

