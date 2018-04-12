package com.example.mateu.edytorzdjec;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class Zdjecie extends AppCompatActivity {

    Bitmap temp;
    ImageView image;
    long addres;
    Mat mat;
    boolean ifGray;
    int kolory[];
    int Red[];
    int tab[][];
    int Green[];
    int Blue[];

    public String TAG = "Zdjęcie";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zdjecie);
        addres = getIntent().getLongExtra("mat", 0);
        Mat temp = new Mat(addres);
        mat = temp.clone();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.grey) {

            if (ifGray == true) {
                Toast.makeText(getApplicationContext(), "NIEDOZWOLONA KONWERSJA!", Toast.LENGTH_LONG).show();
            } else {
                ifGray = true;
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
                temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(mat, temp);

                Drawable d = new BitmapDrawable(temp);
                image.setBackground(d);
            }
            return true;
        }

        if (id == R.id.obroc) {

            Core.rotate(mat, mat, Core.ROTATE_90_CLOCKWISE);
            temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, temp);
            Drawable d = new BitmapDrawable(temp);
            image.setBackground(d);

            return true;
        }
        if (id == R.id.HSV) {
            if (ifGray == true) {
                Toast.makeText(getApplicationContext(), "NIEDOZWOLONA KONWERSJA!", Toast.LENGTH_LONG).show();
            } else {
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGB2HSV);
                temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(mat, temp);
                Drawable d = new BitmapDrawable(temp);
                image.setBackground(d);
            }
        }

        if (id == R.id.Binary) {

            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2GRAY);
            Imgproc.adaptiveThreshold(mat, mat, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.THRESH_BINARY, 15, 5);
            temp = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(mat, temp);
            Drawable d = new BitmapDrawable(temp);
            image.setBackground(d);


        }
            return true;
        }


    }

