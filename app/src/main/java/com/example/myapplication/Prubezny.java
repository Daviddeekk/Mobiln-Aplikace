package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Prubezny extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private Button  b, startButton;
    private String TAG ="MainActivity2";
    private TableRow odstranRow;
    private TextView textv,textv2, jmenoSportu, casy;
    private EditText et;
    private static int u;
    private static String sport, casTag;
    private ImageButton zpet, pokracovat, start, resetButton, importB, zavodnici, stopB;
    boolean k = true;

    MyTimer[] timers = new MyTimer[u];
    Timer timer;
    Double time = 0.0;
    private int num, zastavenych;
    boolean timerStarted = false;
    String[][] newArray = new String[u][3];


    Button button;
    boolean isRunning;
    int counter;
    //int zastavenych;

    @Override
    public void onBackPressed() {
        {openActivity1();}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prubezny);
        onCreateView();
        timer = new Timer();

    }

    //jak bude vypadat stránka po spuštění
    private void onCreateView(){
        zastavenych = 0;
        
        jmenoSportu = (TextView) findViewById(R.id.hromadny);
        zpet = (ImageButton) findViewById(R.id.zpet);
        zavodnici = (ImageButton) findViewById(R.id.zavodnici);
        importB= (ImageButton) findViewById(R.id.importB);
        zavodniciEditable(true);
        jmenoSportu.setText(sport);
        System.out.println(sport);


        zpet.setColorFilter(Color.rgb(255,7,107));

        resetButton = (ImageButton) findViewById(R.id.reset);
        resetButton.setColorFilter(Color.rgb(50, 190, 202));

        pokracovat = (ImageButton)findViewById(R.id.pokracovat);
        pokracovat.setVisibility(View.INVISIBLE);

        cisloZavodnika();
        odstran();
        buttonReset(true);
        startTimerManyTimes();
        //odstraní řádky, které nepotřebujeme
        for(int i =1; i<=u; i++){
            int num = i +20;
            Resources res = getResources();
            int id = res.getIdentifier("textView" + num , "id", getPackageName());
            casy = (TextView) findViewById(id);
            casy.setText("00 : 00 : 00");
        }



    }
    public void back(View view){
        openActivity1();
    }

    public void openActivity1() {
        Intent intent = new Intent(this, Hlavni.class);
        startActivity(intent);                                  //metoda pro otevření první stránky
    }
    public void changeImageStopButton(){

    }

    public void reset(View view)                                //resetuje stopky i s výsledky
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setMessage("Opravdu chcete resetovat stopky i s výsledky?");
        resetAlert.setTitle("Resetovat stopky");
        resetAlert.setPositiveButton("Ano", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                    time = 0.0;
                    timerStarted = false;

                    zastavenych = 0;
                    textv.setEnabled(true);
                    for(int k = 1; k<=u;k++){
                        num = 20+k;
                        Resources res = getResources();
                        int id = res.getIdentifier("textView" + num, "id", getPackageName());
                        casy = (TextView) findViewById(id);
                        casy.setEnabled(true);
                        casy.setText("00 : 00 : 00");
                       // timers[k-1].counter = 0;
                        timers[k-1] = new MyTimer(textv, casTag);
                        timers[k-1].counter=0;




                }
                pokracovat = (ImageButton) findViewById(R.id.pokracovat);
                pokracovat.setVisibility(View.INVISIBLE);
                startTimerManyTimes();
                zavodniciEditable(true);

            }
        });
        resetAlert.setNeutralButton("Ne", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {}
        });
        resetAlert.show();
    }

    private void startTimerManyTimes(){
        for (int i = 1; i <=u; i++){
            Resources res = getResources();
            int id = res.getIdentifier("stop" + i, "id", getPackageName());

            stopB = (ImageButton) findViewById(id);
            stopB.setImageResource(R.drawable.playimage_foreground);
            stopB.setColorFilter(Color.rgb(50, 190, 201));
            stopB.setEnabled(true);
            stopB.setTag("stop" +i);
        }
    }

    public String getSport(String s){
        sport = s;
        return sport;
    }
    public String getTag(String gtag){
        casTag = gtag;
        return casTag;
    }

    public int getNumber(int i){
        u = i;
        return u;
    }
    //resetuje jména závodníků
    public void zavodniciReset (View view){

        for (int i = 1; i <= u; i++){
            Resources res = getResources();
            int id = res.getIdentifier("textView" + i, "id", getPackageName());
            textv = (TextView) findViewById(id);
            textv.setHint("Jméno závodníka");
            textv.setText(null);

            int id2 = res.getIdentifier("cisloZavodnika" + i, "id", getPackageName());

            et = (EditText) findViewById(id2);
            et.setText(Integer.toString(i));
        }
    }
    //odstrani přebytečné řádky, nechá jenom ty potřebné
    public void odstran() {
        int k = u;
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

    //při zastavení času u jenoho závodníka tlačítko a čas se vypnou
    public void stop(View view) {

        zavodniciEditable(false);
        String tag = view.getTag().toString();
        for (int i = 1; i <=u; i++){

            if(tag.equals("stop"+i)){
                resetButton.setEnabled(false);
                resetButton.setColorFilter(Color.GRAY);
                num = 20+i;
                Resources res = getResources();
                int id2 = res.getIdentifier("textView" + num, "id", getPackageName());
                textv = (TextView) findViewById(id2);

                int id1 = res.getIdentifier("stop" + i, "id", getPackageName());
                stopB = (ImageButton) findViewById(id1);

                timers[i-1] = new MyTimer(textv, casTag);
                final int index = i-1;

                timers[index].start();

                stopB.setImageResource(R.drawable.stopimage_foreground);
                stopB.setTag("started" + i);


            }
            else if(tag.equals("started" +i)){
                Resources res = getResources();
                String o = view.getTag().toString().replaceAll("[^0-9]+", "");
                int k = Integer.parseInt(o);

                int id1 = res.getIdentifier("stop" + o, "id", getPackageName());
                stopB = (ImageButton) findViewById(id1);

                timers[k-1].stop();
                zastavenych = zastavenych + 1;

                stopB.setColorFilter(Color.GRAY);
                stopB.setEnabled(false);
                System.out.println(zastavenych);
            }
        }
        //pokud vypl všechny objeví se pokracovat
        if(u == zastavenych)
        {
            resetButton.setEnabled(true);
            resetButton.setColorFilter(Color.rgb(50, 190, 201));
            pokracovat = (ImageButton) findViewById(R.id.pokracovat);
            pokracovat.setVisibility(View.VISIBLE);
        }
    }
    //nastaví na začátku alespon cislo zavodnika
    private void cisloZavodnika(){
        for(int i = 1; i<=u; i++){

            Resources res = getResources();
            int id = res.getIdentifier("cisloZavodnika" + i, "id", getPackageName());

            et = (EditText) findViewById(id);
            et.setText(Integer.toString(i));
        }
    }

    public void buttonReset(boolean reset){ // otevře tlačíka a závodníky
        for (int k = 21; k <=40; k++){

            int stopid = k-20;
            Resources res = getResources();
            int id = res.getIdentifier("stop" + stopid , "id", getPackageName());
            int idt = res.getIdentifier("textView" + k, "id", getPackageName());

            stopB = (ImageButton) findViewById(id);

            stopB.setEnabled(reset);

            if(stopB.isEnabled()){

                stopB.setColorFilter(Color.rgb(50, 190, 202));

            }
            else{
                stopB.setColorFilter(Color.GRAY);

            }
            textv = (TextView) findViewById(idt);

            zavodniciEditable(true);
        }
    }

    public void zavodniciEditable(boolean edit)
    {
        zavodnici.setEnabled(edit);
        importB.setEnabled(edit);

        if(edit){
            zavodnici.setColorFilter(Color.rgb(50, 190, 202));
            importB.setColorFilter(Color.rgb(50, 190, 202));
        }
        else{
            zavodnici.setColorFilter(Color.GRAY);
            importB.setColorFilter(Color.GRAY);
        }

        // int viewCount = 20;
        for (int i = 1; i <= u; i++) {
            Resources res = getResources();
            int id = res.getIdentifier("textView" + i, "id", getPackageName());
            textv = (TextView) findViewById(id);
            textv.setEnabled(edit);

            int id2 = res.getIdentifier("cisloZavodnika" +i, "id",getPackageName());
            et = (EditText) findViewById(id2);
            et.setEnabled(edit);
        }
    }

    public void openEd(){ //otevře stránku s editací

        pokracovat = (ImageButton) findViewById(R.id.pokracovat);
        Resources res = getResources();

        int row = 0;
        for (int i = 1; i <=u; i++){

            int num = 20 +i;
            int id = res.getIdentifier("textView" + i, "id", getPackageName());
            int id2 = res.getIdentifier("textView" + num, "id", getPackageName());
            int id3 = res.getIdentifier("cisloZavodnika" + i, "id", getPackageName());

            textv = (TextView) findViewById(id);    //jmeno
            textv2 = (TextView) findViewById(id2);  //cas
            et = (EditText) findViewById(id3); //cislo

            newArray[row][0] = textv.getText().toString();
            newArray[row][1] = textv2.getText().toString();
            newArray[row][2] = et.getText().toString();
            row = row +1;

        }
        Editace e = new Editace();
        e.setArray(newArray);
        e.getTag(casTag);
        e.poc(u);
        Intent intent = new Intent(this, Editace.class);
        startActivity(intent);

    }
    public void openEditace(View view){
        openEd();
    }
    public void upload(View view){
        launchFilePicker();

    }
    public void launchFilePicker() {
        // Create an intent for selecting a file via the file manager
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

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
                        data.add(line.split(","));
                    }
                    String[][] arrayData = data.toArray(new String[data.size()][]);

                    int dataLength = arrayData.length;
                    Resources res = getResources();
                    for (int i = 1; i <=dataLength; i++){
                        if(i <= u){
                            System.out.println(arrayData[0][0]);
                            int id = res.getIdentifier("textView" + i, "id", getPackageName());
                            et = (EditText) findViewById(id);
                            et.setText(arrayData[i-1][0]);
                            System.out.println(et.getText().toString());

                        }
                    }

                } catch (IOException e) {
                    Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


}
