package com.example.bipuldevashish;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FirebaseAuth mAuth;
    Button btn_register;
    String mEmail,mPassword;
    EditText email,password,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        btn_register = findViewById(R.id.button_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUserWithEmailAndPassword(mEmail,mPassword);
            }
        });
    }

    //for checking if the user is already signed in
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void createUserWithEmailAndPassword (String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()){
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;

        email = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        mEmail = email.getText().toString();
        mPassword = password.getText().toString();

        if (TextUtils.isEmpty(mEmail)){
            email.setError("Required field");
            valid = false;
        }else {
            email.setError(null);
        }

        if (TextUtils.isEmpty(mPassword)){
            password.setError("Required field");
            valid = false;
        }else {
            password.setError(null);
        }

        return valid;
    }

    private void updateUI(FirebaseUser currentUser) {
    }
}