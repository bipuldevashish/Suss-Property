package com.example.bipuldevashish.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bipuldevashish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    //variable declaration
    TextView usernameTextView,
            userEmailTextView,
            userPhoneNumberTextView;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    final String TAG = "Profile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Progress Dialog Init
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading ");
        progressDialog.setMessage("Retrieving profile data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Linking the variables to the UI
        usernameTextView = findViewById(R.id.userName);
        userEmailTextView = findViewById(R.id.userEmail);
        userPhoneNumberTextView = findViewById(R.id.userPhoneNumber);

        //Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // [START_EXCLUDE]
                        if (dataSnapshot.exists()) {

                            // fetching data from firebase
                            String fullName = dataSnapshot.child("Name").getValue(String.class);
                            String Email = dataSnapshot.child("Email").getValue(String.class);
                            String Mobile = dataSnapshot.child("Mobile").getValue(String.class);

                            setProfile(fullName, Email, Mobile);

                        } else {

                            Log.d(TAG, "User " + userId + " is unexpectedly null");
                            Toast.makeText(Profile.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.w(TAG, "getUser:onCancelled", error.toException());
                    }
                });
    }

    public void setProfile(String name, String email, String mobile) {
        usernameTextView.setText(name);
        userEmailTextView.setText(email);
        userPhoneNumberTextView.setText(mobile);

        progressDialog.dismiss();
    }
}