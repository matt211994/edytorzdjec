package com.example.mateu.edytorzdjec;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavePicture {


    protected static boolean storeImage(Bitmap imageData, String filename) {
        String iconsStoragePath = Environment.getExternalStorageDirectory() + "/EdytorZdjec";
        File sdIconStorageDir = new File(iconsStoragePath);

        //create storage directories, if they don't exist
        sdIconStorageDir.mkdirs();
        try {
            String filePath = "/storage/emulated/0/EdytorZdjec/" + filename;
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            //choose another format if PNG doesn't suit you
            imageData.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
            return false;
        }

        return true;
    }
}


