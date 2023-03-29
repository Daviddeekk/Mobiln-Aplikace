package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class VysledkyDatabaze extends AppCompatActivity {
    DatabazeZavodu db = new DatabazeZavodu(this);
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vysledky_databaze); //access to activity_main

        displayData();


    }

    public void zpet(View view) {
        onBackPressed();
    }
    public void displayData() { //zobrazí data z databáze do layoutu
        LinearLayout layout = findViewById(R.id.my_linear_layout);
        layout.setOrientation(LinearLayout.VERTICAL);
        db.displayAllData(layout);
    }

    
}
