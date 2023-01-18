package com.example.myapplication;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vysledky);
        odstran();
        table = findViewById(R.id.table);
        sortByCasVz();
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
    public void setData(){

        Resources res = getResources();
        int k = 0;
        for(int i = 1; i<=pocetZavodniku;i++){

            int id = res.getIdentifier("jmeno"+i, "id", getPackageName());
            view1 = (TextView) findViewById(id);
            view1.setText(fullArray[k][0]);

            int id2 = res.getIdentifier("cas"+i, "id", getPackageName());
            view2 = (TextView) findViewById(id2);
            view2.setText(fullArray[k][1]);

            int id3 = res.getIdentifier("cislo"+i, "id", getPackageName());
            view3 = (TextView) findViewById(id3);
            view3.setText(fullArray[k][2]);

            int id4 = res.getIdentifier("pozice"+i, "id", getPackageName());
            view4 = (TextView) findViewById(id4);
            view4.setText(fullArray[k][3]);

            k = k+1;
        }}
    //seřadí vzestupně podle času, defalutní, přidá pozici
    public void sortByCasVz() {
        int column = 1; // column to sort by
        for (int i = 0; i < array.length; i++) {
            for (int j = i+1; j < array.length; j++) {
                if (Integer.parseInt(array[i][column].replaceAll(" : ","")) > Integer.parseInt(array[j][column].replaceAll(" : ",""))) {

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
                if (Integer.parseInt(fullArray[i][column].replaceAll(" : ","")) < Integer.parseInt(fullArray[j][column].replaceAll(" : ",""))) {
// swap values
                    String[] temp = fullArray[i];
                    fullArray[i] = fullArray[j];
                    fullArray[j] = temp;
                }}}
        setData();
    }
    //seřadí podle čísla závodníka sestupně
    public void sortByCisloS(){
        for (int i = 0; i < fullArray.length; i++) {
            for (int j = 0; j < fullArray.length - 1; j++) {
                if (fullArray[j][2].compareTo(fullArray[j + 1][2]) > 0) {
                    String[] temp = fullArray[j];
                    fullArray[j] = fullArray[j + 1];
                    fullArray[j + 1] = temp;}}}
        setData();

    }

    //seřadí podle čísla závodníka vzestupně
    public void sortByCisloVz(){
        for (int i = 0; i < fullArray.length; i++) {
            for (int j = 0; j < fullArray.length - 1; j++) {
                if (fullArray[j][2].compareTo(fullArray[j + 1][2]) < 0) {
                    String[] temp = fullArray[j];
                    fullArray[j] = fullArray[j + 1];
                    fullArray[j + 1] = temp;}}}
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

    public void share(View view){

        showDialog();
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.export);

        TableRow t1 = dialog.findViewById(R.id.row1);
        TableRow t2 = dialog.findViewById(R.id.row2);
        t2.setEnabled(false);
        t2.setBackgroundColor(Color.rgb(50,50,50));

       ImageView imgv = dialog.findViewById(R.id.imageView2);
       imgv.setColorFilter(Color.rgb(100,100,100));


        t1.setOnClickListener(new View.OnClickListener(){


            @Override
            public void onClick(View v) {

                try {
                    exportToCSV(fullArray, v.getContext());
                    dialog.dismiss();
                    SnackBar();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        t2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                System.out.println("clicked");
            }
        });

        dialog.show();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    public static void exportToCSV(String[][] fullArray, Context context) throws IOException {
        // get the file path for the app's private internal storage directory
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);


        // create a unique file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String fileName = "data_" + timeStamp + ".txt";
        File file = new File(dir, fileName);
        // get the file path as a string
        String filePath = file.getAbsolutePath();

        // open the file for writing
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
        BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
// write each row to the file
        for (String[] row : fullArray) {
            // write the values, separated by commas
            bufferedWriter.write(String.join(",", row));
            // add a newline character
            bufferedWriter.newLine();
        }

        // close the BufferedWriter and OutputStreamWriter
        bufferedWriter.close();
        outputStreamWriter.close();
        fileOutputStream.close();
    }
    public void SnackBar(){
        ConstraintLayout constraintLayout = findViewById(R.id.layoutR);

        Snackbar.make(constraintLayout, "Uloženo do složky Documents", Snackbar.LENGTH_LONG)
                .setDuration(5000)
                .show();
    }



}



