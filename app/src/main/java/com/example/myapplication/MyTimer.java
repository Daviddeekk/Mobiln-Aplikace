package com.example.myapplication;
import android.annotation.SuppressLint;
import android.widget.TextView;
public class MyTimer { //timer pro závod s průběžným startem
    TextView textv;
    boolean isRunning, ended;
    double counter;
    String tag;
    public MyTimer(TextView txv, String tags) {
        tag = tags;
        this.textv = txv;
        isRunning = false;
        counter = 0;
        ended = false;
    }
    @SuppressLint("SuspiciousIndentation")
    public void start() {//po spuštění
        isRunning=true;
            textv.post(new Runnable() {
                @Override
                public void run() {
                    if (isRunning) {
                        int rounded = (int) Math.round(counter);
                        int hours = rounded/360000;
                        int minutes = (rounded / 6000) % 60;
                        int seconds = (rounded / 100) % 60;
                        int millis = rounded % 100;

                        if(tag.equals("prubezny")){
                            textv.setText(String.format("%02d : %02d : %02d",hours, minutes, seconds));
                            textv.postDelayed(this, 10);
                        }
                        if(tag.equals("rychlyPrubezny")){

                            textv.setText(String.format("%02d : %02d : %02d", minutes, seconds, millis));
                            textv.postDelayed(this, 10);
                        }
                        counter = counter +1.666666;
                    }
                }});}
    public void stop() {
        isRunning = false;
    }
}
