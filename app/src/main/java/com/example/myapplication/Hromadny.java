package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Dialog;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Hromadny extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 42;
    private static final int REQUEST_CODE = 1;
    private String TAG = "MainActivity2";
    private TableRow odstranRow;
    private TextView textv, textv2, timerText;
    private EditText et;
    private static int pocetZavodniku;
    private static String sport, casTag;
    private ImageButton zpet, pokracovat, start, resetButton, importB, zavodnici, stopB;
    private TextView[] timesArray;
    private EditText[] zavodniciArray, cisloZavodnikaArray;
    private ImageButton[] stopButtonArray;

    boolean bezi = false;
    Timer timer;
    TimerTask task;
    Double time = 0.0;
    private int num, zastavenych;
    boolean timerStarted = false, timerGoing = true;
    String[][] newArray = new String[pocetZavodniku][3];

    @Override
    public void onBackPressed() {
        {
            if(zpet.isEnabled()&&task !=null)
            {
                task.cancel();
                goBackDialog();
            }
            else if(zpet.isEnabled()){
                hlavni();
                stopTimer();
            }else{
            }}}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hromadny);
        defineButtons();
                                              //odstraní řádky, které nepotřebujeme
    }

    private void defineButtons() {

        timer = new Timer();
        zavodnici = (ImageButton) findViewById(R.id.zavodnici);
        zavodnici.setColorFilter(Color.rgb(18, 94, 188));

        start = (ImageButton) findViewById(R.id.startStop);
        start.setColorFilter(Color.rgb(18, 94, 188));

        importB = (ImageButton) findViewById(R.id.importB);
        importB.setColorFilter(Color.rgb(18, 94, 188));
        //na začátkpocetZavodnikunemohpocetZavodnikuzmáčknout tlačítka
        zpet = (ImageButton) findViewById(R.id.zpet);
        zpet.setColorFilter(Color.rgb(255, 7, 107));

        resetButton = (ImageButton) findViewById(R.id.reset);
        resetButton.setEnabled(false);
        resetButton.setColorFilter(Color.GRAY);

        timerText = (TextView) findViewById(R.id.timerText);

        pokracovat = (ImageButton) findViewById(R.id.pokracovat);
        pokracovat.setVisibility(View.INVISIBLE);

        timesArray = new TextView[pocetZavodniku];
        zavodniciArray = new EditText[pocetZavodniku];
        cisloZavodnikaArray = new EditText[pocetZavodniku];
        stopButtonArray = new ImageButton[pocetZavodniku];

        for (int i = 1; i <= pocetZavodniku; i++) {
            int id1 = getResources().getIdentifier("textView" + (20 + i), "id", getPackageName());
            int id2 = getResources().getIdentifier("textView" + i, "id", getPackageName());
            int id3 = getResources().getIdentifier("cisloZavodnika" + i, "id", getPackageName());
            int id4 = getResources().getIdentifier("stop" + i, "id", getPackageName());

            timesArray[i - 1] = findViewById(id1);
            zavodniciArray[i - 1] = findViewById(id2);
            cisloZavodnikaArray[i - 1] = findViewById(id3);
            stopButtonArray[i - 1] = findViewById(id4);

            et = findViewById(id3);
            textv = findViewById(id2);
            textv2 = findViewById(id1);

            textv2.setTextColor(isDarkMode(this) ? Color.WHITE : Color.BLACK);
            textv.setTextColor(isDarkMode(this) ? Color.WHITE : Color.BLACK);
            et.setTextColor(isDarkMode(this) ? Color.WHITE : Color.BLACK);
        }
        buttonReset(false);
        defaultcisloZavodnika();                            //vytvoření timeru
        odstran();
    }
    private void stopTimer() {
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
    public void backButton(View view) {
        if(zpet.isEnabled()&&task !=null)
        {
            task.cancel();
            goBackDialog();
        }
        else if(zpet.isEnabled()){
            stopTimer();
            hlavni();
        }
    }

    private void hlavni() {
        Intent intent = new Intent(this, Hlavni.class);
        startActivity(intent);                                  //metoda pro otevření první stránky
    }

    public void resetVysledky(View view)                                //resetuje stopky i s výsledky
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyAlertDialogTheme));
        resetAlert.setMessage("Opravd chcete resetovat stopky i s výsledky?");
        resetAlert.setTitle("Resetovat stopky");
        resetAlert.setPositiveButton("Ano", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (task != null) {
                    timerStarted = false;
                    buttonReset(false);
                    buttonUpdate(true);
                    zastavenych = 0;
                    for (int k = 1; k <= pocetZavodniku; k++) {
                        TextView textView = timesArray[k - 1];
                        textView.setEnabled(true);
                    }
                    resetButton.setEnabled(false);
                    resetButton.setColorFilter(Color.GRAY);
                }
                pokracovat = (ImageButton) findViewById(R.id.pokracovat);
                pokracovat.setVisibility(View.INVISIBLE);
                task.cancel();
                time = 0.0;
                bezi = false;

                timerText.setText(formatTime(0, 0, 0));
                setCasToAll();
            }
        });
        resetAlert.setNeutralButton("Ne", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog dialog = resetAlert.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.white));
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(this, R.color.white));


    }
    public void goBackDialog(){
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyAlertDialogTheme));
        resetAlert.setMessage("Opravdu chcete odejít, prijdete o všechna naměřená data");
        resetAlert.setTitle("Odejít?");
        resetAlert.setPositiveButton("Ano", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                stopTimer();
                hlavni();
            }
        });
        resetAlert.setNeutralButton("Ne", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {}
        });
        AlertDialog dialog = resetAlert.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.white));
        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    private boolean isDarkMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }

    public void startButton(View view) {
        if (timerStarted == false) {
            resetButton.setEnabled(true);
            resetButton.setColorFilter(Color.rgb(18, 94, 188));
            buttonReset(true);
            timerStarted = true;
            buttonUpdate(false);
            startTimer();
            zavodniciEditable(false);
            bezi = true;
        } else {
            timerStarted = false;
            task.cancel();
        }
    }

    public String getTag(String gtag) {
        casTag = gtag;
        return casTag;
    }

    private void startTimer()
    {
        task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() //okamžité spuštění, nečeká se
                {
                    @Override
                    public void run() {
                        time++;
                        //aby se čas pocetZavodnikuzávodníků nenastavoval pokaždé ale pouze po jedné sekundě, zbytečně mnoho operací
                        if (!timerText.getText().equals(timesArray[0].getText())) {
                            setCasToAll();
                        }
                        timerText.setText(getTimerText());
                        if(!bezi){
                            time = 0.0;
                            timerText.setText(getTimerText());
                            setCasToAll();
                        }}});}};
            timer.scheduleAtFixedRate(task, 0, 10);
    }
    private void setCasToAll() {
        String timerText = getTimerText();
        for (int i = 0; i < timesArray.length; i++) {
            TextView textView = timesArray[i];
            if (textView.isEnabled() && !textView.getText().equals(timerText)) {
                textView.setText(timerText);
            }
        }}

    public String getTimerText() {
        int roundedTime = (int) Math.round(time);
        int hours = roundedTime / 360000;
        int minutes = (roundedTime / 6000) % 60;
        int seconds = (roundedTime / 100) % 60;
        int milliseconds = roundedTime % 100;
        if (casTag.equals("hromadny")) {
            return formatTime(seconds, minutes, hours);
        } else {
            return formatTime(milliseconds, seconds, minutes);
        }
    }
    public String formatTime(int seconds, int minutes, int hours) {
        String formattedSeconds = (seconds < 10) ? "0" + seconds : String.valueOf(seconds);
        String formattedMinutes = (minutes < 10) ? "0" + minutes : String.valueOf(minutes);
        String formattedHours = (hours < 10) ? "0" + hours : String.valueOf(hours);
        return formattedHours + " : " + formattedMinutes + " : " + formattedSeconds;
    }


    public int getNumber(int i) {
        pocetZavodniku = i;
        return pocetZavodniku;
    }

    //resetuje jména závodníků
    public void jmenaZavodnikuReset(View view) {
        for (int i = 1; i <= pocetZavodniku; i++) {
            TextView textView = zavodniciArray[i - 1];
            textView.setEnabled(true);
            textView.setHint("Jméno závodníka");
            textView.setText(null);
            EditText et = cisloZavodnikaArray[i - 1];
            et.setText(Integer.toString(i));
        }
    }
    //odstrani přebytečné řádky, nechá jenom ty potřebné
    private void odstran() {
        int k = pocetZavodniku;
        int kolikrat = 20 - k;
        int smaz = 20;
        for (int i = 0; i < kolikrat; i++) {
            Resources res = getResources();
            int id = res.getIdentifier("row" + smaz, "id", getPackageName());
            odstranRow = (TableRow) findViewById(id);
            odstranRow.setVisibility(View.GONE);
            k = k + 1;
            smaz = smaz - 1;
        }
    }

    //při zastavení časpocetZavodnikupocetZavodnikujenoho závodníka tlačítko a čas se vypnou
    public void stop(View view) {
        String tag = view.getTag().toString();          //dostanu tag stisknutého tlačítka
        for (int i = 0; i < pocetZavodniku; i++) {
            if (tag.equals("stop" + (i + 1))) {         //pokud se tag rovná nějakému tlačítku stop tak...
                TextView textView = timesArray[i];
                ImageButton stopB = findViewById(getResources().getIdentifier("stop" + (i + 1), "id", getPackageName()));
                textView.setEnabled(false);             //vypne se textView
                stopB.setColorFilter(textView.isEnabled() ? Color.rgb(18, 94, 188) : Color.GRAY);
                stopB.setEnabled(false);                //vypne se tlačítko.
                zastavenych++;                          //k hodnotě zastavených se přičte 1
                textView.setText(getTimerText());       //nastaví konečný čas
            }}
        //pokud vypl všechny objeví se tlačítko pokračovat
        if (pocetZavodniku == zastavenych) {
            task.cancel();
            timerText.setText(getTimerText());
            ImageButton pokracovat = findViewById(R.id.pokracovat);
            pokracovat.setVisibility(View.VISIBLE);
            zpet.setEnabled(true);              //zapnou se tlačítka, která byla vyplá.
            zpet.setColorFilter(Color.RED);
            importB.setEnabled(true);
            importB.setColorFilter(Color.rgb(18, 94, 188));
        }
    }

    //nastaví na začátkpocetZavodnikualespon cislo zavodnika
    private void defaultcisloZavodnika() {
        for (int i = 1; i <= pocetZavodniku; i++) {
            EditText et = cisloZavodnikaArray[i - 1];
            et.setText(Integer.toString(i));
        }
    }



    private void buttonReset(boolean reset) { // ressetuje tlačíka a časy
        for (int i = 1; i <= pocetZavodniku; i++) {
            ImageButton imgButton = stopButtonArray[i - 1];
            TextView et = timesArray[i - 1];

            imgButton.setEnabled(reset);
            if (imgButton.isEnabled()) {
                imgButton.setColorFilter(Color.RED);
            } else {
                imgButton.setColorFilter(Color.GRAY);
            }
            et.setText(getTimerText());
            zavodniciEditable(true);
        }
    }

    private void buttonUpdate(boolean resetStart) {
        start.setEnabled(resetStart);

        if (resetStart == true) {
            start.setColorFilter(Color.rgb(18, 94, 188));
            importB.setColorFilter(Color.rgb(18, 94, 188));
            zavodnici.setColorFilter(Color.rgb(18, 94, 188));
            zpet.setEnabled(true);
            zpet.setColorFilter(Color.RED);

        } else {
            start.setColorFilter(Color.GRAY);
            importB.setColorFilter(Color.GRAY);
            zavodnici.setColorFilter(Color.GRAY);
            zpet.setEnabled(false);
            zpet.setColorFilter(Color.GRAY);
        }
    }

    private void zavodniciEditable(boolean edit) {
        zavodnici.setEnabled(edit);
        importB.setEnabled(edit);
        for (int i = 1; i <= pocetZavodniku; i++) {
            EditText et = zavodniciArray[i - 1];
            et.setEnabled(edit);

            EditText et2 = cisloZavodnikaArray[i - 1];
            et2.setEnabled(edit);
        }
    }

    private void openEd() {
        int row = 0;
        for (int i = 1; i <= pocetZavodniku; i++) {
            TextView cas = timesArray[i - 1];           //definuje odkud budu brát data
            EditText zavodnici = zavodniciArray[i - 1];
            EditText cisloZavodnika = cisloZavodnikaArray[i - 1];

            newArray[row][0] = zavodnici.getText().toString();      //do 2D array uložení dat závodu
            newArray[row][1] = cas.getText().toString();
            newArray[row][2] = cisloZavodnika.getText().toString();
            row = row + 1;
        }
        Editace e = new Editace();
        e.getTag(casTag);           //z třídy Editace volám metody, které přenesou data
        e.setArray(newArray);
        e.poc(pocetZavodniku);
        Intent intent = new Intent(this, Editace.class);
        startActivity(intent);
    }
    public void openEditace(View view) {
        openEd();
    }

    public void uploadJmena(View view) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.selector);

        ImageButton nacteniZCsv = dialog.findViewById(R.id.importZCsv);
        ImageButton zDatabaze = dialog.findViewById(R.id.zdatabaze);
        zDatabaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Hromadny.this, NacitaniZavodnikuZDatabaze.class);
                NacitaniZavodnikuZDatabaze ds = new NacitaniZavodnikuZDatabaze();
                ds.getNumber(pocetZavodniku);
                startActivityForResult(intent, REQUEST_CODE);
                dialog.cancel();
            }
        });
        nacteniZCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("text/*");
                startActivityForResult(intent, READ_REQUEST_CODE);
                dialog.cancel();
            }
        });
        dialog.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String[] names = resultData.getStringArrayExtra("selectedRows");
            for (int i = 1; i <= names.length; i++) {
                if (i <= pocetZavodniku) {
                    EditText zavodnici = zavodniciArray[i - 1];
                    zavodnici.setText(names[i - 1]);
                }}}
        else if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                try {
                    InputStream is = getContentResolver().openInputStream(uri);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    List<String[]> data = new ArrayList<>();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        data.add(line.split(";"));
                    }
                    String[][] arrayData = data.toArray(new String[data.size()][]);
                    int dataLength = arrayData.length;
                    Resources res = getResources();
                    for (int i = 1; i <= dataLength; i++) {
                        if (i <= pocetZavodniku) {
                            EditText zavodnici = zavodniciArray[i - 1];
                            zavodnici.setText(arrayData[i - 1][0]);
                        }}
                } catch (IOException e) {
                    Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
                }}}
    }
}
