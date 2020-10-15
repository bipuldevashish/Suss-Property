package com.example.bipuldevashish.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.bipuldevashish.Fragments.BuyFragment;
import com.example.bipuldevashish.Fragments.HomeFragment;
import com.example.bipuldevashish.Fragments.SellFragment;
import com.example.bipuldevashish.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    TextView tvFragmentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        tvFragmentName = findViewById(R.id.fragmentName);
        bottomNavigationView = findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        loadFragment(new HomeFragment());


        CircleImageView setAccount = findViewById(R.id.userProfileImage);
        setAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Profile.class));
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentFrame, fragment);
        transaction.commit();
    }

    private final BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            int id = menuItem.getItemId();
            if(id == R.id.nav_buy) {
                tvFragmentName.setText("Buy");
                loadFragment(new BuyFragment());
                return true;
            }
            else if(id == R.id.nav_home) {
                tvFragmentName.setText("Home");
                loadFragment(new HomeFragment());
                return true;

            }
            else if(id == R.id.nav_sell) {
                tvFragmentName.setText("Sell");
                loadFragment(new SellFragment());
                return true;
            }
            return true;
        }
    };
}