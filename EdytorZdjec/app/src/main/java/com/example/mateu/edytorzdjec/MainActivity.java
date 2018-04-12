package com.example.mateu.edytorzdjec;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity {


    public Bitmap selected;
    long addres;
    Mat mat;
    private static final int SELECTED_PICTURE = 1;
    static String TAG = "EdytorZdjęć";


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCv loaded");
        }


    }


    public void btnClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SELECTED_PICTURE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String[] projection = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    selected = BitmapFactory.decodeFile(filePath);
                    selected = selected.copy(Bitmap.Config.ARGB_8888, true);
                    mat = new Mat(selected.getHeight(), selected.getWidth(), CvType.CV_8UC1);
                    Utils.bitmapToMat(selected, mat);
                    addres=mat.getNativeObjAddr();
                    MatToZdjecie();

                }
                break;
            default:
                break;


        }


    }


    public void MatToZdjecie()
    {
        Intent intent=new Intent(this,Zdjecie.class);
        intent.putExtra("mat",addres);
        startActivity(intent);

    }

}