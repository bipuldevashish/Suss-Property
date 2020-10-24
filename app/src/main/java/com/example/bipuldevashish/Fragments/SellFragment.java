package com.example.bipuldevashish.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.bipuldevashish.Activity.Home;
import com.example.bipuldevashish.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.example.bipuldevashish.Activity.Profile.PICK_IMAGE;


public class SellFragment extends Fragment {

    Spinner spinnerFlatLayout, spinnerType, spinnerFacing;
    String spinnerFlatLayoutResult, spinnerTypeResult, spinnerFacingResult;
    Button saveAndContinue, buttonAttachment;
    EditText editPlotArea, editRate, editDescription, editAddress;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Uri> imageList = new ArrayList<>();
    private ArrayList<String> propertyImageArray = new ArrayList<>();
    private String pushGeneratorKey;
    ProgressDialog progressDialog;
    final String TAG = "SellFragment";
    StorageReference storageReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_sell, container, false);

        //Linking all the xml elements
        spinnerType = rootView.findViewById(R.id.spinner_type);
        spinnerFacing = rootView.findViewById(R.id.spinner_facing);
        spinnerFlatLayout = rootView.findViewById(R.id.spinner_flatLayout);
        saveAndContinue = rootView.findViewById(R.id.buttonSaveandContinue);
        buttonAttachment = rootView.findViewById(R.id.buttonAttachment);
        editPlotArea = rootView.findViewById(R.id.edText_areaInSqrft);
        editAddress = rootView.findViewById(R.id.edText_address);
        editDescription = rootView.findViewById(R.id.edText_description);
        editRate = rootView.findViewById(R.id.edText_rate);

        // firebase linkage
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Postdetails");

        pushGeneratorKey = reference.push().getKey();
        storageReference = FirebaseStorage.getInstance().getReference().child("Property Images").child(pushGeneratorKey);

        //spinner flatlayout implemented
        ArrayAdapter<CharSequence> myAdapterFlatLayout = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.house_type_array, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        myAdapterFlatLayout.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerFlatLayout.setAdapter(myAdapterFlatLayout);
        spinnerFlatLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Log.d(TAG, "Value of position" + position);
                spinnerFlatLayoutResult = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), " Please select a property layout option", Toast.LENGTH_SHORT).show();
            }
        });

        //spinner Property Type implemented
        ArrayAdapter<CharSequence> myAdapterType = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.propertyType, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        myAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerType.setAdapter(myAdapterType);
        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Log.d(TAG, "Value of position" + position);
                spinnerTypeResult = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), " Please select a property type option", Toast.LENGTH_SHORT).show();
            }
        });

        //spinner property facing implemented
        ArrayAdapter<CharSequence> myAdapterFacing = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.facing, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        myAdapterFacing.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerFacing.setAdapter(myAdapterFacing);
        spinnerFacing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "Value of position" + position);
                spinnerFacingResult = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), " Please select a facing option", Toast.LENGTH_SHORT).show();
            }
        });


        //save and continue button functionallity added
        saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSellingDetails();
            }
        });


        //Attachment button functionality added
        buttonAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickFromGallery();
            }
        });


        return rootView;

    }

    private void pickFromGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            int countClipData = data.getClipData().getItemCount();
            Log.d(TAG, "value of countdata = " + countClipData);

            if (countClipData == 5) {
                int currentImageSelected = 0;
                while (currentImageSelected < countClipData) {
                    Uri imageUri = data.getClipData().getItemAt(currentImageSelected).getUri();
                    imageList.add(imageUri);
                    Log.d(TAG, "value of currentImageSelected = " + currentImageSelected);
                    currentImageSelected = currentImageSelected + 1;
                    uploadImage();
                }
            } else {
                Toast.makeText(getContext(), "Please select exactly 5 Images", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Please select Images", Toast.LENGTH_SHORT).show();
        }
    }


    private void uploadImage() {

        Log.d(TAG, "Entering uploadImage()");

        int uploadCount;
        for (uploadCount = 0; uploadCount < imageList.size(); uploadCount++) {

            Uri individualImage = imageList.get(uploadCount);
            final StorageReference imageName = storageReference.child("image" + individualImage.getLastPathSegment());

            Log.d(TAG, "value of uploadCount = " + uploadCount);

            imageName.putFile(individualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = String.valueOf(uri);
                            propertyImageArray.add(url);

                        }
                    });
                }
            });


        }
    }


    private void validateSellingDetails() {

        String rate = editRate.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String plotArea = editPlotArea.getText().toString().trim();
        String description = editDescription.getText().toString().trim();


        if (spinnerTypeResult.isEmpty() || spinnerTypeResult.equals("Select Property Type")) {
            Toast.makeText(getContext(), "Please Enter Property Type", Toast.LENGTH_SHORT).show();
        } else if (spinnerFlatLayoutResult.isEmpty() || spinnerTypeResult.equals("Select BHK")) {
            Toast.makeText(getContext(), "Please Select Property Layout", Toast.LENGTH_SHORT).show();
        } else if (spinnerFacingResult.isEmpty() || spinnerTypeResult.equals("Select Facing Direction")) {
            Toast.makeText(getContext(), "Please Enter PropertyType", Toast.LENGTH_SHORT).show();
        } else if (plotArea.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter The Plot Area", Toast.LENGTH_SHORT).show();
        } else if (rate.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Rate", Toast.LENGTH_SHORT).show();
        } else if (description.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Detailed Description", Toast.LENGTH_SHORT).show();
        } else if (address.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Full Address", Toast.LENGTH_SHORT).show();
        } else if (propertyImageArray.isEmpty()) {
            Toast.makeText(getContext(), "Please select exactly five images", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Sit Back !");
            progressDialog.setMessage("While We Save Your Data");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            uploadData(rate, address, plotArea, description);

        }

    }

    private void uploadData(String rate, String address, String plotArea, String description) {
        String sellerID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "sellerID = " + sellerID);

        final HashMap<String, Object> UserNewsDb = new HashMap<>();

        UserNewsDb.put("type", spinnerTypeResult);
        UserNewsDb.put("bhk", spinnerFlatLayoutResult);
        UserNewsDb.put("facing", spinnerFacingResult);
        UserNewsDb.put("area", plotArea);
        UserNewsDb.put("rate", rate);
        UserNewsDb.put("address", address);
        UserNewsDb.put("description", description);
        UserNewsDb.put("Seller ID", sellerID);

        for (int i = 0; i < 5; i++) {
            String imageulr = propertyImageArray.get(i);
            UserNewsDb.put("image" + i, imageulr);
        }


        reference.child(pushGeneratorKey).setValue(UserNewsDb).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Intent goHome = new Intent(getActivity(), Home.class);
                    goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(goHome);
                } else {
                    Log.d("RegisterActivity", "Couldnt save Seller details");
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });


    }
}