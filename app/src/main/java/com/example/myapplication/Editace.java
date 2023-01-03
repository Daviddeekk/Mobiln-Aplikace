package com.example.myapplication;

import static android.app.PendingIntent.getActivity;
import static android.text.format.DateFormat.getTimeFormat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.res.Resources;

import java.util.Date;



public class Editace extends AppCompatActivity {
    private TextView view1, view2, view3;
    private static String u, w, cislo;
    public static int pocetZavodniku;
    private Button button;
    private ImageButton button2;
    private TableRow odstranRow;
    private ImageButton imgb, zpet, dalsi;
    public String sec, min, hod;
    private static String[][] array;
    public  int radek, dulezite;
    String[][] finalArray = new String[pocetZavodniku][3];


   

    //TextView jmeno;
    //static String getName;
    public void onBackPressed() {
        {openActivity2();}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editace);
        buttonsColor();
        odstran();
        setData();
        for (int k = 1; k<=pocetZavodniku; k++){
        viewsEditable(false, k);
        }
        dalsi = (ImageButton) findViewById(R.id.next);
        dalsi.setColorFilter(Color.rgb(7, 255, 148));

        zpet = (ImageButton) findViewById(R.id.zpet);
        zpet.setColorFilter(Color.rgb(255,7,107));

    }
    public static void setArray(String[][] array) {
        Editace.array = array;

    }
    public int poc(int i){
        pocetZavodniku= i;
        return pocetZavodniku;
    }
    public void back(View view){
        openActivity2();
    }
    public void dalsiStranka(View view){
        finalArray();

        Intent intent = new Intent(this, Vysledky.class);
        startActivity(intent);

    }


    public void openActivity2() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public void setData(){

        Resources res = getResources();
        int k = 0;
        for(int i = 1; i<=pocetZavodniku;i++){

            int id = res.getIdentifier("jmeno"+i, "id", getPackageName());
            view1 = (TextView) findViewById(id);
            view1.setText(array[k][0]);

            int id2 = res.getIdentifier("cas"+i, "id", getPackageName());
            view2 = (Button) findViewById(id2);
            view2.setText(array[k][1]);

            int id3 = res.getIdentifier("cislo"+i, "id", getPackageName());
            view3 = (EditText) findViewById(id3);
            view3.setText(array[k][2]);

            k = k+1;
    }}

    public void zavodniciEditable(View view) {

        for (int i = 1; i<=pocetZavodniku; i++) {

            Resources res = getResources();

            int id = res.getIdentifier("lock" + i, "id", getPackageName());
            imgb = (ImageButton) findViewById(id);
            System.out.println(imgb.isActivated());


            if(view.getId()==imgb.getId()){

                if (imgb.isActivated()) {
                    viewsEditable(false, i);

                    imgb.setColorFilter(Color.rgb(255, 7, 107));
                    imgb.setActivated(false);

                } else {
                    viewsEditable(true, i);

                    imgb.setColorFilter(Color.rgb(7, 255, 148));

                    imgb.setActivated(true);

            }
        }
    }}
    public void buttonsColor(){ //při spuštění se nastaví barva tlačítka
        for (int i = 1; i <= pocetZavodniku; i++){
            Resources res = getResources();
            int id = res.getIdentifier("lock" + i, "id", getPackageName());
            imgb = (ImageButton) findViewById(id);

            imgb.setColorFilter(Color.rgb(255,7,107));
        }
    }
    public void viewsEditable(boolean edit, int i){

        Resources res = getResources();
        int id = res.getIdentifier("jmeno" + i, "id", getPackageName());
        view1 = (TextView) findViewById(id);
        view1.setEnabled(edit);
        int ids = res.getIdentifier("cas" + i, "id", getPackageName());
        view2 = (TextView) findViewById(ids);
        view2.setEnabled(edit);
        int id3 = res.getIdentifier("cislo" + i, "id", getPackageName());
        view3 = (TextView) findViewById(id3);
        view3.setEnabled(edit);

       // view2.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "12")});
    }

    public void openPicker(View view) {



        picker(view.getId());
    }
    public void picker(int cisloRadku){


        //loop s podmínkou hledá číslo řádku a z něj dostává Čas
        for (int i = 1; i<=pocetZavodniku; i++) {

            Resources res = getResources();
            int id = res.getIdentifier("cas" + i, "id", getPackageName());
            button = (Button) findViewById(id);

           if(cisloRadku == button.getId()){

               int idButton = res.getIdentifier("cas"+i,"id",getPackageName());
               button = (Button) findViewById(idButton);

              String cas = button.getText().toString();
              w = cas.replaceAll(" : ","");
               radek = Integer.parseInt(w);
               dulezite = i;

           }

        }

        int k = radek;

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

        // v numberPicker se rozdělí čísla podle dat
        for(int i = 1; i<=3; i++){

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
                        Resources res = getResources();
                        int idk = res.getIdentifier("cas" + dulezite, "id", getPackageName());
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
        Resources res = getResources();

        int row = 0;
        for (int i = 1; i <=pocetZavodniku; i++){

            int num = 20 +i;
            int id = res.getIdentifier("jmeno" + i, "id", getPackageName());
            int id2 = res.getIdentifier("cas" + i, "id", getPackageName());
            int id3 = res.getIdentifier("cislo" + i, "id", getPackageName());

            view1 = (TextView) findViewById(id);    //jmeno
            view2 = (TextView) findViewById(id2);   //cas
            view3 = (EditText) findViewById(id3);   //cislo

           finalArray[row][0] = view1.getText().toString();
           finalArray[row][1] = view2.getText().toString();
           finalArray[row][2] = view3.getText().toString();
           row = row +1;

        }
        Vysledky v = new Vysledky();
        v.poc(pocetZavodniku);
        v.finalArray(finalArray);
        System.out.println(finalArray[0][0]);
    }

}