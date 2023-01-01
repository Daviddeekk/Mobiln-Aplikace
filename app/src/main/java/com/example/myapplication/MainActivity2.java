package com.example.myapplication;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {
    private Button button, b, startButton;
    private TableRow odstranRow;
    private TextView textv,textv2, timerText;
    private EditText et;
    private static int u;
    private ImageButton zpet, pokracovat;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    private int num, zastavenych;
    boolean timerStarted = false;
    String[][] newArray = new String[u][3];

    //int zastavenych;

    @Override
    public void onBackPressed() {
         {openActivity1();}
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        buttonReset(false);                                     //na začátku nemohu zmáčknout tlačítka
        zpet = (ImageButton) findViewById(R.id.zpet);
        zpet.setColorFilter(Color.rgb(255,7,107));


        timerText = (TextView) findViewById(R.id.timerText);    //definování timerTextu
        startButton = (Button) findViewById(R.id.startStop);    //definování tlačítka start
        cisloZavodnika();
        timer = new Timer();                                    //vytvoření timeru
        odstran();                                              //odstraní řádky, které nepotřebujeme

        pokracovat = (ImageButton)findViewById(R.id.pokracovat);
        pokracovat.setVisibility(View.INVISIBLE);               //tlačítko pro pokračování bude neviditelně

    }
    public void back(View view){
        openActivity1();
    }
    public void openActivity1() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);                                  //metoda pro otevření první stránky
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
                if(timerTask != null)
                {
                    timerTask.cancel();
                    //setButtonUI("START");
                    time = 0.0;
                    timerStarted = false;
                    timerText.setText(formatTime(0,0,0));
                    buttonReset(false);
                    startButtonReset(true);
                    zastavenych = 0;
                    textv.setEnabled(true);
                    for(int k = 1; k<=20;k++){
                        num = 20+k;
                        Resources res = getResources();
                        int id = res.getIdentifier("textView" + num, "id", getPackageName());
                        textv = (TextView) findViewById(id);
                        textv.setEnabled(true);
                    }
                }
                pokracovat = (ImageButton) findViewById(R.id.pokracovat);
                pokracovat.setVisibility(View.INVISIBLE);
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

    public void startStop(View view)
    {
        if(timerStarted == false)
        {
            buttonReset(true);
            timerStarted = true;
            startButtonReset(false);
            startTimer();
            zavodniciEditable(false);
        }
        else
        {
            timerStarted = false;
            //setButtonUI("START");
            timerTask.cancel();

        }
    }
    //private void setButtonUI(String start) {startButton.setText(start);}

    private void startTimer()                 //po začátku timeru
    {
        timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                runOnUiThread(new Runnable() //okamžité spuštění, nečeká se
                {
                    @Override
                    public void run()
                    {
                        time++;
                        timerText.setText(getTimerText());
                        //aby se čas u závodníků nenastavoval pokaždé ale pouze po jedné sekundě, zbytečně mnoho operací
                            if(timerText.getText().equals(textv.getText())){
                            }else{
                                allTimes();}}
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0 ,1000);
    }
    public String getTimerText()
    {
        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);
        return formatTime(seconds, minutes, hours);
    }
    private String formatTime(int seconds, int minutes, int hours)
    {
        return String.format("%02d",hours) + " : " + String.format("%02d",minutes) + " : " + String.format("%02d",seconds);
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
        String tag = view.getTag().toString();
        for (int i = 1; i <=u; i++){
            if(tag.equals("stop"+i)){
                num = 20+i;
                view.setEnabled(false);
                zastavenych = zastavenych + 1;
                Resources res = getResources();
                int id = res.getIdentifier("textView" + num, "id", getPackageName());

                textv = (TextView) findViewById(id);
                textv.setText(getTimerText());
                textv.setEnabled(false);

            }
        }
        //pokud vypl všechny objeví se pokracovat
        if(u == zastavenych)
        {
            timerTask.cancel();
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

    private void allTimes(){
        for(int i = 1; i<=u;i++){

            num = 20+i;
            Resources res = getResources();
            int id = res.getIdentifier("textView" + num, "id", getPackageName());

            textv = (TextView) findViewById(id);
            if(textv.isEnabled()){
                textv.setText(getTimerText());
            }else{}
        }}

    public void buttonReset(boolean reset){ // otevře tlačíka a závodníky
        for (int k = 21; k <=40; k++){
            int stopid = k-20;
            Resources res = getResources();
            int id = res.getIdentifier("stop" + stopid , "id", getPackageName());
            int idt = res.getIdentifier("textView" + k, "id", getPackageName());
            b = (Button) findViewById(id);
            b.setEnabled(reset);
            textv = (TextView) findViewById(idt);
            textv.setText(getTimerText());
            zavodniciEditable(true);
        }
    }
    public void startButtonReset(boolean resetStart)
    {
        b = (Button) findViewById(R.id.startStop);
        b.setEnabled(resetStart);
    }
    public void zavodniciEditable(boolean edit)
    {
        Button tlacitko = (Button) findViewById(R.id.zavodnici);
        tlacitko.setEnabled(edit);
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
        e.poc(u);
        Intent intent = new Intent(this, Editace.class);
        startActivity(intent);

    }
        public void openEditace(View view){
            openEd();
            }
}