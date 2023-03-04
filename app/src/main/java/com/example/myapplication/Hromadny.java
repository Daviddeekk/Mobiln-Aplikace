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

    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    private int num, zastavenych;
    boolean timerStarted = false, timerGoing = true;
    String[][] newArray = new String[pocetZavodniku][3];

    @Override
    public void onBackPressed() {
        {
            if(zpet.isEnabled()){
                stopTimer();
                goBackDialog();
            }else{

            }
        }
    }

    private void stopTimer() {
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hromadny);

        defineButtons();
        defaultcisloZavodnika();
        timer = new Timer();                                    //vytvoření timeru
        odstran();//odstraní řádky, které nepotřebujeme

    }

    private void defineButtons() {


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

            textv.setTextColor(isDarkMode(this) ? Color.WHITE : Color.BLACK);
            et.setTextColor(isDarkMode(this) ? Color.WHITE : Color.BLACK);
        }
        buttonReset(false);
    }

    public void backButton(View view) {
        stopTimer();
        goBackDialog();
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
                if (timerTask != null) {

                    time = 0.0;
                    timerTask.cancel();
                    timerText.setText(formatTime(0, 0, 0));
                    timerStarted = false;
                    buttonReset(false);
                    buttonUpdate(true);
                    zastavenych = 0;
                    textv.setEnabled(true);

                    for (int k = 1; k <= pocetZavodniku; k++) {
                        TextView textView = timesArray[k - 1];
                        textView.setEnabled(true);

                    }
                    resetButton.setEnabled(false);
                    resetButton.setColorFilter(Color.GRAY);
                }
                pokracovat = (ImageButton) findViewById(R.id.pokracovat);
                pokracovat.setVisibility(View.INVISIBLE);
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
           // zpet.setEnabled(false);
        } else {
            timerStarted = false;
            timerTask.cancel();
        }
    }

    public String getTag(String gtag) {
        casTag = gtag;
        return casTag;
    }

    private void startTimer()                 //po začátkpocetZavodnikutimeru
    {
        timerTask = new TimerTask() {
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
                    }
                });
            }
        };

            timer.scheduleAtFixedRate(timerTask, 0, 10);

    }

    private String getTimerText() { //z time dostane minuty, hodiny, sekundy, setiny
        int rounded = (int) Math.round(time);
        int hours = rounded / 360000;
        int minutes = (rounded / 6000) % 60;
        int seconds = (rounded / 100) % 60;
        int millis = rounded % 100;
        if (casTag.equals("hromadny")) {
            return formatTime(seconds, minutes, hours);
        } else {
            return formatTime(millis, seconds, minutes);
        }
    }

    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    public String getSport(String s) {
        sport = s;
        return sport;
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
        String tag = view.getTag().toString();
        for (int i = 0; i < pocetZavodniku; i++) {
            if (tag.equals("stop" + (i + 1))) {
                TextView textView = timesArray[i];
                ImageButton stopB = findViewById(getResources().getIdentifier("stop" + (i + 1), "id", getPackageName()));
                textView.setEnabled(false);
                stopB.setColorFilter(textView.isEnabled() ? Color.rgb(18, 94, 188) : Color.GRAY);
                stopB.setEnabled(false);
                zastavenych++;
                textView.setText(getTimerText());
            }
        }
        //pokud vypl všechny objeví se pokracovat
        if (pocetZavodniku == zastavenych) {

            timerTask.cancel();

            ImageButton pokracovat = findViewById(R.id.pokracovat);
            pokracovat.setVisibility(View.VISIBLE);
            zpet.setEnabled(true);
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

    private void setCasToAll() {
        String timerText = getTimerText();
        for (int i = 1; i <= pocetZavodniku; i++) {
            TextView textView = timesArray[i - 1];
            if (textView.isEnabled() && !textView.getText().equals(timerText)) {
                textView.setText(timerText);
            }
        }
    }

    private void buttonReset(boolean reset) { // otevře tlačíka a závodníky
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

    private void openEd() { //otevře stránkpocetZavodnikus editací
        pokracovat = (ImageButton) findViewById(R.id.pokracovat);

        int row = 0;
        for (int i = 1; i <= pocetZavodniku; i++) {

            TextView cas = timesArray[i - 1];
            EditText zavodnici = zavodniciArray[i - 1];
            EditText cisloZavodnika = cisloZavodnikaArray[i - 1];

            newArray[row][0] = zavodnici.getText().toString();
            newArray[row][1] = cas.getText().toString();
            newArray[row][2] = cisloZavodnika.getText().toString();
            row = row + 1;
        }

        Editace e = new Editace();
        e.getTag(casTag);
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
                Dialog zapsatDialog = new Dialog(Hromadny.this);
                zapsatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                zapsatDialog.setContentView(R.layout.load);

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
                Dialog zapsatDialog = new Dialog(Hromadny.this);
                zapsatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                zapsatDialog.setContentView(R.layout.load);
                launchFilePicker();
                dialog.cancel();
            }
        });
        dialog.show();
    }

    private void launchFilePicker() {
        // Create an intent for selecting a file via the file manager
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            String[] names = resultData.getStringArrayExtra("selectedRows");
            View layout = findViewById(R.id.hromadnyLayoutt);
            Resources res = getResources();
            for (int i = 1; i <= names.length; i++) {
                if (i <= pocetZavodniku) {
                    EditText zavodnici = zavodniciArray[i - 1];
                    zavodnici.setText(names[i - 1]);
                }
            }
        }
        // If the selection worked, we have a URI pointing to the file
        else if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Get the URI of the selected file
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
                        }
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
