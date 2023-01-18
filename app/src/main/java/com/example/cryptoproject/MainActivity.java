package com.example.cryptoproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cryptoproject.cryptoMethods.AES;
import com.example.cryptoproject.cryptoMethods.Base64;
import com.example.cryptoproject.cryptoMethods.Caesar;
import com.example.cryptoproject.cryptoMethods.Vigenere;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {

    private int choose_id_menu1;
    private int fileid;
    private TextView inputText;
    private TextView outputText;

    private static final String fileName = "hello.txt";
    private static final String text = "Hello World";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fileid = 0;

        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.input_text);
        outputText = findViewById(R.id.output_text);

        Button button_menu = findViewById(R.id.button_choose_1);
        button_menu.setOnClickListener(menu1);

        Button button_encrypt = findViewById(R.id.button_encrypt);
        button_encrypt.setOnClickListener(encrypt);

        Button button_decrypt = findViewById(R.id.button_decrypt);
        button_decrypt.setOnClickListener(decrypt);

        Button button_read_file = findViewById(R.id.button_read_file);
        button_read_file.setOnClickListener(readfile);

        Button button_write_file = findViewById(R.id.button_write_file);
        button_write_file.setOnClickListener(writefile);

        Button button_photo = findViewById(R.id.button_hide);
        button_photo.setOnClickListener(hideinfo);
    }

    View.OnClickListener menu1 = this::showPopupMenu;
    View.OnClickListener encrypt = this::choose_encrypt;
    View.OnClickListener decrypt = this::choose_decrypt;
    View.OnClickListener readfile = this::read_file;
    View.OnClickListener writefile = this::write_file;
    View.OnClickListener hideinfo = this::hide_info;

    @SuppressLint("NonConstantResourceId")
    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.menu1);

        popupMenu
                .setOnMenuItemClickListener(item -> {
                    Intent intent = new Intent(MainActivity.this, KeyInput.class);

                    switch (item.getItemId()) {
                        case R.id.menu1_1:
                            choose_id_menu1 = 1;
                            intent.putExtra("algo", choose_id_menu1);
                            Toast.makeText(getApplicationContext(),
                                    "Вы выбрали Base64",
                                    Toast.LENGTH_SHORT).show();
                            return true;
                        case R.id.menu1_2:
                            choose_id_menu1 = 2;
                            intent.putExtra("algo", choose_id_menu1);
                            Toast.makeText(getApplicationContext(),
                                    "Вы выбрали шифр Виженера",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            return true;
                        case R.id.menu1_3:
                            choose_id_menu1 = 3;
                            intent.putExtra("algo", choose_id_menu1);
                            Toast.makeText(getApplicationContext(),
                                    "Вы выбрали AES шифрование",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            return true;
                        case R.id.menu1_4:
                            choose_id_menu1 = 4;
                            intent.putExtra("algo", choose_id_menu1);
                            Toast.makeText(getApplicationContext(),
                                    "Вы выбрали шифр Цезаря",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                            return true;
                        default:
                            return false;
                    }
                });

        popupMenu.show();
    }

    private void choose_encrypt(View v) {
        String key;
        if (choose_id_menu1 != 1) {
            choose_id_menu1 = getIntent().getExtras().getInt("algo");
        }
        switch (choose_id_menu1) {
            case 1:
                outputText.setText(Base64.encodeString(inputText.getText().toString()));
                break;
            case 2:
                key = getIntent().getExtras().getString("key");
                outputText.setText(Vigenere.encrypt(inputText.getText().toString(), key));
                break;
            case 3:
                key = getIntent().getExtras().getString("key");
                outputText.setText(AES.encrypt(inputText.getText().toString(), key));
                break;
            case 4:
                key = getIntent().getExtras().getString("key");
                outputText.setText(Caesar.encrypt(Integer.parseInt(key), inputText.getText().toString()));
                break;
            default:
                break;
        }
    }

    private void choose_decrypt(View v) {
        if (choose_id_menu1 != 1) {
            choose_id_menu1 = getIntent().getExtras().getInt("algo");
        }
        switch (choose_id_menu1) {
            case 1:
                outputText.setText(Base64.decodeString(inputText.getText().toString()));
                break;
            case 2:
                String key = getIntent().getExtras().getString("key");
                outputText.setText(Vigenere.decrypt(inputText.getText().toString(), key));
                break;
            case 3:
                key = getIntent().getExtras().getString("key");
                outputText.setText(AES.decrypt(inputText.getText().toString(), key));
                break;
            case 4:
                key = getIntent().getExtras().getString("key");
                outputText.setText(Caesar.decrypt(Integer.parseInt(key), inputText.getText().toString()));
            default:
                break;
        }
    }

    private void read_file(View v) {
        Intent mediaIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        mediaIntent.setType("text/plain");
        mediaIntent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(mediaIntent,0);
    }

    private void write_file(View v) {
        try {
            File myFile = new File(Environment.getExternalStorageDirectory().toString() + "/" + "file" + fileid + ".txt");
            System.out.println(Environment.getExternalStorageDirectory().toString() + "/" + "file" + fileid + ".txt");
            FileOutputStream outputStream = new FileOutputStream(myFile);
            outputStream.write(outputText.getText().toString().getBytes());
            outputStream.close();
            fileid++;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0
                && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String path = uri.getPath().replace(':', '/');
            String[] names  = path.split("/");
            read(names[names.length-1]);
        }
    }

    private void read(String filename) {
        File myFile = new File("/storage/emulated/0/" + filename);
        try {
            FileInputStream inputStream = new FileInputStream(myFile);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            try {
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                inputText.setText(stringBuilder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void hide_info(View v) {
        Intent intent = new Intent(MainActivity.this, PhotoActivity.class);
        startActivity(intent);
    }
}