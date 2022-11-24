package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //access to activity_main

        /*LinearLayout layout = new LinearLayout(this);
        for (int i = 0; i < 3; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setLayoutParams(new LinearLayout.LayoutParams
                    (LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            for (int j = 0; j < 4; j++) {
                Button btnTag = new Button(this);
                btnTag.setLayoutParams(new LinearLayout.LayoutParams
                        (LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT));
                btnTag.setText("Button " + (j + 1 + (i * 4 )));
                btnTag.setId(j + 1 + (i * 4));

                row.addView(btnTag);
            }
            layout.addView(row);
        }
        setContentView(layout);
        */



        button = (Button) findViewById(R.id.beh);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {


                openActivity2();


            }
        });

    }

    public void openActivity2() {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);


    }
    /*public void createButtons(View view)
    {
       int id = view.getId();
    }

   */
    /*private void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }*/
    /*public void poStisknuti (View view){
       // Button tlacitkoBeh = findViewById(R.id.beh);
        //tlacitkoBeh.setText("Běhání");
        setContentView(R.layout.activity_main2);
       // startActivity(new Intent(getApplicationContext(), hromadnyZavod.class));
    }
*/


}