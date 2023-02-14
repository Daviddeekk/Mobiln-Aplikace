package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class Hlavni extends AppCompatActivity {
    private ImageButton datButton;
    private Button button;
    private Button prubezny;
    boolean length;
    boolean back = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hlavni); //access to activity_main
        defineButton();

    }

    public void defineButton(){
        datButton = findViewById(R.id.dtbs);

        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        boolean isDarkMode = nightModeFlags == Configuration.UI_MODE_NIGHT_YES;

// Set the tint color based on the theme mode
        if (isDarkMode) {
            datButton.setColorFilter(ContextCompat.getColor(this, R.color.black));
        } else {
            datButton.setColorFilter(ContextCompat.getColor(this, R.color.white));
        }

    }
    public void openDatabase(View view){
        Intent intent = new Intent(this, Databaze.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    public void openActivity2() {
        Intent intent = new Intent(this, Hromadny.class);
        startActivity(intent);

    }

    public void openActivity3(){
        Intent intent = new Intent(this, Prubezny.class);
        startActivity(intent);
    }
    public void dialogButton(View view){
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(2);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Zadejte počet závodníků (1-20)");
        final EditText input = new EditText(this);
        input.setFilters(FilterArray);
        input.setGravity(Gravity.CENTER_HORIZONTAL);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);

        ArrayList<String> dataList = dbHelper.getAllData();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataList);


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(input.length() == 0){
                    dialogButton(view);
                    }else{
                    int cislo = Integer.parseInt(input.getText().toString());
                        if(cislo <= 20 && cislo > 0
                             ){
                            Button button = (Button) view;
                                String tag = view.getTag().toString();

                                if(tag.equals("hromadny")||tag.equals("rychlyHromadny")){

                                    Hromadny m = new Hromadny();
                                    m.getNumber(cislo);
                                    m.getSport(button.getText().toString());
                                    m.getTag(tag);

                                   // dbHelper.addData("David Daněk");

                                    openActivity2();


                                }
                                else if (tag.equals("prubezny")||tag.equals("rychlyPrubezny")){
                                    //MainActivity3 n = new MainActivity3();
                                    Prubezny p = new Prubezny();
                                    p.getTag(tag);
                                    p.getNumber(cislo);
                                    p.getSport(button.getText().toString());
                                    openActivity3();
                                }
                            }

                else{
                dialogButton(view);
                }
                }
            }
        });

        builder.setNegativeButton("Zpět", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



}