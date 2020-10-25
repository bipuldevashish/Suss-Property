package com.example.bipuldevashish.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bipuldevashish.Adapter.SellAdapter;
import com.example.bipuldevashish.Models.SellModel;
import com.example.bipuldevashish.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomeFragment extends Fragment {

    DatabaseReference sellDetailsNode;
    RecyclerView sellPostRecyclerView;
    SellAdapter sellAdapter;
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sellPostRecyclerView = view.findViewById(R.id.recyclerViewSell);




        fetchSellPost();

        return view;
    }

    private void fetchSellPost() {

        sellDetailsNode =  FirebaseDatabase.getInstance().getReference().child("Postdetails");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        sellPostRecyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<SellModel> options =
                new FirebaseRecyclerOptions.Builder<SellModel>()
                        .setQuery(sellDetailsNode, SellModel.class)
                        .build();


        sellAdapter = new SellAdapter(options);
        sellPostRecyclerView.setAdapter(sellAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();

        sellAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();

        sellAdapter.stopListening();
    }
}