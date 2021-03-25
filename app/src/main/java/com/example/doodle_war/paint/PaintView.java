package com.example.doodle_war.paint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.doodle_war.MainActivity;
import com.example.doodle_war.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.example.doodle_war.paint.BrushView.colorlist;
import static com.example.doodle_war.paint.BrushView.current_brush;
import static com.example.doodle_war.paint.BrushView.pathlist;

public class PaintView extends AppCompatActivity {

    public static Path path=new Path();
    public static Paint paint_brush=new Paint();
    private Button saveDoodleButton;
    private int counter = 0;
    private BrushView mBrushView;
    private ImageView imageView;
    private final static String TAG = "Main";
    BrushView paintViewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveDoodleButton = findViewById(R.id.SaveDoodleButton);
        paintViewLayout = new BrushView(getApplicationContext(),null);
        imageView = findViewById(R.id.imageView);
        setContentView(R.layout.activity_paint_view);
        paintViewLayout.requestFocus();

    }



    public void pencil(View view) {
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
    public void saveDoodle(View view)
    {
        //saveImage(getBitmapFromView(view));
        int canvasWidth = 500;
        int canvasHeight = 500;
        View v = paintViewLayout;
        Bitmap bitmap = Bitmap.createBitmap(canvasWidth, canvasHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);


//set it to your ImageView
        //imageView.setImageBitmap(bitmap);

//or save to sdcard
        String root = Objects.requireNonNull(getExternalFilesDir(getApplicationContext().toString())).toString();
        File dir = new File(root);
        Toast.makeText(this,root,Toast.LENGTH_SHORT).show();
        boolean isDirectoryCreated = dir.exists() || dir.mkdirs();

        File outputFile = new File(dir, "doodle.png");
        OutputStream fout = null;
        try {
            fout = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fout);
            Toast.makeText(this,"Image Saved",Toast.LENGTH_SHORT).show();
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}