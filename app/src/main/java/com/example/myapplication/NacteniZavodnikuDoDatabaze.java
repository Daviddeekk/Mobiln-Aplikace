package com.example.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NacteniZavodnikuDoDatabaze extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    DatabazeZavodniku dbHelper = new DatabazeZavodniku(this);
    private boolean isToastDisplayed = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_databaze); //access to activity_main
        displayData();
    }

    public void zpet(View view) {
        onBackPressed();
    }
    public void displayData() { //
        LinearLayout layout = findViewById(R.id.my_linear_layout);
        layout.setOrientation(LinearLayout.VERTICAL);
        dbHelper.displayAllData(layout);
    }
    public void newData(View view) { //
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.load);
        ImageButton nacteniZCsv = dialog.findViewById(R.id.importZCsv);
        ImageButton zapsatButton = dialog.findViewById(R.id.zapsat);
        zapsatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog zapsatDialog = new Dialog(NacteniZavodnikuDoDatabaze.this);
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
        nacteniZCsv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                launchFilePicker();
                dialog.cancel();
                setContentView(R.layout.activity_databaze);
            }
        });
        dialog.show();
    }
    public void launchFilePicker() {
        // Create an intent for selecting a file via the file manager
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        // If the selection worked, we have a URI pointing to the file
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get the URI of the selected file
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    List<String[]> data = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        data.add(line.split(";"));
                    }
                    String[][] arrayData = data.toArray(new String[data.size()][]);

                    int dataLength = arrayData.length;
                    Resources res = getResources();
                    for (int i = 1; i <= dataLength; i++) {

                        System.out.println(arrayData[0][0]);
                        dbHelper.addData(arrayData[i - 1][0]);
                        setContentView(R.layout.activity_databaze);
                        displayData();
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            setContentView(R.layout.activity_databaze);
            displayData();
        }
    }

}
