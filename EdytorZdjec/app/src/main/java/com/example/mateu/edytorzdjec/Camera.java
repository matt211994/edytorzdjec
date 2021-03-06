package com.example.mateu.edytorzdjec;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceView;
import android.view.View;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class Camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }
    JavaCameraView javaCameraView;
    Mat mRgba, capturemRgba;
    long addres;
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {


        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case BaseLoaderCallback.SUCCESS: {
                    javaCameraView.enableView();
                    break;
                }
                default: {
                    super.onManagerConnected(status);
                    break;
                }
            }

        }
    };
    private String TAG = "Camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        javaCameraView = (JavaCameraView) findViewById(R.id.java_camera_view);
        javaCameraView.setVisibility(SurfaceView.VISIBLE);
        javaCameraView.setCvCameraViewListener(this);

    }

    //podczas pausy aktywności wyłączana jest kamera , oszczędność zasobów telefonu
    @Override
    protected void onPause() {
        super.onPause();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    //podobna sytuacja co wyżej tylko przy kończeniu aktywności
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (javaCameraView != null)
            javaCameraView.disableView();
    }

    //podczas wznowienia, próba wczytania ponownie biblioteki opencv
    @Override
    protected void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            Log.i(TAG, "OpenCV loaded succesfully");
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        } else {
            Log.i(TAG, "OpenCV notloaded");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_4_0, this, mLoaderCallBack);
        }

    }

    // podczas uruchomienia kamery tworzy się obraz Mat
    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(width, height, CvType.CV_8UC4);
    }

    //podczas zatrzymania obrazu, zatracana jest zmienna openCV Mat
    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    //przekazywanie obrazu na wyświetlacz
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        return mRgba;
    }

    //rejestrowanie zdjęcia po naciśnięciu przycisku podgłaszania na telefonie
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();

        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_UP) {
                    javaCameraView.focusSearch(View.FOCUS_UP);
                    capturemRgba = mRgba.clone();
                    addres = capturemRgba.getNativeObjAddr();
                    Intent intent = new Intent(this, Conversions.class);
                    intent.putExtra("mat", addres);
                    startActivity(intent);
                }
                return true;
            case KeyEvent.ACTION_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    capturemRgba = mRgba;
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void onBackPressed()
    {
        this.startActivity(new Intent(Camera.this,SelectPicture.class));
    }

}
