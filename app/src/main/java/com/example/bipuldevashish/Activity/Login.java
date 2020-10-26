package com.example.bipuldevashish.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bipuldevashish.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    TextView noAccount;
    Button logIn;
    EditText emailNum,password;
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Firebase Auth Init
        firebaseAuth = FirebaseAuth.getInstance();

        // Progress Dialog Init
        progressDialog = new ProgressDialog(this);

        // View Ids
        noAccount = findViewById(R.id.noAccount);
        logIn = findViewById(R.id.loginBtn);
        emailNum = findViewById(R.id.etMobile);
        password = findViewById(R.id.etPassword);


        // OnClick LogIn Button
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkAndLogInUser();
            }
        });

        // OnClick NoAccount -> SignUp Activity
        noAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });


    }

    private void checkAndLogInUser() {

        String user_email = emailNum.getText().toString().trim();
        String user_pass = password.getText().toString().trim();

        if (user_email.isEmpty())
        {
            emailNum.setError("Enter Mobile Number");
        }
        if (user_pass.isEmpty())
        {
            password.setError("Enter Password");
        }
        else
        {
            progressDialog.setTitle("Logging You In ");
            progressDialog.setMessage("Confirming your identity");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            LogIn(user_email,user_pass);
        }


    }

    private void LogIn(String user_email, String user_pass) {

        firebaseAuth.signInWithEmailAndPassword(user_email+"@trader.com",user_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Intent goHome = new Intent(Login.this, Home.class);
                    goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(goHome);
                    finish();
                }

                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(Login.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
}