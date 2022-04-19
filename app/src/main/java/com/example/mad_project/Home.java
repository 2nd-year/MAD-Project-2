package com.example.mad_project;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home<placeholder> extends AppCompatActivity {

    BottomNavigationItemView placeHolder;
    BottomNavigationView bottomNavView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        placeHolder = findViewById(R.id.placeholder);
        bottomNavView = findViewById(R.id.bottomNavView);

        bottomNavView.setBackground(null);
        placeHolder.setClickable(false);
    }
}