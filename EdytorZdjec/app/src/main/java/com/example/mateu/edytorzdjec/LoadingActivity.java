package com.example.mateu.edytorzdjec;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class LoadingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        //powitalny wątek, to całe liczenie czasu, lepiej tego nie opisuj :D ale ładnie wygląda
        Thread welcomeThread = new Thread() {

            @Override
            public void run() {
                try {
                    super.run();
                    sleep(1000);
                } catch (Exception e) {

                } finally {
                    Intent intent = new Intent(LoadingActivity.this,SelectPicture.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        welcomeThread.start();
    }
}
