package com.example.cryptoproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.RequiresApi;

import com.example.cryptoproject.cryptoPhoto.Embedding;
import com.example.cryptoproject.cryptoPhoto.Extracting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class PhotoActivity extends Activity {

    private int fileid;
    private int flag = 0;
    private TextView input_secret;
    private TextView output_secret;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_photo);

        input_secret = findViewById(R.id.input_secret);
        output_secret = findViewById(R.id.output_secret);

        Button button_hide = findViewById(R.id.button_hide_photo);
        button_hide.setOnClickListener(hide);

        Button button_extract = findViewById(R.id.button_extract_photo);
        button_extract.setOnClickListener(extract);
    }

    View.OnClickListener hide = this::hide_info;
    View.OnClickListener extract = this::extract_info;

    private void hide_info(View v) {
        flag = 0;
        Intent mediaIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        mediaIntent.setType("image/png");
        mediaIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(mediaIntent,0);
    }

    private void extract_info(View v) {
        flag = 1;
        Intent mediaIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        mediaIntent.setType("image/png");
        mediaIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(mediaIntent,1);
    }

    private void read_file(View v) {
        Intent mediaIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        mediaIntent.setType("image/png");
        mediaIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(mediaIntent,1);
    }

    private void write_file(View v) {
        try {
            File myFile = new File(Environment.getExternalStorageDirectory().toString() + "/" + "photo" + fileid + ".png");
            System.out.println(Environment.getExternalStorageDirectory().toString() + "/" + "photo" + fileid + ".png");
            FileOutputStream outputStream = new FileOutputStream(myFile);
            outputStream.close();
            fileid++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = uri.getPath().replace(':', '/');
            String[] names  = path.split("/");
            if (flag == 0) {
                try {
                    hide(names[names.length - 1], input_secret.getText().toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else {
                extract(names[names.length - 1]);
            }
        }
    }

    private void hide(String filename, String secret) throws FileNotFoundException {
        Bitmap picBitmap = BitmapFactory.decodeFile("/storage/emulated/0/" + filename);
        Bitmap res = Embedding.embedSecretText(secret,picBitmap);

        File file_res = new File("/storage/emulated/0/res_" + filename);
        try (FileOutputStream out = new FileOutputStream(file_res)) {
            res.compress(Bitmap.CompressFormat.PNG, 100, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void extract(String filename){
        Bitmap picBitmap = BitmapFactory.decodeFile("/storage/emulated/0/" + filename);
        Map<String, Object> objectMap = Extracting.extractSecretMessage(picBitmap);
        for (Map.Entry<String, Object> set : objectMap.entrySet()) {
            System.out.println(set.getKey());
            System.out.println(set.getValue());
        }
        output_secret.setText(Objects.requireNonNull(binaryToText(objectMap.get("message_bits").toString())));
    }

    public static String binaryToText(String binaryString) {
        StringBuilder stringBuilder = new StringBuilder();
        int charCode;
        for (int i = 0; i < binaryString.length(); i += 8) {
            charCode = Integer.parseInt(binaryString.substring(i, i + 8), 2);
            String returnChar = Character.toString((char) charCode);
            stringBuilder.append(returnChar);
        }
        return stringBuilder.toString();
    }
}