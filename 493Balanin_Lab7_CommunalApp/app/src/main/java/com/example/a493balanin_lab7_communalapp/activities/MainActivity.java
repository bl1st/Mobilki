package com.example.a493balanin_lab7_communalapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.example.a493balanin_lab7_communalapp.CountersFragment;
import com.example.a493balanin_lab7_communalapp.LocationsFragment;
import com.example.a493balanin_lab7_communalapp.MeasurementsFragment;
import com.example.a493balanin_lab7_communalapp.R;
import com.example.a493balanin_lab7_communalapp.RatesFragment;
import com.example.a493balanin_lab7_communalapp.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private String token;
    ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent i = getIntent();
        token = i.getStringExtra("token");



        replaceFragment(new MeasurementsFragment());

        binding.botNavBar.setOnItemSelectedListener(item ->
                {
                    switch (item.getItemId())
                    {
                        case R.id.Counters:
                            replaceFragment(new CountersFragment());
                            break;
                        case R.id.Measurements:
                            replaceFragment(new MeasurementsFragment());
                            break;
                        case R.id.Rates:
                            replaceFragment(new RatesFragment());
                            break;
                        case R.id.Locations:
                            replaceFragment(new LocationsFragment(token));
                            break;
                    }
                    return true;
                });
    }


    private void replaceFragment(Fragment fragment )
    {
        FragmentManager manager = getSupportFragmentManager();

        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.frameLayout,fragment);
        ft.commit();
    }

}