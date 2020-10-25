package com.example.bipuldevashish.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.bipuldevashish.R;

public class Message extends AppCompatActivity {


    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        SharedPreferences sharedPreferences = getSharedPreferences("post", MODE_PRIVATE);
        value = sharedPreferences.getString("PostKey","");

        Toast.makeText(this, value, Toast.LENGTH_SHORT).show();
    }
}