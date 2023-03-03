package com.example.myapplication;
import android.widget.TextView;
public class MyTimer {
    TextView textv;
    boolean isRunning, ended;
    double counter;
    String tag;
    public MyTimer(TextView button, String tags) {
        tag = tags;
        this.textv = button;
        isRunning = false;
        counter = 0;
        ended = false;
    }
    public String getT (String gtag){
        tag = gtag;
        return tag;
    }
    public void start() {
        System.out.println(tag);
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
