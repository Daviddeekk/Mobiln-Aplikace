package com.example.myapplication;

import static android.app.PendingIntent.getActivity;
import static android.text.format.DateFormat.getTimeFormat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.content.res.Resources;

import java.util.Date;


public class Editace extends AppCompatActivity {
    private TextView view1;
    private TextView view2;
    private static String u;
    private static String w;
    private Button button;
    private ImageButton imgb;
    boolean active = true;
    public String sec;
    public String min;
    public String hod;
    private DatePickerDialog datePickerDialog;
    //TextView jmeno;
    //static String getName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editace);
        setName(u);
        setCas(w);
        viewsEditable(false);
        buttonEditable(true);

        button = (Button) findViewById(R.id.zpet);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
    }
    public void openActivity2() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
    public String getName(String name){

        u = name;
        return u;

    }
    public String getCas(String cas){
        w = cas;
        return w;
    }
    public void setName(String name){
        view1 = findViewById(R.id.jmeno1);
        view1.setText(name);
    }
    public void setCas(String cas){
        view1 = findViewById(R.id.cas1);
        view1.setText(cas);
    }
    public void zavodniciEditable(View view) {
       // button = (Button) findViewById(R.id.zavodnici);
        //button.setEnabled(edit);
       // int viewCount = 20;

        imgb = (ImageButton) findViewById(R.id.lock1);

        Resources res = getResources();

        int id = res.getIdentifier("jmeno1", "id", getPackageName());
        view1 = (TextView) findViewById(id);


        int ids = res.getIdentifier("cas1", "id", getPackageName());
        view2 = (TextView) findViewById(ids);
        if(active == true){
            view1.setEnabled(true);
            view2.setEnabled(true);
            active = false;
            imgb.setColorFilter(Color.rgb(7,255,148));
        }
        else {
            view1.setEnabled(false);
            view2.setEnabled(false);
            active = true;
            imgb.setColorFilter(Color.rgb(255,7,107));
        }
    }
    public void buttonEditable(boolean edit){
        imgb = (ImageButton) findViewById(R.id.lock1);
        imgb.setEnabled(edit);
        imgb.setColorFilter(Color.rgb(255,7,107));
    }
    public void viewsEditable(boolean edit){
        Resources res = getResources();
        int id = res.getIdentifier("jmeno1", "id", getPackageName());
        view1 = (TextView) findViewById(id);
        view1.setEnabled(edit);
        int ids = res.getIdentifier("cas1", "id", getPackageName());
        view2 = (TextView) findViewById(ids);
        view2.setEnabled(edit);
       // view2.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "12")});
    }
    public void openPicker(View view) {
        picker();
    }
    public void picker(){
        w = w.replaceAll(" : ","");
        int k = Integer.parseInt(w);



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = this.getLayoutInflater().inflate(R.layout.activity_picker, null);
        builder.setView(view);

        NumberPicker picker = (NumberPicker) view.findViewById(R.id.picker);
        picker.setMinValue(0);
        picker.setMaxValue(59);

        NumberPicker picker2 = (NumberPicker) view.findViewById(R.id.picker2);
        picker2.setMinValue(0);
        picker2.setMaxValue(59);

        NumberPicker picker3 = (NumberPicker) view.findViewById(R.id.picker3);
        picker3.setMinValue(0);
        picker3.setMaxValue(59);
        TextView t = (TextView) view.findViewById(R.id.hodiny);
        TextView t2 = (TextView) view.findViewById(R.id.minuty);
        TextView t3 = (TextView) view.findViewById(R.id.vteriny);

        for(int i = 1; i<=3; i++){

            if(i == 1) {
                picker3.setValue(k % 100);
                k = k/100;
            }
            if(i == 2){
                picker2.setValue(k%100);
                k = k/100;
            }
            if(i == 3){
                picker.setValue(k);
            }
        }

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if(String.valueOf(picker3.getValue()).length() == 1){
                            sec = "0"+picker3.getValue();} else {sec = String.valueOf(picker3.getValue());}
                        if(String.valueOf(picker2.getValue()).length() == 1){
                            min = "0"+picker2.getValue();} else { min = String.valueOf(picker2.getValue());}
                        if(String.valueOf(picker.getValue()).length() == 1){
                            hod = "0"+picker.getValue();} else { hod= String.valueOf(picker.getValue());}
                        //pokud bude číslo jednociferné přídá se před jednotku 0


                        view1 = findViewById(R.id.cas1);

                        String cas = hod +" : "+ min+" : "+sec;
                        view1.setText(cas);
                        w = cas;
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //negative button action
                    }
                });
        builder.create().show();

    }
}