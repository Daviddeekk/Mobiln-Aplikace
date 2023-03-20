package com.example.myapplication;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TableRow;
import android.widget.TextView;

public class Editace extends AppCompatActivity {
    private TextView view1, view2, view3;
    private static String  w,casTag;
    public static int pocetZavodniku;
    private Button button;
    private TableRow odstranRow;
    private ImageButton imgb, zpet, dalsi;
    public String sec, min, hod;
    private static String[][] array;
    public  int radek, dulezite, time, row;
    String[][] finalArray = new String[pocetZavodniku][3];
    private EditText[] cisloZavodnikaArray, zavodniciArray;
    private Button[] timeButtonArray;
    private ImageButton[] lockButtonArray;


    public void onBackPressed() {
        {goBackDialog();}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editace);
        defineButtons();



    }
    public void defineButtons(){
        timeButtonArray = new Button[pocetZavodniku];
        zavodniciArray = new EditText[pocetZavodniku];
        cisloZavodnikaArray = new EditText[pocetZavodniku];
        lockButtonArray = new ImageButton[pocetZavodniku];
        for (int i = 1; i<=pocetZavodniku; i++){
            int id1 = getResources().getIdentifier("cislo" + (i), "id", getPackageName());
            int id2 = getResources().getIdentifier("jmeno" + i, "id", getPackageName());
            int id3 = getResources().getIdentifier("cas" + i, "id", getPackageName());
            int id4 = getResources().getIdentifier("lock" + i, "id", getPackageName());

            timeButtonArray[i - 1] = findViewById(id3);
            zavodniciArray[i - 1] = findViewById(id2);
            cisloZavodnikaArray[i - 1] = findViewById(id1);
            lockButtonArray[i - 1] = findViewById(id4);

            viewsEditable(false, i);
        }
        buttonsColor();
        odstran();
        setData();

        dalsi = (ImageButton) findViewById(R.id.next);
        dalsi.setColorFilter(Color.rgb(7, 255, 148));

        zpet = (ImageButton) findViewById(R.id.zpet);
        zpet.setColorFilter(Color.rgb(255,7,107));
        textColor();
    }

    public int poc(int i){
        pocetZavodniku= i;
        return pocetZavodniku;
    }
    public void back(View view){
        goBackDialog();
    }
    public void dalsiStranka(View view){
        finalArray();
        Intent intent = new Intent(this, Vysledky.class);
        startActivity(intent);

    }
    public String getTag(String gtag){
        casTag = gtag;
        return casTag;
    }
    public boolean isDarkMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }

    public void textColor(){
            for (int i = 1; i <= pocetZavodniku; i++) {
                EditText et1 = zavodniciArray[i-1];
                EditText et2 = cisloZavodnikaArray[i-1];
                Button bcas = timeButtonArray[i-1];

                et1.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
                et2.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
                bcas.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
            }
    }
    public void goBackDialog(){
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyAlertDialogTheme));
        resetAlert.setMessage("Opravdu chcete odejít, prijdete o všechna naměřená data");
        resetAlert.setTitle("Odejít?");
        resetAlert.setPositiveButton("Ano", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                openActivity2();
            }
        });
        resetAlert.setNeutralButton("Ne", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {}
        });
        AlertDialog dialog = resetAlert.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.white));
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(this, R.color.white));
    }
    public void openActivity2() {
        Intent intent = new Intent(this, Hlavni.class);
        startActivity(intent);
    }
    public static void setArray(String[][] array) {
        Editace.array = array;
    }
    public void setData(){
        int k = 0;
        for(int i = 1; i<=pocetZavodniku;i++){
            EditText et1 = zavodniciArray[i-1];
            et1.setText(array[k][0]);

            Button bcas = timeButtonArray[i-1];
            bcas.setText(array[k][1]);

            EditText et2 = cisloZavodnikaArray[i-1];
            et2.setText(array[k][2]);

            k = k+1;
    }}

    public void zavodniciEditable(View view) {
        for (int i = 1; i<=pocetZavodniku; i++) {
            ImageButton lck = lockButtonArray[i-1];
            if(view.getId()==lck.getId()){
                if (lck.isActivated()) {
                    viewsEditable(false, i);
                    lck.setColorFilter(Color.rgb(255, 7, 107));
                    lck.setActivated(false);
                } else {
                    viewsEditable(true, i);
                    lck.setColorFilter(Color.rgb(7, 255, 148));
                    lck.setActivated(true);
                }}
    }}
    public void viewsEditable(boolean edit, int i){
        EditText et1 = zavodniciArray[i-1];
        EditText et2 = cisloZavodnikaArray[i-1];
        Button bcas = timeButtonArray[i-1];

        et1.setEnabled(edit);
        et2.setEnabled(edit);
        bcas.setEnabled(edit);
    }

    public void buttonsColor(){ //při spuštění se nastaví barva tlačítka
        for (int i = 1; i <= pocetZavodniku; i++){
            ImageButton lck = lockButtonArray[i-1];
            lck.setColorFilter(Color.rgb(255,7,107));
        }
    }

    public void openPicker(View view) {
        picker(view.getId());
    }
    public void picker(int cisloRadku){
        for (int i = 1; i<=pocetZavodniku; i++) {                                                           //loop s podmínkou hledá číslo řádku a z něj dostává Čas
            Button bcas = timeButtonArray[i-1];
            if(cisloRadku == bcas.getId()){
                String cas = bcas.getText().toString();
                w = cas.replaceAll(" : ","");
                time = Integer.parseInt(w);
                row = i;}}
        int k = time;
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
        if (casTag.equals("rychlyHromadny") || casTag.equals("rychlyPrubezny")){
            picker3.setMaxValue(99);}
        else{
            picker3.setMaxValue(59);}
        for(int i = 1; i<=3; i++){  // v numberPicker se rozdělí čísla podle dat

            if(i == 1) {
                picker3.setValue(k % 100); //sekundy
                k = k/100;
            }
            if(i == 2){
                picker2.setValue(k%100); //minuty
                k = k/100;
            }
            if(i == 3){
                picker.setValue(k); //hodiny
            }}
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            picker3.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
            picker.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
            picker2.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
        }
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        if(String.valueOf(picker3.getValue()).length() == 1){
                            sec = "0"+picker3.getValue();}
                        else {sec = String.valueOf(picker3.getValue());}

                        if(String.valueOf(picker2.getValue()).length() == 1){
                            min = "0"+picker2.getValue();}
                        else { min = String.valueOf(picker2.getValue());}

                        if(String.valueOf(picker.getValue()).length() == 1){
                            hod = "0"+picker.getValue();}
                        else { hod= String.valueOf(picker.getValue());}
                        //pokud bude číslo jednociferné přídá se před jednotku 0
                        Resources res = getResources();
                        int idk = res.getIdentifier("cas" + row, "id", getPackageName());
                        view1 = findViewById(idk);
                        String cas = hod +" : "+ min+" : "+sec;
                        view1.setText(cas);
                        w = cas;
                    }})

                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //negative button action
                    }
                });
        builder.create().show();
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
    public void finalArray(){
        int row = 0;
        for (int i = 1; i <=pocetZavodniku; i++){
            EditText et1 = zavodniciArray[i-1];
            EditText et2 = cisloZavodnikaArray[i-1];
            Button bcas = timeButtonArray[i-1];

           finalArray[row][0] = et1.getText().toString();
           finalArray[row][1] = bcas.getText().toString();
           finalArray[row][2] = et2.getText().toString();
           row = row +1;
        }
        Vysledky v = new Vysledky();
        v.poc(pocetZavodniku);
        v.finalArray(finalArray);
    }

}