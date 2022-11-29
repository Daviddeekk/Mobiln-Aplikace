package com.example.myapplication;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {
    private Button button;
    private Button b;
    private TableRow odstranRow;
    TextView textv;
    TextView timerText;
    Button stopStartButton;
    private static int u;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    private int num;
    boolean timerStarted = false;
    int zastavenych;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        button = (Button) findViewById(R.id.zpet);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivity1();
            }
        });

        timerText = (TextView) findViewById(R.id.timerText);
        stopStartButton = (Button) findViewById(R.id.startStop);
        buttonReset(false);
        timer = new Timer();

    }
    public void openActivity1() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void reset(View view)
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
                    setButtonUI("START");
                    time = 0.0;
                    timerStarted = false;
                    timerText.setText(formatTime(0,0,0));
                    buttonReset(false);
                    startButtonReset(true);
                    zastavenych = 0;
                }
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

        }
        else
        {
            timerStarted = false;
            setButtonUI("START");
            timerTask.cancel();
        }
    }
    private void setButtonUI(String start)
    {
        stopStartButton.setText(start);
    }

    private void startTimer()
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
                    }
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

    public void zavodniciReset (View view){
        int kolikrat = 8;
        for (int i = 1; i <= kolikrat; i++){
            Resources res = getResources();
            int id = res.getIdentifier("textView" + i, "id", getPackageName());

            textv = (TextView) findViewById(id);
            textv.setText(null);
            textv.setHint("Jméno závodníka");



        }
    }
    public void odstran(View view) {

        System.out.println(zastavenych);
        int k = u;
        int kolikrat = Math.abs(k-8);
        System.out.println(u);
        int smaz = 8;
        for (int i = 0; i < kolikrat; i++)
        {
            Resources res = getResources();
            int id = res.getIdentifier("row" + smaz, "id", getPackageName());

            odstranRow = (TableRow) findViewById(id);
            odstranRow.setVisibility(View.INVISIBLE);

            k = k + 1;

            smaz = smaz -1;

        }
    }

    public void stop(View view) {

            switch (view.getTag().toString()){
                case "stop1":
                    num = 1; view.setEnabled(false); zastavenych =zastavenych +1; break;
                case "stop2":
                    num = 2; view.setEnabled(false); zastavenych =zastavenych +1; break;
                case "stop3":
                    num = 3; view.setEnabled(false); zastavenych =zastavenych +1; break;
                case "stop4":
                    num = 4; view.setEnabled(false); zastavenych =zastavenych +1; break;
                case "stop5":
                    num = 5; view.setEnabled(false); zastavenych =zastavenych +1; break;
                case "stop6":
                    num = 6; view.setEnabled(false); zastavenych =zastavenych +1; break;
                case "stop7":
                    num = 7; view.setEnabled(false); zastavenych =zastavenych +1; break;
                case "stop8":
                    num = 8; view.setEnabled(false); zastavenych =zastavenych +1 ;break;

            }
        if(u == zastavenych)
        {
            timerTask.cancel();
            System.out.println("canceled");
        }
        Resources res = getResources();
        //System.out.println(zastavenych);
        int id = res.getIdentifier("textView1" + num, "id", getPackageName());

           textv = (TextView) findViewById(id);
            textv.setText(getTimerText());
       ;
    }
    public void buttonReset(boolean reset){
        for (int k = 1; k <=8; k++){
            Resources res = getResources();
            int id = res.getIdentifier("stop" + k, "id", getPackageName());
            int idt = res.getIdentifier("textView1" + k, "id", getPackageName());
            b = (Button) findViewById(id);
            b.setEnabled(reset);
            textv = (TextView) findViewById(idt);
            textv.setText("TIME");
        }
    }
    public void startButtonReset(boolean resetStart)
    {
        b = (Button) findViewById(R.id.startStop);
        b.setEnabled(resetStart);
    }
}