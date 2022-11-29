package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private Button button;
    private Button prubezny;
    boolean length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //access to activity_main
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


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Zadejte počet závodníků (1-8)");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_CLASS_NUMBER);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(input.length() == 0){
                    dialogButton(view);
                }else{


                int cislo = Integer.parseInt(input.getText().toString());

                if(cislo < 9 && cislo > 0 //&& input.getText() != null
                 ){
                    String tag = view.getTag().toString();
                    System.out.println(tag);
                    //openActivity3();

                    if(tag.equals("hromadny")){
                        System.out.println("here");
                        MainActivity2 m = new MainActivity2();
                        m.getNumber(cislo);
                        openActivity2();
                    }
                    else if (tag.equals("prubezny")){
                        //MainActivity3 n = new MainActivity3();
                        openActivity3();
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