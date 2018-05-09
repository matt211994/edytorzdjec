package com.example.mateu.edytorzdjec;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Conversions extends AppCompatActivity {

    Bitmap temp;
    ImageView image;
    long addres;
    Mat mat,clear;
    int counter=0;
    String filename = "zdjecie.jpg";
    public String TAG = "Picture";

    SavePicture SavePicture = new SavePicture();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zdjecie);
        addres = getIntent().getLongExtra("mat", 0);
        Mat temp = new Mat(addres);
        mat = temp.clone();
        clear = mat.clone();
        image = (ImageView) findViewById(R.id.imageView2);
        showImg();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void showImg() {
        temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, temp);
        Drawable d = new BitmapDrawable(temp);
        image.setBackground(d);
    }

    public void toGray()
    {
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, temp);
        Drawable d = new BitmapDrawable(temp);
        image.setBackground(d);
    }

    public void toRotate()
    {
        Mat M,temp2;
        if(counter==1) {
            mat=clear.clone();
            temp2 = mat.clone();
            M = Imgproc.getRotationMatrix2D(new Point(mat.cols() / 2, mat.rows() / 2), 270, 1);
            Imgproc.warpAffine(mat, temp2, M, mat.size());
            mat = temp2.clone();
            temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, temp);
            Drawable d = new BitmapDrawable(temp);
            image.setBackground(d);
        }
        if (counter==2)
        {
            mat=clear.clone();
            Core.rotate(mat,mat,Core.ROTATE_180);
            temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, temp);
            Drawable d = new BitmapDrawable(temp);
            image.setBackground(d);
        }
        if (counter==3)
        {
            mat=clear.clone();
            temp2 = mat.clone();
            M = Imgproc.getRotationMatrix2D(new Point(mat.cols() / 2, mat.rows() / 2), 90, 1);
            Imgproc.warpAffine(mat, temp2, M, mat.size());
            temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
            mat = temp2.clone();
            Utils.matToBitmap(mat, temp);
            Drawable d = new BitmapDrawable(temp);
            image.setBackground(d);
        }
        if (counter==4)
        {
            mat=clear.clone();
            temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, temp);
            Drawable d = new BitmapDrawable(temp);
            image.setBackground(d);
            counter=0;
        }

    }

    public void toHSV()
    {
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2HSV);
        temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, temp);
        Drawable d = new BitmapDrawable(temp);
        image.setBackground(d);
    }

    public void toBinary()
    {
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
        Imgproc.adaptiveThreshold(mat, mat, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 15, 5);
        temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, temp);
        Drawable d = new BitmapDrawable(temp);
        image.setBackground(d);
    }

    public void toBlur()
    {
        org.opencv.core.Size s = new Size(50,50);
        Imgproc.blur(mat,mat,s);
        temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
        Utils.matToBitmap(mat, temp);
        Drawable d = new BitmapDrawable(temp);
        image.setBackground(d);
    }

    public void SavePicture()
    {
        SavePicture.storeImage(temp,filename);
        Toast.makeText(this,"Zapisano zdjęcie",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.gray) {
                mat = clear.clone();
                toGray();
            return true;
        }
        if (id == R.id.rotate) {
            counter++;
            toRotate();
            return true;
        }
        if (id == R.id.HSV) {
                mat=clear.clone();
                toHSV();
        }
        if (id == R.id.Binary) {
                mat = clear.clone();
                toBinary();
        }
        if (id == R.id.Save)
        {
            SavePicture();
        }
        if(id == R.id.Blur)
        {
            toBlur();
        }
            return true;
        }

}

