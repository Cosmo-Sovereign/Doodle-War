package com.example.doodle_war;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.View;

import static com.example.doodle_war.paint.BrushView.colorlist;
import static com.example.doodle_war.paint.BrushView.current_brush;
import static com.example.doodle_war.paint.BrushView.pathlist;

public class PaintView extends AppCompatActivity {

    public static Path path=new Path();
    public static Paint paint_brush=new Paint();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_view);
    }

    public void pancil(View view) {
        paint_brush.setColor(Color.BLACK);
        currentColor(paint_brush.getColor());
    }

    public void eraser(View view) {
        pathlist.clear();
        colorlist.clear();
        path.reset();
    }

    public void red(View view) {
        paint_brush.setColor(Color.RED);
        currentColor(paint_brush.getColor());
    }

    public void blue(View view) {
        paint_brush.setColor(Color.BLUE);
        currentColor(paint_brush.getColor());
    }

    public void yellow(View view) {
        paint_brush.setColor(Color.YELLOW);
        currentColor(paint_brush.getColor());
    }

    public void green(View view) {
        paint_brush.setColor(Color.GREEN);
        currentColor(paint_brush.getColor());
    }

    public void magenta(View view) {
        paint_brush.setColor(Color.MAGENTA);
        currentColor(paint_brush.getColor());
    }
    public  void currentColor(int c){
        current_brush=c;
        path=new Path();
    }
}