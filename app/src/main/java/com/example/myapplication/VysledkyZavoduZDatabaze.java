package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class VysledkyZavoduZDatabaze extends AppCompatActivity {
    DatabazeVysledkuZavodu db = new DatabazeVysledkuZavodu(this);
    private int id;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtbz_vysledky_zavodu);

        // Retrieve the id parameter from the Intent
        id = getIntent().getIntExtra("id", -1);

        // Check if the id parameter was successfully retrieved
        if (id != -1) {

            displayData();

        } else {
            // Handle the case where the id parameter was not found
            Toast.makeText(this, "Parameter 'id' not found", Toast.LENGTH_SHORT).show();
            finish();
        }


    }
    public void displayData() { //
        LinearLayout layout = findViewById(R.id.my_linear_layout);
        layout.setOrientation(LinearLayout.VERTICAL);
        System.out.println(id);
        db.displayDataByForeignId(layout, id);
    }
    public void zpet(View view){
        onBackPressed();
    }


}