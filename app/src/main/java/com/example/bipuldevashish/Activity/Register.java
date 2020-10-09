package com.example.bipuldevashish.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bipuldevashish.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {

    EditText name,email,mobile,password,confirmPass;
    Button btnRegister;

    FirebaseAuth firebaseAuth;
    DatabaseReference userDb;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // Auth Init
        firebaseAuth = FirebaseAuth.getInstance();
        // Database Init
        userDb = FirebaseDatabase.getInstance().getReference();

        //ProgressDialog
        progressDialog = new ProgressDialog(this);


        // UI Linkage ----->
        name = findViewById(R.id.etName);
        email = findViewById(R.id.etEmail);
        mobile = findViewById(R.id.etMobile);
        password = findViewById(R.id.etPassword);
        confirmPass = findViewById(R.id.etCpassword);
        btnRegister = findViewById(R.id.registerBtn);

        // ---->

        // OnClick Function Calls
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkAndSignUp();

            }
        });

    }

    private void checkAndSignUp() {

        String user_name = name.getText().toString().trim();
        String user_email = email.getText().toString().trim();
        String user_mobile = mobile.getText().toString().trim();
        String user_password = password.getText().toString().trim();
        String user_cpassword = confirmPass.getText().toString().trim();

        if (user_name.isEmpty())
        {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
        }
        if (user_email.isEmpty())
        {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        }
        if (user_mobile.isEmpty())
        {
            Toast.makeText(this, "Please Enter Mobile", Toast.LENGTH_SHORT).show();
        }
        if (user_password.isEmpty())
        {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        if (user_cpassword.isEmpty())
        {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else if (user_password.equals(user_cpassword))
        {
            progressDialog.setTitle("Registering User");
            progressDialog.setMessage("Please wait while We Create Your Account");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            authenticate(user_name,user_email,user_mobile,user_password);
        }
        else {

            Toast.makeText(this, "Password not same", Toast.LENGTH_SHORT).show();
        }

    }

    private void authenticate(final String user_name, final String user_email, final String user_mobile, final String user_password)
    {

        firebaseAuth.createUserWithEmailAndPassword(user_mobile+"@trade.com",user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                if (task.isSuccessful())
                {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String userId = currentUser.getUid();

                    //Initialising root base for user details
                    userDb = userDb.child("Users").child(userId);

                    HashMap<String,String> userNode = new HashMap<>();

                    userNode.put("Name",user_name);
                    userNode.put("Email",user_email);
                    userNode.put("Mobile",user_mobile);
                    userNode.put("Password",user_password);
                    userNode.put("ProfilePicture","https://www.nicepng.com/png/detail/780-7805650_generic-user-image-male-man-cartoon-no-eyes.png");
                    userNode.put("State","");
                    userNode.put("followers","0");
                    userNode.put("following","0");
                    userNode.put("posts","0");


                    userDb.setValue(userNode).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            {
                                progressDialog.dismiss();
                                Intent goHome = new Intent(Register.this, Home.class);
                                goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(goHome);
                            }

                            else
                            {
                                Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    progressDialog.hide();
                    Toast.makeText(Register.this, "Number already registered !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}