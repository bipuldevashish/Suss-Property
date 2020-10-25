package com.example.bipuldevashish.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bipuldevashish.Activity.Message;
import com.example.bipuldevashish.Fragments.EditPostFragment;
import com.example.bipuldevashish.Models.SellModel;
import com.example.bipuldevashish.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SellAdapter extends FirebaseRecyclerAdapter<SellModel,SellAdapter.SellViewHolder> {

    Context mcontext;
    String postKey;
    Iterable<DataSnapshot> imageList;

    public SellAdapter(@NonNull FirebaseRecyclerOptions<SellModel> options)
    {
        super(options);

    }


    @Override
    protected void onBindViewHolder(@NonNull final SellViewHolder holder, final int position, @NonNull final SellModel model) {

        // Show Edit Delete Option Only When the Seller Post equals to UserId
        popUpDisplay(holder,position);
        getImageLinks(holder,position);
        holder.houseType.setText(model.getType());
        holder.houseAddress.setText(model.getAddress());
        holder.houseBHK.setText(model.getBhk() + " |  ");
        holder.housePlot.setText(model.getArea() + " sq.Ft");
        holder.houseFace.setText(model.getFacing());
        holder.houseAboutDesc.setText(model.getDescription());
        holder.housePrice.setText("$ " + model.getRate());

        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String key = getRef(position).getKey();
                fetchSellerNumber(key,model);


            }
        });

        holder.popUpOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = mcontext.getSharedPreferences("post", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("newKey", getRef(position).getKey());
                editor.apply();
                //creating a popup menu
                PopupMenu popup = new PopupMenu(mcontext, holder.popUpOptions, Gravity.RIGHT);
                //inflating menu from xml resource
                popup.inflate(R.menu.postoptions);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.edit_post:
                                //handle edit post click
                                //every time the value of the variable value in EditPostFragment
                                loadFragment(new EditPostFragment());
                                break;
                            case R.id.delete_post:
                                //handle delete post click
                                //deletepost() is working properly
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

    private void getImageLinks(final SellViewHolder holder, int position) {

        DatabaseReference propImageRef = FirebaseDatabase.getInstance().getReference().child("Postdetails").child(getRef(position).getKey()).child("images");

        propImageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String one = (String) snapshot.child("image0").getValue();
                String two = (String) snapshot.child("image1").getValue();
                String three = (String) snapshot.child("image2").getValue();
                String four = (String) snapshot.child("image3").getValue();
                String five = (String) snapshot.child("image4").getValue();

                List<SlideModel> slideModels = new ArrayList<>();
                slideModels.add(new SlideModel(one,"1"));
                slideModels.add(new SlideModel(two,"2"));
                slideModels.add(new SlideModel(three,"3"));
                slideModels.add(new SlideModel(four,"4"));
                slideModels.add(new SlideModel(five,"5"));
                holder.slider.setImageList(slideModels,true);




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void popUpDisplay(final SellViewHolder holder, int position) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserId = firebaseUser.getUid();

        DatabaseReference sellerIdRef = FirebaseDatabase.getInstance().getReference().child("Postdetails").child(getRef(position).getKey());

        sellerIdRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String sellerId = (String) snapshot.child("sellerID").getValue();
                checkSameOrNot(currentUserId,sellerId,holder);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // if same then show popup menu
    private void checkSameOrNot(String currentUserId, String sellerId, SellViewHolder holder) {

        if (currentUserId.equals(sellerId))
        {
            holder.popUpOptions.setVisibility(View.VISIBLE);
        }
    }

    private void fetchSellerNumber(String key, final SellModel model) {


        String bhk = model.getBhk();
        String address = model.getAddress();

        try {
            String mobile = "+919504901900";
            String msg = "Hello, I just saw your post on Suss Property App and I am interested.Your property at "+ address +" with "+ bhk;

            mcontext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobile + "&text=" + msg)));
        }catch (Exception e){
            Toast.makeText(mcontext, "Install Whatsapp", Toast.LENGTH_SHORT).show();
        }


    }



    private void loadFragment(Fragment fragment) {
        AppCompatActivity activity = (AppCompatActivity) mcontext;
        FragmentTransaction transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentFrame, fragment);
        transaction.commit();
    }

    private void deletePost(String postKey) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Postdetails").child(postKey);
        ref.removeValue();

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Property Images").child(postKey);
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
        TextView popUpOptions;
        ImageView contact;
        ImageSlider slider;



        public SellViewHolder(@NonNull View itemView) {

            super(itemView);

            houseType = itemView.findViewById(R.id.houseType);
            houseAddress = itemView.findViewById(R.id.houseAddress);
            houseBHK = itemView.findViewById(R.id.houseBHK);
            housePlot = itemView.findViewById(R.id.housePlot);
            houseFace = itemView.findViewById(R.id.houseFace);
            housePrice = itemView.findViewById(R.id.housePrice);
            houseAboutDesc = itemView.findViewById(R.id.houseAboutDescription);
            popUpOptions = itemView.findViewById(R.id.popUpOptions);
            contact = itemView.findViewById(R.id.whatsapp);
            slider = itemView.findViewById(R.id.houseImage);
        }
    }
}
