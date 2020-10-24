package com.example.bipuldevashish.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
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


    // This code is used to get the key of the post on which we click or tap

//    String key = getRef(position).getKey();
//
//    SharedPreferences sharedPref = mcontext.getSharedPreferences("myKey", MODE_PRIVATE);
//    SharedPreferences.Editor editor = sharedPref.edit();
//    editor.putString("keyValue", key);
//    editor.apply();

    //-----------------------------------------------

    // use this code in the fragment or activity where u want to access that key of the repsective code for further database access

    // SharedPreferences sharedPreferences = context.getSharedPreferences("myKey", MODE_PRIVATE);
    //        value = sharedPreferences.getString("keyValue","");





    @Override
    protected void onBindViewHolder(@NonNull final SellViewHolder holder, int position, @NonNull SellModel model) {

        holder.houseType.setText(model.getType());
        holder.houseAddress.setText(model.getAddress());
        holder.houseBHK.setText(model.getBhk() + " |  ");
        holder.housePlot.setText(model.getArea());
        holder.houseFace.setText(model.getFacing());
        holder.houseAboutDesc.setText(model.getDescription());
        holder.housePrice.setText("$ " + model.getRate());

        holder.buttonViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mcontext, holder.buttonViewOptions);
                //inflating menu from xml resource
                popup.inflate(R.menu.postoptions);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit_post:
                                //handle edit post click

                                break;
                            case R.id.delete_post:
                                //handle delete post click

                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });
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
        TextView buttonViewOptions;


        public SellViewHolder(@NonNull View itemView) {

            super(itemView);

            houseType = itemView.findViewById(R.id.houseType);
            houseAddress = itemView.findViewById(R.id.houseAddress);
            houseBHK = itemView.findViewById(R.id.houseBHK);
            housePlot = itemView.findViewById(R.id.housePlot);
            houseFace = itemView.findViewById(R.id.houseFace);
            housePrice = itemView.findViewById(R.id.housePrice);
            houseAboutDesc = itemView.findViewById(R.id.houseAboutDescription);
            buttonViewOptions = itemView.findViewById(R.id.textViewOptions);
        }
    }
}
