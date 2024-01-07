package com.example.practice2023_pccomponents.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.practice2023_pccomponents.R;
import com.example.practice2023_pccomponents.models.OrderedProduct;
import com.example.practice2023_pccomponents.services.OrderContainer;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public OrderContainer orderContainter = new OrderContainer();
    FragmentContainerView controllerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.my_nav);
        NavController navController = Navigation.findNavController(this,  R.id.hostFragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }


    public void CANYOUSEMEE()
    {

    }



}