package com.example.bipuldevashish.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bipuldevashish.Fragments.EditPostFragment;
import com.example.bipuldevashish.Fragments.HomeFragment;
import com.example.bipuldevashish.Fragments.SellFragment;
import com.example.bipuldevashish.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    // Constants -----------------------------------------------------------------------------------
    private static final int TIME_INTERVAL = 2000;
    private long mBackPressed;
    // ---------------------------------------------------------------------------------------------
    BottomNavigationView bottomNavigationView;
    TextView tvFragmentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // UI LINKS
        final CircleImageView setAccount = findViewById(R.id.userProfileImage);
        tvFragmentName = findViewById(R.id.fragmentName);
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        // -----------------------------------------------------------------------------------------

        // Load Default Frag : HomeFrag
        loadFragment(new HomeFragment());

        //setProfileIcon();
        // Go To Profile
        setAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //creating a popup menu
                PopupMenu popup = new PopupMenu(getApplicationContext(), setAccount);
                //inflating menu from xml resource
                popup.inflate(R.menu.button_logout);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.nav_profile:
                                //handle edit post click
                                startActivity(new Intent(Home.this, Profile.class));
                                break;
                            case R.id.nav_logout:
                                //handle delete post click
                                signout();
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

    private void signout() {
        FirebaseAuth mAUTH = FirebaseAuth.getInstance();
        mAUTH.signOut();
        startActivity(new Intent(Home.this, Login.class));
    }

    // Load Fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentFrame, fragment);
        transaction.commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            int id = menuItem.getItemId();

            if(id == R.id.nav_home) {
                tvFragmentName.setText("Home");
                loadFragment(new HomeFragment());
                return true;

            }
            else if(id == R.id.nav_sell) {
                tvFragmentName.setText("Property Details");
                loadFragment(new SellFragment());
                return true;
            }
            return true;
        }
    };


    // Avoid Closing App on Pressing back button
    @Override
    public void onBackPressed() {
        if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed();
            return;
        } else {
            Toast.makeText(getBaseContext(), "Double Click To Quit App", Toast.LENGTH_SHORT).show();
        }
        mBackPressed = System.currentTimeMillis();
    }
}