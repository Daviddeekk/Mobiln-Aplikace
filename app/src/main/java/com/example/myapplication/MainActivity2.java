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

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity2 extends AppCompatActivity {
    private Button button;

    private TableRow odstranRow;
    TextView timerText;
    Button stopStartButton;
    static int u;
    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;

    boolean timerStarted = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        button = (Button) findViewById(R.id.zpet);
       // button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivity1();

            }


        });



        timerText = (TextView) findViewById(R.id.timerText);
        stopStartButton = (Button) findViewById(R.id.startStop);

        timer = new Timer();


    }
    public void openActivity1() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    /*public void startStop (View view)
    {

        Button start = findViewById(R.id.startStop);
        start.setText("Háráasdfklůals");


    }
    */
    public void reset(View view)
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(this);
        resetAlert.setTitle("Reset Timer");
        resetAlert.setMessage("Are you sure you want to reset the timer?");
        resetAlert.setPositiveButton("Reset", new DialogInterface.OnClickListener()
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

                }
            }
        });

        resetAlert.setNeutralButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                //do nothing
            }
        });

        resetAlert.show();

    }

    public void startStop(View view)
    {

        if(timerStarted == false)
        {
            timerStarted = true;
            setButtonUI("STOP");

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
                runOnUiThread(new Runnable()
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
      //  System.out.println(i);
    }

    public void odstran(View view) {

      //  int o = 1;

        System.out.println(u);
        int kolikrat = Math.abs(u-8);
        int smaz = 8;
        for (int i = 0; i < kolikrat; i++)
        {

            Resources res = getResources();
            int id = res.getIdentifier("row" + smaz, "id", getPackageName());


            System.out.println(id);

            odstranRow = (TableRow) findViewById(id);
            odstranRow.setVisibility(View.INVISIBLE);
            //dialogButton();
            u = u + 1;
            System.out.println("smazano " + smaz );
            smaz = smaz -1;

        }

        /*odstranB = (Button) findViewById(R.id.test);
        osm = (Button) findViewById(R.id.reset8);
        osm.setVisibility(View.GONE);
        osmTime = (TextView) findViewById(R.id.textView18);
        osmTime.setVisibility(View.GONE);
        osmZav = (TextView)  findViewById(R.id.textView8);
        osmZav.setVisibility(View.GONE);

     //   osmZav.setEnabled(true);*/
    }
}