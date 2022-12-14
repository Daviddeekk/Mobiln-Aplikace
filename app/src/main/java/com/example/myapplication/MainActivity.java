package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button prubezny;
    boolean length;
    boolean back = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //access to activity_main
    }
    @Override
    public void onBackPressed() {
        if (back == true) {}
    }

    public void openActivity2() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);

    }
    public void openActivity3(){
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }
    public void dialogButton(View view){
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(2);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Zadejte počet závodníků (1-20)");
        final EditText input = new EditText(this);
        input.setFilters(FilterArray);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(input.length() == 0){
                    dialogButton(view);
                }else{

                int cislo = Integer.parseInt(input.getText().toString());
                if(cislo <= 20 && cislo > 0
                 ){
                    String tag = view.getTag().toString();

                    if(tag.equals("hromadny")){

                        MainActivity2 m = new MainActivity2();
                        m.getNumber(cislo);
                        openActivity2();
                    }
                    else if (tag.equals("prubezny")){
                        //MainActivity3 n = new MainActivity3();
                        MainActivity2 m = new MainActivity2();
                        m.getNumber(cislo);
                        openActivity2();
                    }

                }

                else{
                dialogButton(view);
                }
                }
            }
        });

        builder.setNegativeButton("Zpět", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }



}