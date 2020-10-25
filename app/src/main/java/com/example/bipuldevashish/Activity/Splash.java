package com.example.bipuldevashish.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bipuldevashish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Splash extends AppCompatActivity {

    FirebaseUser firebaseUser;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // After completion of 2000 ms, the next activity will get started.
        int SPLASH_SCREEN_TIME_OUT = 2000;

        // Get User Present Or Not
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // if User not Fetched
                if (firebaseUser == null) {
                    Intent i = new Intent(Splash.this, Login.class);
                    startActivity(i);
                    finish();

                }
                // if Fetched
                else {

                    // Get User Id
                    userId = firebaseUser.getUid();

                    // Referencing the Database
                    DatabaseReference rootref = FirebaseDatabase.getInstance().getReference();

                    // Refered the database with UID as its root : UID - exists ? Yes -> Go To Home :: No -> Go To Register
                    DatabaseReference userCheck = rootref.child("Users").child(userId);

                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()) {
                                // Exists : Got to Home
                                startActivity(new Intent(Splash.this, Home.class));
                                finish();
                            } else {

                                // Doesn't Exists : Go To Register
                                startActivity(new Intent(Splash.this, Register.class));
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    };
                    userCheck.addListenerForSingleValueEvent(valueEventListener);
                }
//
            }

        }, SPLASH_SCREEN_TIME_OUT);


    }


}

