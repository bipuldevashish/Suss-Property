package com.example.bipuldevashish.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.bipuldevashish.Activity.Profile;
import com.example.bipuldevashish.Adapter.SellAdapter;
import com.example.bipuldevashish.Models.SellModel;
import com.example.bipuldevashish.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class EditPostFragment extends Fragment {

    //Variable declaration
    Spinner spinnerFlatLayout, spinnerType, spinnerFacing;
    String spinnerLayoutResult, spinnerTResult, spinnerFaceResult;
    Button saveDetails;
    ProgressDialog progressDialog;
    EditText editTextArea, editTextRate, editTextDescription, editTextAddress;
    FirebaseDatabase database;
    DatabaseReference reference;
    private ArrayList<String> propertyImageArray = new ArrayList<>();
    final String TAG = "EditPostFragment";
    StorageReference storageReference;
    String value;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View parentView = inflater.inflate(R.layout.fragment_edit_post, container, false);

        //Linking all the xml elements
        spinnerType = parentView.findViewById(R.id.spinner_typeEditPost);
        spinnerFacing = parentView.findViewById(R.id.spinner_facingEditPost);
        spinnerFlatLayout = parentView.findViewById(R.id.spinner_flatLayoutEditPost);
        saveDetails = parentView.findViewById(R.id.saveDetails);
        editTextArea = parentView.findViewById(R.id.edText_areaEditPost);
        editTextAddress = parentView.findViewById(R.id.edText_addressEditPost);
        editTextDescription = parentView.findViewById(R.id.edText_descriptionEditPost);
        editTextRate = parentView.findViewById(R.id.edText_rateEditPost);

        //shared preference
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("post", Context.MODE_PRIVATE);
        value = sharedPreferences.getString("newKey", "");


        // firebase linkage
        database = FirebaseDatabase.getInstance();
        Log.d(TAG, "value of value = " + value);
        reference = database.getReference().child("Postdetails").child(value);

        Log.d(TAG, "postkey = " + value);

        saveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                varifyFields();
            }
        });
        //spinner flatlayout implemented
        ArrayAdapter<CharSequence> myAdapterFlatLayout = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.house_type_array, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        myAdapterFlatLayout.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerFlatLayout.setAdapter(myAdapterFlatLayout);


        spinnerFlatLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Log.d(TAG, "Value of position" + position);
                spinnerLayoutResult = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), " Please select a property layout option", Toast.LENGTH_SHORT).show();
            }
        });

        //spinner Property Type implemented
        ArrayAdapter<CharSequence> myAdapterType = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.propertyType, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        myAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerType.setAdapter(myAdapterType);


        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                Log.d(TAG, "Value of position" + position);
                spinnerTResult = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), " Please select a property type option", Toast.LENGTH_SHORT).show();
            }
        });


        //spinner property facing implemented
        ArrayAdapter<CharSequence> myAdapterFacing = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.facing, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        myAdapterFacing.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerFacing.setAdapter(myAdapterFacing);


        spinnerFacing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG, "Value of position" + position);
                spinnerFaceResult = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        setPreviousData();
        return parentView;
    }

    private void varifyFields() {
        String area, desc, address, rate;
        area = editTextArea.getText().toString();
        desc = editTextDescription.getText().toString();
        rate = editTextRate.getText().toString();
        address = editTextAddress.getText().toString();


        if (spinnerTResult.isEmpty() || spinnerTResult.equals("Select Property Type")) {
            Toast.makeText(getContext(), "Please Enter Property Type", Toast.LENGTH_SHORT).show();
        } else if (spinnerLayoutResult.isEmpty() || spinnerTResult.equals("Select BHK")) {
            Toast.makeText(getContext(), "Please Select Property Layout", Toast.LENGTH_SHORT).show();
        } else if (spinnerFaceResult.isEmpty() || spinnerFaceResult.equals("Select Facing Direction")) {
            Toast.makeText(getContext(), "Please Enter PropertyType", Toast.LENGTH_SHORT).show();
        } else if (area.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter The Plot Area", Toast.LENGTH_SHORT).show();
        } else if (rate.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Rate", Toast.LENGTH_SHORT).show();
        } else if (desc.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Detailed Description", Toast.LENGTH_SHORT).show();
        } else if (address.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Full Address", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Updating Post");
            progressDialog.setMessage("Just a moment");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            saveModifiedResult(rate, address, area, desc);
        }

    }

    private void saveModifiedResult(String rate, String address, String area, String desc) {

        reference.child("address").setValue(address);
        reference.child("area").setValue(area);
        reference.child("bhk").setValue(spinnerLayoutResult);
        reference.child("description").setValue(desc);
        reference.child("facing").setValue(spinnerFaceResult);
        reference.child("rate").setValue(rate);
        reference.child("type").setValue(spinnerTResult);

        progressDialog.dismiss();
        Intent goHome = new Intent(getActivity(), Home.class);
        goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(goHome);


    }

    private void setPreviousData() {
        reference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        // [START_EXCLUDE]
                        if (dataSnapshot.exists()) {

                            // fetching data from firebase
                            String rate = dataSnapshot.child("rate").getValue(String.class);
                            String address = dataSnapshot.child("address").getValue(String.class);
                            String description = dataSnapshot.child("description").getValue(String.class);
                            String area = dataSnapshot.child("area").getValue(String.class);
                            String bhk = dataSnapshot.child("bhk").getValue(String.class);
                            String type = dataSnapshot.child("type").getValue(String.class);
                            String facing = dataSnapshot.child("facing").getValue(String.class);
                            Log.d(TAG, "update database working Moving to setProfile");


                            setEditFragment(rate, address, description, area, bhk, type, facing);


                        } else {

                            Log.d(TAG, "User " + " is unexpectedly null");
                            Toast.makeText(getContext(),
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

    private void setEditFragment(String rate, String address, String description, String area, String bhk, String type, String facing) {
        int item_pos_type = 0;
        int item_count_type = 0;
        int posi_type = 0;

        String[] property_attri_type = getResources().getStringArray(R.array.propertyType);
        item_count_type = property_attri_type.length;
        Log.d(TAG, "length of property " + item_count_type);

        while (item_pos_type < item_count_type) {
            // Compare with the search text
            if (property_attri_type[item_pos_type].equals(type))
                posi_type = item_pos_type;

            item_pos_type = item_pos_type + 1;
        }
        spinnerType.setSelection(posi_type);


        int item_pos_bhk = 0;
        int item_count_bhk = 0;
        int posi_bhk = 0;

        String[] property_attri_bhk = getResources().getStringArray(R.array.house_type_array);
        item_count_bhk = property_attri_bhk.length;
        Log.d(TAG, "value of bhk " + bhk);
        Log.d(TAG, "length of property " + item_count_bhk);

        while (item_pos_bhk < item_count_bhk) {
            // Compare with the search text
            if (property_attri_bhk[item_pos_bhk].equals(bhk))
                posi_bhk = item_pos_bhk;

            item_pos_bhk = item_pos_bhk + 1;
        }
        spinnerFlatLayout.setSelection(posi_bhk);

        int item_pos_layout = 0;
        int item_count_layout = 0;
        int posi_layout = 0;

        String[] property_attri_layout = getResources().getStringArray(R.array.facing);
        item_count_layout = property_attri_layout.length;
        Log.d(TAG, "length of property " + item_count_bhk);

        while (item_pos_layout < item_count_layout) {
            // Compare with the search text
            if (property_attri_layout[item_pos_layout].equals(facing))
                posi_layout = item_pos_layout;

            item_pos_layout = item_pos_layout + 1;
        }
        spinnerFacing.setSelection(posi_layout);

        editTextRate.setText(rate);
        editTextAddress.setText(address);
        editTextArea.setText(area);
        editTextDescription.setText(description);
    }
}