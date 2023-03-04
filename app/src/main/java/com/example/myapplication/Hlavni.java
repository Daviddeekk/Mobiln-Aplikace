package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;

import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
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
    private SeekBar mSeekBar;
    private TextView mSelectedValueTextView;
    private RadioButton b1, b2, c1, c2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hlavni);
        defineButtons();
        DatabazeZavodu z = new DatabazeZavodu(this);

    }
    public boolean isDarkMode(Context context) { //vrátí boolean jestli je dark mode
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
    //definuje barvu imageButtons
    public void defineButtons(){
        datButton = findViewById(R.id.dtbs);
        sportView = findViewById(R.id.zvolteSport);

        b1 = findViewById(R.id.formatButton1);
        b2 = findViewById(R.id.formatButton2);
        c1 = findViewById(R.id.radioButton1);
        c2 = findViewById(R.id.radioButton2);

        mSeekBar = findViewById(R.id.seekBar);
        mSelectedValueTextView = findViewById(R.id.selectedValueTextView);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the selected value TextView
                int value = progress + 1;
                mSelectedValueTextView.setText(String.valueOf(value));

                // Show the selected value in a tooltip
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    mSeekBar.setTooltipText(String.valueOf(value));
                } else {
                    TooltipCompat.setTooltipText(mSeekBar, String.valueOf(value));
                }
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
                if(b1.isChecked() && c1.isChecked()){
                    Hromadny m = new Hromadny();
                    m.getNumber(Integer.parseInt(mSelectedValueTextView.getText().toString()));

                    m.getTag("hromadny");
                    hromadny();
                }
                else if(b1.isChecked() && c2.isChecked()){
                    Hromadny m = new Hromadny();
                    m.getNumber(Integer.parseInt(mSelectedValueTextView.getText().toString()));

                    m.getTag("rychlyHromadny");
                    hromadny();

                }
                else if(b2.isChecked() && c1.isChecked()){
                    Prubezny p = new Prubezny();
                    p.getTag("prubezny");
                    p.getNumber(Integer.parseInt(mSelectedValueTextView.getText().toString()));

                    prubezny();
                    System.out.println("prubezny");
                }
                else if(b2.isChecked() && c2.isChecked()){
                    Prubezny p = new Prubezny();
                    p.getTag("rychlyPrubezny");
                    p.getNumber(Integer.parseInt(mSelectedValueTextView.getText().toString()));
                    prubezny();

                }
            }
        });

    }
    //otevře class database
    public void openDatabase(View view){
        Intent intent = new Intent(this, NacteniZavodnikuDoDatabaze.class);
        startActivity(intent);
    }
    @Override //pokud je stisknuto back button ukončí se aplikace
    public void onBackPressed() {
        this.finishAffinity();
    }
    //metoda která otevře hromadny class
    public void hromadny() {
        Intent intent = new Intent(this, Hromadny.class);
        startActivity(intent);
    }
    //metoda která otevře prubezny class
    public void prubezny(){
        Intent intent = new Intent(this, Prubezny.class);
        startActivity(intent);
    }
    public void vysledky(View view){
        Intent intent = new Intent(this, VysledkyDatabaze.class);
        startActivity(intent);
    }
    //metoda která spustí dialog, s následujícími parametry

}