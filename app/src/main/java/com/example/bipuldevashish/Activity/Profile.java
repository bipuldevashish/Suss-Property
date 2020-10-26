package com.example.bipuldevashish.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bipuldevashish.Models.SellModel;
import com.example.bipuldevashish.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    //variable declaration
    TextView usernameTextView, userEmailTextView, userPhoneNumberTextView;
    EditText etextName, etextEmail;
    Button saveButton, editProfileButton;
    ImageView imageViewproFilepic;
    ProgressDialog progressDialog;
    private DatabaseReference mDatabase;
    DatabaseReference reference;
    private FirebaseAuth mAuth;
    final String TAG = "Profile";
    public static final int PICK_IMAGE = 1;
    Uri imageUri;
    private String myUri;
    private StorageReference storageProfilePicsRef;
    private StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //EditProfile Button Linked
        editProfileButton = findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RelativeLayout linearLayoutProfileDetails = findViewById(R.id.profileDetailForm);
                LinearLayout linearLayoutUpdateProfile = findViewById(R.id.updateProfileForm);
                linearLayoutProfileDetails.setVisibility(View.GONE);
                linearLayoutUpdateProfile.setVisibility(View.VISIBLE);

                //profile pic button clickable enabled
                imageViewproFilepic = findViewById(R.id.userProfile_image);
                imageViewproFilepic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        pickImageFromGallery();

                    }
                });
            }
        });


        // Save profile Button Linked
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
                RelativeLayout linearLayoutProfileDetails = findViewById(R.id.profileDetailForm);
                LinearLayout linearLayoutUpdateProfile = findViewById(R.id.updateProfileForm);
                linearLayoutProfileDetails.setVisibility(View.VISIBLE);
                linearLayoutUpdateProfile.setVisibility(View.GONE);
                imageViewproFilepic.setClickable(false);
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading ");
        progressDialog.setMessage("Retrieving profile data");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        //Linking the variables to the UI
        usernameTextView = findViewById(R.id.userName);
        userEmailTextView = findViewById(R.id.userEmail);
        userPhoneNumberTextView = findViewById(R.id.userPhoneNumber);
        etextName = findViewById(R.id.etNameUpdateProfile);
        etextEmail = findViewById(R.id.etEmailUpdateProfile);
        imageViewproFilepic = findViewById(R.id.userProfile_image);

        //Firebase reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        reference = mDatabase.child("Users").child(userId);
        mAuth = FirebaseAuth.getInstance();
        storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child("Profile Pic");
        Log.d(TAG, "entering updateDatabase()");
        updateDatabase();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    private void updateDatabase() {
        reference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // [START_EXCLUDE]
                        if (dataSnapshot.exists()) {

                            // fetching data from firebase
                            String fullName = dataSnapshot.child("Name").getValue(String.class);
                            String Email = dataSnapshot.child("Email").getValue(String.class);
                            String Mobile = dataSnapshot.child("Mobile").getValue(String.class);
                            String Image = dataSnapshot.child("image").getValue(String.class);
                            Log.d(TAG, "update database working Moving to setProfile");
                            setProfile(fullName, Email, Mobile, Image);
                            progressDialog.dismiss();

                        } else {

                            Log.d(TAG, "User " + " is unexpectedly null");
                            Toast.makeText(Profile.this,
                                    "Error: could not fetch user.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Log.w(TAG, "getUser:onCancelled", error.toException());
                    }
                });
    }

    private void updateProfile() {
        String NewName = etextName.getText().toString();
        String NewEmail = etextEmail.getText().toString();
        reference.child("Name").setValue(NewName);
        reference.child("Email").setValue(NewEmail);
        Log.d(TAG, "New name and email = " + NewName + " " + NewEmail);
        updateDatabase();
    }

    public void setProfile(String name, String email, String mobile, String image) {
        usernameTextView.setText(name);
        userEmailTextView.setText(email);
        userPhoneNumberTextView.setText(mobile);
        etextName.setText(name);
        etextEmail.setText(email);
        if (image.length()==0) {
            imageViewproFilepic.setImageResource(R.drawable.ic_profile);
            Log.d(TAG, "value of image inside if" + image);
        } else {
            Picasso.get().load(image).into(imageViewproFilepic);
            Log.d(TAG, "value of image inside else" + image);
            progressDialog.dismiss();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            imageUri = data.getData();
            uploadProfileImage();
            updateProfile();

        }
    }

    private void uploadProfileImage() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please wait ,While we are setting your profile");
        progressDialog.show();

        if (imageUri != null) {
            final StorageReference fileRef = storageProfilePicsRef.child(mAuth.getCurrentUser().getUid() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUri);

                        reference.updateChildren(userMap);
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }

}

