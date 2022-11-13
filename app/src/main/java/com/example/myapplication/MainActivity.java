package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //access to activity_main


    }
    public void poStisknuti (View view){
        //Button tlacitkoBeh = findViewById(R.id.beh);
        //tlacitkoBeh.setText("Běhání");
        setContentView(R.layout.hromadny);
    }
    public void zpet (View view)
    {
        setContentView(R.layout.activity_main);
    }
}