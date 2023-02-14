package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Databaze extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_databaze); //access to activity_main
        displayData();

    }
    public void zpet(View view){
        onBackPressed();

    }
    public void displayData(){
        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);


        LinearLayout layout = findViewById(R.id.my_linear_layout);
        layout.setOrientation(LinearLayout.VERTICAL);

        dbHelper.displayAllData(layout);
    }
}
