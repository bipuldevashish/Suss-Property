package com.example.bipuldevashish.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.bipuldevashish.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Message extends AppCompatActivity {


    String value;
    Button whatsappContact;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        SharedPreferences sharedPreferences = getSharedPreferences("post", MODE_PRIVATE);
        value = sharedPreferences.getString("PostKey","");


        DatabaseReference getSellerNumbRef = FirebaseDatabase.getInstance().getReference().child("Postdetails").child(value);


    }
}