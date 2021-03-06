package com.example.mateu.edytorzdjec;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class SelectPicture extends AppCompatActivity {


    private static final int SELECTED_PICTURE = 1;
    static String TAG = "EdytorZdjęć";

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public Bitmap selected;
    long addres;
    Mat mat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCv loaded");
        }


    }


    public void btnClick(View view) {
        mat=null;
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECTED_PICTURE);
    }

    //CAŁA FUNKCJA WYBIERANIA ZDJĘCIA Z GALERII
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
                    addres=0;
                    addres = mat.getNativeObjAddr();
                    MatToAnotherClass();

                }
                break;
            default:
                break;
        }
    }

    //PRZESYŁANIE MATA DO AKTYWNOŚCI CONVERSIONS
    public void MatToAnotherClass() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SelectPicture.this, Conversions.class);
                intent.putExtra("mat", addres);
                startActivity(intent);
            }
        }).start();

    }

    public void StartCameraView(View view) {
        Intent intent = new Intent(this, Camera.class);
        startActivity(intent);
    }

}