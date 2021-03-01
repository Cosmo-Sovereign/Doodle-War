package com.example.doodle_war;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import static com.example.doodle_war.PaintView.paint_brush;
import static com.example.doodle_war.PaintView.path;

public class BrushView extends View {

    public static ArrayList<Path> pathlist=new ArrayList<>();
    public static ArrayList<Integer> colorlist=new ArrayList<>();
    public ViewGroup.LayoutParams params;
    public static int current_brush= Color.BLACK;
    public BrushView(Context context) {
        super(context);
    }

    public BrushView(Context context, @Nullable AttributeSet attrs) {

        super(context, attrs);
        init(context);
    }

    public BrushView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    private void init(Context context)
    {
        paint_brush.setAntiAlias(true);
        paint_brush.setColor(Color.BLACK);
        paint_brush.setStyle(Paint.Style.STROKE);
        paint_brush.setStrokeCap(Paint.Cap.ROUND);
        paint_brush.setStrokeJoin(Paint.Join.ROUND);

        params=new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x=event.getX();
        float y=event.getY();
        switch(event.getAction()){

            case MotionEvent.ACTION_DOWN:
                path.moveTo(x,y);
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                path.lineTo(x,y);
                pathlist.add(path);
                colorlist.add(current_brush);
                invalidate();
                return true;

            default:
                return false;
        }
    }
    @Override
    protected void onDraw(Canvas canvas) {
        for (int i=0; i<pathlist.size(); i++)
        {
            paint_brush.setColor(colorlist.get(i));
            canvas.drawPath(pathlist.get(i),paint_brush);
            invalidate();
        }
    }
}

