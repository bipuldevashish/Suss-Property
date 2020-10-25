package com.example.bipuldevashish.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

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

import com.example.bipuldevashish.Adapter.SellAdapter;
import com.example.bipuldevashish.Models.SellModel;
import com.example.bipuldevashish.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class EditPostFragment extends Fragment {

    //Variable declaration
    Spinner spinnerFlatLayout, spinnerType, spinnerFacing;
    String spinnerFlatLayoutResult, spinnerTypeResult, spinnerFacingResult;
    Button saveAndContinue, buttonAttachment;
    EditText editPlotArea, editRate, editDescription, editAddress;
    FirebaseDatabase database;
    DatabaseReference reference;
    ArrayList<Uri> imageList = new ArrayList<>();
    private ArrayList<String> propertyImageArray = new ArrayList<>();
    ProgressDialog progressDialog;
    final String TAG = "EditPostFragment";
    StorageReference storageReference;
    String key;


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
        saveAndContinue = parentView.findViewById(R.id.saveDetails);
        buttonAttachment = parentView.findViewById(R.id.buttonAttachmentEditPost);
        editPlotArea = parentView.findViewById(R.id.edText_areaEditPost);
        editAddress = parentView.findViewById(R.id.edText_addressEditPost);
        editDescription = parentView.findViewById(R.id.edText_descriptionEditPost);
        editRate = parentView.findViewById(R.id.edText_rateEditPost);

        // firebase linkage
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Postdetails");


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
                spinnerFlatLayoutResult = parent.getItemAtPosition(position).toString();

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
                spinnerTypeResult = parent.getItemAtPosition(position).toString();
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
                spinnerFacingResult = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), " Please select a facing option", Toast.LENGTH_SHORT).show();
            }
        });

        setPreviousData();
        return parentView;
    }

    private void setPreviousData() {

        Log.d(TAG, "postkey = " + key);
    }
}