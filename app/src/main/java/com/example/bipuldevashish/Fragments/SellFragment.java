package com.example.bipuldevashish.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;


public class SellFragment extends Fragment {

    Spinner spinnerFlatLayout, spinnerType, spinnerFacing;
    String spinnerFlatLayoutResult, spinnerTypeResult, spinnerFacingResult;
    Button saveAndContinue;
    EditText editPlotArea, editRate, editDescription, editAddress;
    FirebaseDatabase database;
    DatabaseReference reference;
    ProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_sell, container, false);
        //Linking all the spinners to ui
        spinnerType = rootView.findViewById(R.id.spinner_type);
        spinnerFacing = rootView.findViewById(R.id.spinner_facing);
        spinnerFlatLayout = rootView.findViewById(R.id.spinner_flatLayout);
        saveAndContinue = rootView.findViewById(R.id.buttonSaveandContinue);
        editPlotArea = rootView.findViewById(R.id.edText_areaInSqrft);
        editAddress = rootView.findViewById(R.id.edText_address);
        editDescription = rootView.findViewById(R.id.edText_description);
        editRate = rootView.findViewById(R.id.edText_rate);

        // firebase linkage
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Postdetails");
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
                spinnerFlatLayoutResult = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                spinnerTypeResult = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
                spinnerFacingResult = parent.getItemAtPosition(position).toString();

                Toast.makeText(getContext(), spinnerFlatLayoutResult, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateSellingDetails();
            }
        });
        return rootView;
    }

    private void validateSellingDetails() {

        String rate = editRate.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String plotArea = editPlotArea.getText().toString().trim();
        String description = editDescription.getText().toString().trim();

        if (rate.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Rate", Toast.LENGTH_SHORT).show();
        }
        if (address.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Full Address", Toast.LENGTH_SHORT).show();
        }
        if (plotArea.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter The Plot Area", Toast.LENGTH_SHORT).show();
        }
        if (description.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter Detailed Description", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "data filled properly", Toast.LENGTH_SHORT).show();
            uploadData(rate, address, plotArea, description);
        }

    }

    private void uploadData(String rate, String address, String plotArea, String description) {

        String id = reference.push().getKey();
        final HashMap<String, Object> UserNewsDb = new HashMap<>();

        UserNewsDb.put("type", spinnerTypeResult);
        UserNewsDb.put("bhk", spinnerFlatLayoutResult);
        UserNewsDb.put("facing", spinnerFacingResult);
        UserNewsDb.put("area", plotArea);
        UserNewsDb.put("rate", rate);
        UserNewsDb.put("address", address);
        UserNewsDb.put("description", description);

        reference.child(id).setValue(UserNewsDb).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent goHome = new Intent(getActivity(), Home.class);
                    goHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(goHome);
                } else {
                    Log.d("RegisterActivity", "Couldnt save Seller details");
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.hide();
                }
            }
        });


    }
}