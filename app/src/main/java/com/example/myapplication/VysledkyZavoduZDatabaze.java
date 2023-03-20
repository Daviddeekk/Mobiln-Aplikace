package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;


public class VysledkyZavoduZDatabaze extends AppCompatActivity {
    DatabazeVysledkuZavodu db = new DatabazeVysledkuZavodu(this);
    private int id;
    private TextView pozice, jmeno, cislo, cas;

    boolean desc = true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dtbz_vysledky_zavodu);
        cas = findViewById(R.id.nadpisCas);
        pozice = findViewById(R.id.nadpisPozice);
        jmeno = findViewById(R.id.nadpisJmeno);
        cislo = findViewById(R.id.nadpisCislo);

        id = getIntent().getIntExtra("id", -1);
        if (id != -1) {
            displayData();

        } else {
            Toast.makeText(this, "Parameter 'id' not found", Toast.LENGTH_SHORT).show();
            finish();
        }

       pozice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = findViewById(R.id.my_linear_layout);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.removeAllViews();
                if(desc){
                    db.displayDataByForeignId(layout, id, "position", "DESC");
                    desc = false;
                }
                else{
                    db.displayDataByForeignId(layout, id, "position", "ASC");
                    desc = true;
                }}
        });

        cislo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = findViewById(R.id.my_linear_layout);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.removeAllViews();

                if(desc){
                    db.displayDataByForeignId(layout, id, "racer_number", "DESC");
                    desc = false;
                }
                else{
                    db.displayDataByForeignId(layout, id, "racer_number", "ASC");
                    desc = true;
                }
            }
        });
       jmeno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = findViewById(R.id.my_linear_layout);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.removeAllViews();

                if(desc){
                    db.displayDataByForeignId(layout, id, "racer_name", "DESC");
                    desc = false;
                }
                else{
                    db.displayDataByForeignId(layout, id, "racer_name", "ASC");
                    desc = true;
                }
            }
        });
        cas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout layout = findViewById(R.id.my_linear_layout);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.removeAllViews();

                if(desc){
                    db.displayDataByForeignId(layout, id, "racer_time", "DESC");
                    desc = false;
                }
                else{
                    db.displayDataByForeignId(layout, id, "racer_time", "ASC");
                    desc = true;
                }
            }
        });

    }
    public void displayData() { //
        LinearLayout layout = findViewById(R.id.my_linear_layout);
        layout.setOrientation(LinearLayout.VERTICAL);
        db.displayDataByForeignId(layout, id, "position", "ASC");
    }
    public void zpet(View view){
        onBackPressed();
    }
    public void share(View view){

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.export);

        TableRow t1 = dialog.findViewById(R.id.row1);
        TableRow t2 = dialog.findViewById(R.id.row2);
        t2.setVisibility(View.GONE);
        t1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                db.exportDataByForeignId(id,"position", "ASC", view.getContext() );
                SnackBar();
                dialog.dismiss();

            }});
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.show();


    }
    public void SnackBar(){
        CoordinatorLayout cd = findViewById(R.id.zavodniciVysledky);

        Snackbar.make(cd, "Uloženo do složky Documents", Snackbar.LENGTH_LONG)
                .setDuration(2000)
                .show();
    }

}