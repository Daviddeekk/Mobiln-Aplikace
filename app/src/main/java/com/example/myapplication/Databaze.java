package com.example.myapplication;

import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class Databaze extends AppCompatActivity {
    MyDatabaseHelper dbHelper = new MyDatabaseHelper(this);


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_databaze); //access to activity_main
        displayData();


    }

    public void zpet(View view) {
        onBackPressed();

    }

    public void displayData() {
        LinearLayout layout = findViewById(R.id.my_linear_layout);
        layout.setOrientation(LinearLayout.VERTICAL);


        dbHelper.displayAllData(layout);
    }

    public void newData(View view) {
        /*LinearLayout layout = findViewById(R.id.my_linear_layout);
        dbHelper.addData("David Daněk");
        layout.setOrientation(LinearLayout.VERTICAL);

        setContentView(R.layout.activity_databaze);
        displayData();
        System.out.println("clicked");
        */

        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.selector);


        ImageButton zapsatButton = dialog.findViewById(R.id.zapsat);
        zapsatButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Dialog zapsatDialog = new Dialog(Databaze.this);
                zapsatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                zapsatDialog.setContentView(R.layout.zapsatjmeno);


                ImageButton cancel = zapsatDialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                    zapsatDialog.cancel();
                    }
                });
                EditText et = zapsatDialog.findViewById(R.id.jmeno);
                ImageButton zapsat = zapsatDialog.findViewById(R.id.zapsat);
                zapsat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String k = String.valueOf(et.getText());
                        dbHelper.addData(k);
                        zapsatDialog.cancel();
                        setContentView(R.layout.activity_databaze);
                        displayData();
                    }
                });

                zapsatDialog.show();
                dialog.cancel();
            }
        });


        dialog.show();
    }
}
