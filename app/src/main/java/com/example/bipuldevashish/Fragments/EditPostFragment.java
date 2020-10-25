package com.example.bipuldevashish.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.example.bipuldevashish.Activity.Profile;
import com.example.bipuldevashish.Adapter.SellAdapter;
import com.example.bipuldevashish.Models.SellModel;
import com.example.bipuldevashish.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
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
        saveAndContinue = parentView.findViewById(R.id.saveDetails);
        buttonAttachment = parentView.findViewById(R.id.buttonAttachmentEditPost);
        editPlotArea = parentView.findViewById(R.id.edText_areaEditPost);
        editAddress = parentView.findViewById(R.id.edText_addressEditPost);
        editDescription = parentView.findViewById(R.id.edText_descriptionEditPost);
        editRate = parentView.findViewById(R.id.edText_rateEditPost);

        //shared preference
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("post", Context.MODE_PRIVATE);
        value = sharedPreferences.getString("PostKey", "");


        // firebase linkage
        database = FirebaseDatabase.getInstance();
        Log.d(TAG, "value of value = " + value);
        reference = database.getReference().child("Postdetails").child(value);

        Log.d(TAG, "postkey = " + value);

        //spinner flatlayout implemented
        ArrayAdapter<CharSequence> myAdapterFlatLayout = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.house_type_array, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        myAdapterFlatLayout.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerFlatLayout.setAdapter(myAdapterFlatLayout);


//        spinnerFlatLayout.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//                Log.d(TAG, "Value of position" + position);
//                spinnerFlatLayoutResult = parent.getItemAtPosition(position).toString();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getContext(), " Please select a property layout option", Toast.LENGTH_SHORT).show();
//            }
//        });

        //spinner Property Type implemented
        ArrayAdapter<CharSequence> myAdapterType = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.propertyType, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        myAdapterType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinnerType.setAdapter(myAdapterType);


//        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//
//                Log.d(TAG, "Value of position" + position);
//                spinnerTypeResult = parent.getItemAtPosition(position).toString();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Toast.makeText(getContext(), " Please select a property type option", Toast.LENGTH_SHORT).show();
//            }
//        });


        //spinner property facing implemented
        ArrayAdapter<CharSequence> myAdapterFacing = ArrayAdapter.createFromResource(parentView.getContext(),
                R.array.facing, android.R.layout.simple_list_item_1);
        // Specify the layout to use when the list of choices appears
        myAdapterFacing.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerFacing.setAdapter(myAdapterFacing);


//        spinnerFacing.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                Log.d(TAG, "Value of position" + position);
//                spinnerFacingResult = parent.getItemAtPosition(position).toString();
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });


        setPreviousData();
        return parentView;
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

                            progressDialog.dismiss();

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
        int item_pos = 0;
        int item_count = 0;
        int posi = 0;

//        String[] property_type = getResources().getStringArray(R.array.propertyType);
//        item_count = property_type.length;
//        Log.d(TAG, "length of property " + item_count);
//
//        while (item_pos < item_count) {
//            // Compare with the search text
//            if ( property_type[item_pos].equals(type))
//                posi = item_pos;
//            item_pos +=1;
//        }
//        spinnerType.setSelection(posi);

        editRate.setText(rate);
        editAddress.setText(address);
        editPlotArea.setText(area);
        editDescription.setText(description);
    }
}