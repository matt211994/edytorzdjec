package com.example.mateu.edytorzdjec;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;

public class SavingPicture extends AppCompatActivity {

    SavePicture SavePicture = new SavePicture();
    EditText edit;
    Button buttonOK, buttonCancel;
    public String filename;
    Bitmap bmp=null;

    //TO JEST CAŁA AKTYWNOŚĆ W KTÓREJ PODAJEMY NAZWĘ PLIKU I PLIK SIĘ ZAPISUJE

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bmp=null;
        setContentView(R.layout.activity_saving_picture);
        edit = findViewById(R.id.editText);
        buttonOK = findViewById(R.id.button_ok);
        buttonCancel = findViewById(R.id.button_cancel);
        String Bitmapname = getIntent().getStringExtra("image");
        try {
            FileInputStream is = this.openFileInput(Bitmapname);
            bmp = BitmapFactory.decodeStream(is);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        //WYWOŁYWANIE FUNKCJI SAVEPICTURE PO NACIŚNIĘCIU PRZYCISKU OK
        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filename = edit.getText().toString();
                filename = filename+".jpg";
                SavePicture(bmp,filename);
                Toast.makeText(SavingPicture.this, "Zdjęcie zostało zapisane!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SavingPicture.this,SelectPicture.class);
                startActivity(intent);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SavingPicture.this,SelectPicture.class);
                startActivity(intent);
            }
        });
    }



    //OSOBNY WĄTEK DO ZAPISYWANIA PLIKU
    public void SavePicture(final Bitmap bitmapToSave, final String filenameToSave) {
        new Thread( new Runnable() {
            public void run(){
                SavePicture.storeImage(bitmapToSave, filenameToSave);
                return;
            }
        }).start();
    }



    @Override
    public void onBackPressed()
    {
        this.startActivity(new Intent(SavingPicture.this,SelectPicture.class));
    }
}
