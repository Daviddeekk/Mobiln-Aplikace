package com.example.myapplication;

import static android.app.ProgressDialog.show;
import static android.view.View.INVISIBLE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
import android.app.UiModeManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Vysledky extends AppCompatActivity {

    private static String[][] array;
    private TableLayout table;
    private TableRow odstranRow;
    public boolean podminka = true;
    private TextView view1, view2, view3, view4;
    public static int pocetZavodniku;
    String[][] fullArray = new String[pocetZavodniku][4];
    private TextView[] poziceArray, zavodniciArray, cisloZavodnikaArray, casArray;
    private ImageButton imgb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vysledky);
        defineButtons();


    }
    public void defineButtons(){
        poziceArray = new TextView[pocetZavodniku];
        zavodniciArray = new TextView[pocetZavodniku];
        cisloZavodnikaArray = new TextView[pocetZavodniku];
        casArray = new TextView[pocetZavodniku];
        for (int i = 1; i<=pocetZavodniku; i++){
            int id1 = getResources().getIdentifier("pozice" + i, "id", getPackageName());
            int id2 = getResources().getIdentifier("jmeno" + i, "id", getPackageName());
            int id3 = getResources().getIdentifier("cislo" + i, "id", getPackageName());
            int id4 = getResources().getIdentifier("cas" + i, "id", getPackageName());

            cisloZavodnikaArray[i - 1] = findViewById(id3);
            zavodniciArray[i - 1] = findViewById(id2);
            poziceArray[i - 1] = findViewById(id1);
            casArray[i - 1] = findViewById(id4);

        }

        imgb = (ImageButton) findViewById(R.id.odejit);
        imgb.setVisibility(INVISIBLE);
        odstran();
        table = findViewById(R.id.table);
        sortByCasVz();
    }
    public void zpetNaHlavni(View view) {
        Intent intent = new Intent(this, Hlavni.class);
        startActivity(intent);
    }
    public int poc(int i){
        pocetZavodniku= i;
        return pocetZavodniku;
    }
    public static void finalArray(String[][] array) {
        Vysledky.array = array;
    }

    public void zpet(View view){
        onBackPressed();

    }
    public void odstran() {
        int k = pocetZavodniku;
        int kolikrat = 20-k;
        int smaz = 20;
        for (int i = 0; i < kolikrat; i++)
        {
            Resources res = getResources();
            int id = res.getIdentifier("row" + smaz, "id", getPackageName());
            odstranRow = (TableRow) findViewById(id);
            odstranRow.setVisibility(View.GONE);
            k = k + 1;
            smaz = smaz -1;
        }
    }
    public boolean isDarkMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }

    public void setData(){
        int k = 0;
        for(int i = 1; i<=pocetZavodniku;i++){

            TextView txv1 = zavodniciArray[i-1];
            TextView txv2 = casArray[i-1];
            TextView txv3 = cisloZavodnikaArray[i-1];
            TextView txv4 = poziceArray[i-1];

            txv1.setText(fullArray[k][0]);
            txv2.setText(fullArray[k][1]);
            txv3.setText(fullArray[k][2]);
            txv4.setText(fullArray[k][3]);

            txv1.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
            txv2.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
            txv3.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
            txv4.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
            k = k+1;
        }}
    //seřadí vzestupně podle času, defalutní, přidá pozici
    public void sortByCasVz() {
        int column = 1; // column to sort by
        for (int i = 0; i < array.length; i++) {
            for (int j = i+1; j < array.length; j++) {
                if (Integer.parseInt(array[i][column].replaceAll(" : ",""))
                        > Integer.parseInt(array[j][column].replaceAll(" : ",""))) {
                    String[] temp = array[i];
                    array[i] = array[j];
                    array[j] = temp;
                }}}
        for (int i = 0; i<pocetZavodniku; i++){
            fullArray[i][0] = array[i][0]; //jmeno
            fullArray[i][1] = array[i][1]; //cas
            fullArray[i][2] = array[i][2]; //cislo
            fullArray[i][3] = String.valueOf(i+1); //pozice
        }
        setData();
    } //seřadí podle času sestupně
    public void sortByCasS(){
        int column = 3; // column to sort by
        for (int i = 0; i < fullArray.length; i++) {
            for (int j = i+1; j < fullArray.length; j++) {
                if (Integer.parseInt(fullArray[i][column].replaceAll(" : ",""))
                        < Integer.parseInt(fullArray[j][column].replaceAll(" : ",""))) {
                    String[] temp = fullArray[i];
                    fullArray[i] = fullArray[j];
                    fullArray[j] = temp;
                }}}
        setData();
    }
    //seřadí podle čísla závodníka sestupně
    public void sortByCisloS() {
        for (int i = 0; i < fullArray.length; i++) {
            for (int j = 0; j < fullArray.length - 1; j++) {
                if (shouldSwapRowsReverse(fullArray[i], fullArray[j])) {
                    String[] temp = fullArray[i];
                    fullArray[i] = fullArray[j];
                    fullArray[j] = temp;
                }
            }
        }
        setData();
    }
    //seřadí podle čísla závodníka vzestupně
    public void sortByCisloVz(){
        for (int i = 0; i < fullArray.length; i++) {
            for (int j = 0; j < fullArray.length - 1; j++) {
                if (shouldSwapRows(fullArray[i], fullArray[j])) {
                    String[] temp = fullArray[i];
                    fullArray[i] = fullArray[j];
                    fullArray[j] = temp;
                }
            }
        }
        setData();
    }
    public void sortByNameAlph(){
        for (int i = 0; i < fullArray.length; i++) {
            for (int j = 0; j < fullArray.length - 1; j++) {
                if (fullArray[j][0].compareTo(fullArray[j + 1][0]) > 0) {
                    String[] temp = fullArray[j];
                    fullArray[j] = fullArray[j + 1];
                    fullArray[j + 1] = temp;}}}
        setData();
    }
    public void sortByNameRev(){
        for (int i = 0; i < fullArray.length; i++) {
            for (int j = 0; j < fullArray.length - 1; j++) {
                if (fullArray[j][0].compareTo(fullArray[j + 1][0]) < 0) {
                    String[] temp = fullArray[j];
                    fullArray[j] = fullArray[j + 1];
                    fullArray[j + 1] = temp;}}}
        setData();
    }

    public void sortingByCasAPozice(View view){
        if(podminka == true){
            sortByCasS();
            podminka = false;
        }
        else{
            sortByCasVz();
            podminka = true;
        }
    }
    public void sortingByCislo(View view){
        if(podminka == true){
            sortByCisloS();
            podminka = false;
        }
        else{
            sortByCisloVz();
            podminka = true;
        }
    }
    public void sortingByName(View view){
        if(podminka == true){
            sortByNameAlph();
            podminka = false;
        }
        else{
            sortByNameRev();
            podminka = true;
        }
    }
    private boolean shouldSwapRows(String[] row1, String[] row2) {
        if (isEmptyRow(row1) && isEmptyRow(row2)) {
            return false;
        } else if (isEmptyRow(row1)) {
            return false;
        } else if (isEmptyRow(row2)) {
            return true;
        } else {
            return Integer.parseInt(row1[2]) < Integer.parseInt(row2[2]);
        }
    }
    private boolean shouldSwapRowsReverse(String[] row1, String[] row2) {
        if (isEmptyRow(row1) && isEmptyRow(row2)) {
            return false;
        } else if (isEmptyRow(row1)) {
            return false;
        } else if (isEmptyRow(row2)) {
            return true;
        } else {
            return Integer.parseInt(row1[2]) > Integer.parseInt(row2[2]);
        }
    }
    private boolean isEmptyRow(String[] row) {
        return row == null || row.length < 3 || TextUtils.isEmpty(row[2]);
    }
    public void share(View view){

        showDialog();
    }
    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.export);

        TableRow t1 = dialog.findViewById(R.id.row1);
        TableRow t2 = dialog.findViewById(R.id.row2);

        ImageView imgv = dialog.findViewById(R.id.imageView2);
        imgv.setColorFilter(isDarkMode(this) ? Color.WHITE : Color.GRAY);

        t1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                try {
                    exportToCSV(fullArray, v.getContext());
                    dialog.dismiss();
                    SnackBar();
                    imgb.setVisibility(View.VISIBLE);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }});

        t2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Dialog zapsatDialog = new Dialog(Vysledky.this);
                zapsatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                zapsatDialog.setContentView(R.layout.zapsatjmeno);

                TextView txtv = zapsatDialog.findViewById(R.id.textView);
                txtv.setText("Pojmenujte výsledek");
                EditText et1 = zapsatDialog.findViewById(R.id.jmeno);
                et1.setHint("Běh na 100m");

                ImageButton cancel = zapsatDialog.findViewById(R.id.cancel);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        zapsatDialog.cancel();
                    }
                });
                    ImageButton zapsat = zapsatDialog.findViewById(R.id.zapsat);
                    zapsat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatabazeVysledkuZavodu rdb = new DatabazeVysledkuZavodu(v.getContext());
                        SQLiteDatabase db1 = rdb.getWritableDatabase();
                        DatabazeZavodu dtbz = new DatabazeZavodu(v.getContext());
                        SQLiteDatabase db2 = dtbz.getWritableDatabase();
                        ContentValues values1 = new ContentValues();

                        Cursor cursor = db2.rawQuery("SELECT MAX(id) FROM race", null);
                        int lastId = cursor.moveToFirst() ? cursor.getInt(0) : 0;
                        cursor.close();
                        String raceId = Integer.toString(lastId + 1);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy HH:mm");
                        String currentDateTime = sdf.format(new Date());

                        values1.put("time", currentDateTime);
                        values1.put("id", raceId);
                        values1.put("name", String.valueOf(et1.getText()));
                        db2.insert("race", null, values1);
                        db2.close();

                        for (int i = 0; i < fullArray.length; i++) {
                            ContentValues values2 = new ContentValues();
                            values2.put("racer_name", fullArray[i][0]);
                            values2.put("racer_time", fullArray[i][1]);
                            values2.put("racer_number", fullArray[i][2]);
                            values2.put("position",fullArray[i][3]);
                            values2.put("race_id", raceId);
                            db1.insert("racer", null, values2);
                            zapsatDialog.dismiss();
                        }
                        db1.close();
                        dialog.cancel();
                        imgb.setVisibility(View.VISIBLE);
                    }
                });
                zapsatDialog.show();
            }

        });

        dialog.show();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }
    public static void exportToCSV(String[][] fullArray, Context context) throws IOException {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "data_" + timeStamp + ".csv";
        File file = new File(dir, fileName);
        String filePath = file.getAbsolutePath();
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

        for (String[] row : fullArray) {
            bufferedWriter.write(String.join(";", row));
            bufferedWriter.newLine();
        }

        bufferedWriter.close();
        outputStreamWriter.close();
        fileOutputStream.close();
    }
    public void SnackBar(){
        RelativeLayout constraintLayout = findViewById(R.id.layoutR);
        Snackbar.make(constraintLayout, "Uloženo do složky Documents", Snackbar.LENGTH_LONG)
                .setDuration(2000)
                .show();
    }

}



