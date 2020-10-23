package com.example.bipuldevashish.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bipuldevashish.Models.SellModel;
import com.example.bipuldevashish.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class SellAdapter extends FirebaseRecyclerAdapter<SellModel,SellAdapter.SellViewHolder> {

    Context mcontext;


    public SellAdapter(@NonNull FirebaseRecyclerOptions<SellModel> options)
    {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull SellViewHolder holder, int position, @NonNull SellModel model) {

        holder.houseType.setText(model.getType());
        holder.houseAddress.setText(model.getAddress());
        holder.houseBHK.setText(model.getBhk()+" |  ");
        holder.housePlot.setText(model.getArea());
        holder.houseFace.setText(model.getFacing());
        holder.houseAboutDesc.setText(model.getDescription());
        holder.housePrice.setText("$ " + model.getRate());


    }

    @NonNull
    @Override
    public SellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                 .from(parent.getContext())
                 .inflate(R.layout.sell_cards,parent,false);

        // To get current context
        mcontext = parent.getContext();
        
        return new SellViewHolder(view);
    }

    public static class SellViewHolder extends RecyclerView.ViewHolder {


        TextView houseType;
        TextView houseAddress;
        TextView houseBHK;
        TextView housePlot;
        TextView houseFace;
        TextView housePrice;
        TextView houseAboutDesc;


        public SellViewHolder(@NonNull View itemView) {

            super(itemView);

            houseType = itemView.findViewById(R.id.houseType);
            houseAddress = itemView.findViewById(R.id.houseAddress);
            houseBHK = itemView.findViewById(R.id.houseBHK);
            housePlot = itemView.findViewById(R.id.housePlot);
            houseFace = itemView.findViewById(R.id.houseFace);
            housePrice = itemView.findViewById(R.id.housePrice);
            houseAboutDesc = itemView.findViewById(R.id.houseAboutDescription);
            
        }
    }
}
