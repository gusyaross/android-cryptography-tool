package com.example.cryptoproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

@RequiresApi(api = Build.VERSION_CODES.O)
public class KeyInput extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_key);

        Button button_menu = findViewById(R.id.access_button);
        button_menu.setOnClickListener(access_input);
    }

    View.OnClickListener access_input = this::access_input;

    private void access_input(View v) {
        Intent intent = new Intent(KeyInput.this, MainActivity.class);
        TextView textView = findViewById(R.id.input_key);
        int choose = getIntent().getExtras().getInt("algo");
        intent.putExtra("key",textView.getText().toString());
        intent.putExtra("algo", choose);
        startActivity(intent);
    }
}
