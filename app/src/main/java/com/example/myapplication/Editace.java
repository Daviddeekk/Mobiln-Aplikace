package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.res.Resources;


public class Editace extends AppCompatActivity {
    private TextView view1;
    private TextView view2;
    private static String u;
    private static String w;
    private Button button;
    private ImageButton imgb;
    boolean active = true;
    //TextView jmeno;
    //static String getName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editace);
        setName(u);
        setCas(w);
        viewsEditable(false);
        buttonEditable(true);

      //  System.out.println(getName);
        button = (Button) findViewById(R.id.zpet);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openActivity2();
            }
        });
    }
    public void openActivity2() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }
    public String getName(String name){

        u = name;
        return u;

    }
    public String getCas(String cas){
        w = cas;
        return w;
    }
    public void setName(String name){
        view1 = findViewById(R.id.jmeno1);
        view1.setText(name);
    }
    public void setCas(String cas){
        view1 = findViewById(R.id.cas1);
        view1.setText(cas);
    }
    public void zavodniciEditable(View view) {
       // button = (Button) findViewById(R.id.zavodnici);
        //button.setEnabled(edit);
       // int viewCount = 20;

        imgb = (ImageButton) findViewById(R.id.lock1);

        Resources res = getResources();

        int id = res.getIdentifier("jmeno1", "id", getPackageName());
        view1 = (TextView) findViewById(id);


        int ids = res.getIdentifier("cas1", "id", getPackageName());
        view2 = (TextView) findViewById(ids);


        if(active == true){
            view1.setEnabled(true);
            view2.setEnabled(true);
            active = false;
            imgb.setColorFilter(Color.rgb(7,255,148));
        }
        else {
            view1.setEnabled(false);
            view2.setEnabled(false);
            active = true;
            imgb.setColorFilter(Color.rgb(255,7,107));
            System.out.println(w);
           w = w.replaceAll(" : ","");
            System.out.println(w);

            System.out.println(w);
            int k = Integer.parseInt(w);

            System.out.println(w);
        }
       /* for (int i = 1; i <= viewCount; i++) {
            Resources res = getResources();
            int id = res.getIdentifier("textView" + i, "id", getPackageName());
            v = (TextView) findViewById(id);
            v.setEnabled(edit);

        }*/
    }
    public void buttonEditable(boolean edit){
        imgb = (ImageButton) findViewById(R.id.lock1);
        imgb.setEnabled(edit);
        imgb.setColorFilter(Color.rgb(255,7,107));
    }
    public void viewsEditable(boolean edit){
        Resources res = getResources();
        int id = res.getIdentifier("jmeno1", "id", getPackageName());
        view1 = (TextView) findViewById(id);
        view1.setEnabled(edit);
        int ids = res.getIdentifier("cas1", "id", getPackageName());
        view2 = (TextView) findViewById(ids);
        view2.setEnabled(edit);
       // view2.setFilters(new InputFilter[]{ new InputFilterMinMax("1", "12")});
    }
}