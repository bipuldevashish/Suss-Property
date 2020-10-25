package com.example.bipuldevashish.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bipuldevashish.Fragments.EditPostFragment;
import com.example.bipuldevashish.Fragments.SellFragment;
import com.example.bipuldevashish.Models.SellModel;
import com.example.bipuldevashish.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SellAdapter extends FirebaseRecyclerAdapter<SellModel, SellAdapter.SellViewHolder> {

    private static final String TAG = "EditPostFragment";
    Context mcontext;
    TextView tvFragmentName;
    public String postKey;

    public SellAdapter(@NonNull FirebaseRecyclerOptions<SellModel> options) {
        super(options);

    }


    @Override
    protected void onBindViewHolder(@NonNull final SellViewHolder holder, final int position, @NonNull SellModel model) {

        holder.houseType.setText(model.getType());
        holder.houseAddress.setText(model.getAddress());
        holder.houseBHK.setText(model.getBhk() + " |  ");
        holder.housePlot.setText(model.getArea());
        holder.houseFace.setText(model.getFacing());
        holder.houseAboutDesc.setText(model.getDescription());
        holder.housePrice.setText("$ " + model.getRate());

        holder.buttonViewOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
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
                                loadFragment(new EditPostFragment());
                                break;
                            case R.id.delete_post:
                                //handle delete post click
                                postKey = getRef(position).getKey();
                                deletePost(postKey);
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

    //To load fragment
    private void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) mcontext;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentFrame, fragment);
        transaction.commit();
    }

    //To delete the post
    private void deletePost(String key) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Postdetails").child(key);
        ref.removeValue();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Property Images").child(key);
        storageReference.delete();
    }


    @NonNull
    @Override
    public SellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.sell_cards, parent, false);

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
