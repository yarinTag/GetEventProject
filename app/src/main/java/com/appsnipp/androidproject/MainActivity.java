package com.appsnipp.androidproject;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {

    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.mainActivityNavHost);
  //     NavigationUI.setupActionBarWithNavController(this,navController);
    }
}