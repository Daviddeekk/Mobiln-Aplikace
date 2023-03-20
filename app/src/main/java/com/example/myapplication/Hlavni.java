package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class Hlavni extends AppCompatActivity {
    private ImageButton datButton, continu;
    private TextView sportView;
    private SeekBar slider;
    private TextView value, zavodnici, vysledky;
    private RadioButton formatRB1, formatRB2,casRB1, casRB2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hlavni);
        defineButtons();
    }
    //definuje barvu imageButtons
    private void defineButtons(){
        datButton = findViewById(R.id.dtbs);
        sportView = findViewById(R.id.zvolteSport);

        formatRB1 = findViewById(R.id.formatButton1);
        formatRB2 = findViewById(R.id.formatButton2);

        casRB1 = findViewById(R.id.radioButton1);
        casRB2 = findViewById(R.id.radioButton2);

        zavodnici = (TextView) findViewById(R.id.databaze_zavodnici);
        zavodnici.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);

        vysledky = (TextView) findViewById(R.id.databaze_zavody);
        vysledky.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);

        slider = findViewById(R.id.seekBar);
        value = findViewById(R.id.selectedValueTextView);
        slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the selected value TextView
                int hodnota = progress + 1;
                value.setText(String.valueOf(hodnota));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        continu = findViewById(R.id.continueButton);
        continu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formatRB1.isChecked() &&  casRB1.isChecked()){
                    Hromadny m = new Hromadny();
                    m.getNumber(Integer.parseInt(value.getText().toString()));
                    m.getTag("hromadny");
                    startHromadny();
                }
                else if(formatRB1.isChecked() &&  casRB2.isChecked()){
                    Hromadny m = new Hromadny();
                    m.getNumber(Integer.parseInt(value.getText().toString()));
                    m.getTag("rychlyHromadny");
                    startHromadny();
                }
                else if(formatRB2.isChecked() &&  casRB1.isChecked()){
                    Prubezny p = new Prubezny();
                    p.getTag("prubezny");
                    p.getNumber(Integer.parseInt(value.getText().toString()));

                    startPrubezny();
                    System.out.println("prubezny");
                }
                else if(formatRB2.isChecked() &&  casRB2.isChecked()){
                    Prubezny p = new Prubezny();
                    p.getTag("rychlyPrubezny");
                    p.getNumber(Integer.parseInt(value.getText().toString()));
                    startPrubezny();
                }}});
    }
    public boolean isDarkMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
    public void onBackPressed() {
        this.finishAffinity();
    }
    public void startHromadny() {
        Intent intent = new Intent(this, Hromadny.class);
        startActivity(intent);
    }
    public void startPrubezny(){
        Intent intent = new Intent(this, Prubezny.class);
        startActivity(intent);
    }
    public void vysledky(View view){
        Intent intent = new Intent(this, VysledkyDatabaze.class);
        startActivity(intent);
    }
    public void zavodnici(View view){
        Intent intent = new Intent(this, NacteniZavodnikuDoDatabaze.class);
        startActivity(intent);
    }

}