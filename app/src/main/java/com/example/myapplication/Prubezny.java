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

public class Prubezny extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
    private static final int READ_REQUEST_CODE = 42;
    private String TAG ="MainActivity2";
    private TableRow odstranRow;
    private TextView textv,textv2, jmenoSportu, casy;
    private EditText et;
    private static int pocetZavodniku;
    private static String sport, casTag;
    private ImageButton zpet, pokracovat, start, resetButton, importB, zavodnici, stopB;
    private TextView[] timesArray;
    private EditText[] zavodniciArray, cisloZavodnikaArray;
    private ImageButton[] stopButtonArray;

    MyTimer[] timers = new MyTimer[pocetZavodniku];
    Timer timer;
    Double time = 0.0;
    private int num, zastavenych;
    boolean timerStarted = false;
    String[][] newArray = new String[pocetZavodniku][3];

    @Override
    public void onBackPressed() {
        {
            if(zpet.isEnabled()){

                goBackDialog();
            }else{

            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prubezny);
        defineButtons();
        timer = new Timer();
        allZavodniciTextColor();
    } //jak bude vypadat stránka po spuštění
    private void defineButtons(){//definice tlačítek, polí, ....

        zastavenych = 0;

        zpet = (ImageButton) findViewById(R.id.zpet);
        zavodnici = (ImageButton) findViewById(R.id.zavodnici);
        importB= (ImageButton) findViewById(R.id.importB);

        zpet.setColorFilter(Color.rgb(255,7,107));
        resetButton = (ImageButton) findViewById(R.id.reset);
        resetButton.setColorFilter(Color.rgb(18, 94, 188));
        pokracovat = (ImageButton)findViewById(R.id.pokracovat);
        pokracovat.setVisibility(View.INVISIBLE);

        timesArray = new TextView[pocetZavodniku];
        zavodniciArray = new EditText[pocetZavodniku];
        cisloZavodnikaArray = new EditText[pocetZavodniku];
        stopButtonArray = new ImageButton[pocetZavodniku];
        //odstraní řádky, které nepotřebujeme
        for(int i = 1; i<= pocetZavodniku; i++){
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

            casy = (TextView) findViewById(id1);
            casy.setText("00 : 00 : 00");
        }
        startTimerManyTimes();
        buttonReset(true);
        defaultcisloZavodnika();
        zavodniciEditable(true);
        odstran();
    }
    public void backButton(View view){ //při stisknutí červeného kříže otevře dialog
        goBackDialog();
    }

    public void hlavni() { //vrátí zpět na hlavní stránku
        Intent intent = new Intent(this, Hlavni.class);
        startActivity(intent);
    }
    public void goBackDialog(){ //dialog, zpět na hlavní stránku
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


    public void resetVysledky(View view) //resetuje stopky i s výsledky
    {
        AlertDialog.Builder resetAlert = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.MyAlertDialogTheme));
        resetAlert.setMessage("Opravdu chcete resetovat stopky i s výsledky?");
        resetAlert.setTitle("Resetovat stopky");
        resetAlert.setPositiveButton("Ano", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                    time = 0.0;
                    timerStarted = false;
                    zastavenych = 0;
                    textv.setEnabled(true);
                for (int k = 1; k <= pocetZavodniku; k++) {
                    TextView textView = timesArray[k - 1];
                    textView.setEnabled(true);
                    textView.setText("00 : 00 : 00");

                }
                pokracovat = (ImageButton) findViewById(R.id.pokracovat);
                pokracovat.setVisibility(View.INVISIBLE);
                startTimerManyTimes();
                zavodniciEditable(true);
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
    private void startTimerManyTimes(){ //definuje spouštěcí tlačítka
        int i = 1;
        for (ImageButton stopB : stopButtonArray){
            stopB.setImageResource(R.drawable.playimage_foreground);
            stopB.setColorFilter(Color.rgb(18, 94, 188));
            stopB.setEnabled(true);
            stopB.setTag("stop" +i);
            i++;
        }
    }
    public String getTag(String gtag){//získá tag
        casTag = gtag;
        return casTag;
    }
    public int getNumber(int i){ //získá počet závodníků
        pocetZavodniku = i;
        return pocetZavodniku;
    }
    //resetuje jména závodníků
    public void jmenaZavodnikuReset (View view){
        int i = 1;
        for (TextView textView : zavodniciArray) {
            textView.setEnabled(true);
            textView.setHint("Jméno závodníka");
            textView.setText(null);
            EditText et = cisloZavodnikaArray[i - 1];
            et.setText(Integer.toString(i));
            i++;
        }
    }
    public boolean isDarkMode(Context context) { //pokud je zaplý dark mode
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
    private void allZavodniciTextColor(){ //barva textu, pokud je dark mode
        int i = 1;
        for (EditText et2 : zavodniciArray) {
            EditText et = cisloZavodnikaArray[i - 1];
            et2.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
            et.setTextColor(isDarkMode(this)? Color.WHITE : Color.BLACK);
            i++;
        }
    }
    //odstrani přebytečné řádky, nechá jenom ty potřebné
    public void odstran() {
        int k = pocetZavodniku;
        int kolikrat = 20-k;

        int smaz = 20;
        for (int i = 0; i < kolikrat; i++)
        {
            Resources res = getResources();
            int id = res.getIdentifier("row" + smaz, "id", getPackageName());
            odstranRow = (TableRow) findViewById(id);
            odstranRow.setVisibility(View.GONE);
            k = k + 1;
            smaz = smaz -1;
        }
    }

    //při zastavení času u jenoho závodníka tlačítko a čas se vypnou
    public void startStop(View view) {
        zavodniciEditable(false);
        String tag = view.getTag().toString();
        for (int i = 1; i <= pocetZavodniku; i++){
            if(tag.equals("stop"+i)){
                resetButton.setEnabled(false);
                resetButton.setColorFilter(Color.GRAY);
                num = 20+i;
                TextView textv = timesArray[i-1];
                ImageButton stopB = stopButtonArray[i-1];
                stopB.setColorFilter(Color.RED);
                timers[i-1] = new MyTimer(textv, casTag);
                final int index = i-1;
                timers[index].start();
                stopB.setImageResource(R.drawable.stopimage_foreground);
                stopB.setTag("started" + i);
            }
            else if(tag.equals("started" +i)){
                String o = view.getTag().toString().replaceAll("[^0-9]+", "");
                int k = Integer.parseInt(o);
                System.out.println(k);
                ImageButton stopB = stopButtonArray[i-1];
                timers[k-1].stop();
                zastavenych = zastavenych + 1;
                stopB.setColorFilter(Color.GRAY);
                stopB.setEnabled(false);
            }}
        //pokud vypl všechny objeví se pokracovat
        if(pocetZavodniku == zastavenych)
        {
            resetButton.setEnabled(true);
            resetButton.setColorFilter(Color.rgb(18, 94, 188));
            pokracovat = (ImageButton) findViewById(R.id.pokracovat);
            pokracovat.setVisibility(View.VISIBLE);
            zpet.setEnabled(true);
            zpet.setColorFilter(Color.RED);
            importB.setEnabled(true);
            importB.setColorFilter(Color.rgb(18, 94, 188));
        }
    }
    //nastaví na začátku alespon cislo zavodnika
    private void defaultcisloZavodnika() {
        int i = 1;
        for (EditText et : cisloZavodnikaArray) {
            et.setText(Integer.toString(i));
            i++;
        }
    }

    private void buttonReset(boolean reset) { // resetuje časy
        int i = 0;
        for (ImageButton imgButton : stopButtonArray) {
            TextView et = timesArray[i];
            imgButton.setEnabled(reset);

            if (imgButton.isEnabled()) {
                imgButton.setColorFilter(Color.rgb(18, 94, 188));
            } else {
                imgButton.setColorFilter(Color.GRAY);
            }
            et.setText("00 : 00 : 00");
            zavodniciEditable(true);
            i++;
        }
    }

    public void zavodniciEditable(boolean edit) //závodníci budou moci být přepsáni
    {
        zavodnici.setEnabled(edit);
        importB.setEnabled(edit);


        if(edit){
            zpet.setEnabled(true);
            zpet.setColorFilter(Color.RED);
            zavodnici.setColorFilter(Color.rgb(18, 94, 188));
            importB.setColorFilter(Color.rgb(18, 94, 188));
        }
        else{
            zpet.setEnabled(false);
            zpet.setColorFilter(Color.GRAY);
            zavodnici.setColorFilter(Color.GRAY);
            importB.setColorFilter(Color.GRAY);
        }
        int i = 0;
        for (EditText et : zavodniciArray) {
            et.setEnabled(edit);
            EditText et2 = cisloZavodnikaArray[i];
            et2.setEnabled(edit);
            i++;
        }
    }
    public void openEd(){ //otevře stránku s editací
        pokracovat = (ImageButton) findViewById(R.id.pokracovat);
        Resources res = getResources();

        int row = 0;
        for (EditText zavodnici : zavodniciArray) {
            TextView cas = timesArray[row];
            EditText cisloZavodnika = cisloZavodnikaArray[row];

            newArray[row][0] = zavodnici.getText().toString();      //do 2D array uložení dat závodu
            newArray[row][1] = cas.getText().toString();
            newArray[row][2] = cisloZavodnika.getText().toString();
            row = row +1;
        }
        Editace e = new Editace();
        e.setArray(newArray);
        e.getTag(casTag);
        e.poc(pocetZavodniku);
        Intent intent = new Intent(this, Editace.class);
        startActivity(intent);
    }
    public void openEditace(View view){
        openEd();
    }
    public void upload(View view) { //načtení závodníků z databáze nebo z csv
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.selector);

        ImageButton nacteniZCsv = dialog.findViewById(R.id.importZCsv);

        ImageButton zDatabaze = dialog.findViewById(R.id.zdatabaze);
        zDatabaze.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Dialog zapsatDialog = new Dialog(Prubezny.this);
                zapsatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                zapsatDialog.setContentView(R.layout.load);

                Intent intent = new Intent(Prubezny.this, NacitaniZavodnikuZDatabaze.class);
                NacitaniZavodnikuZDatabaze ds = new NacitaniZavodnikuZDatabaze();
                ds.getNumber(pocetZavodniku);
                startActivityForResult(intent,REQUEST_CODE);
                dialog.cancel();}
        });
        nacteniZCsv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Dialog zapsatDialog = new Dialog(Prubezny.this);
                zapsatDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                zapsatDialog.setContentView(R.layout.load);
                launchFilePicker();
                dialog.cancel();
            }
        });
        dialog.show();

    }
    public void launchFilePicker() {
        // Create an intent for selecting a file via the file manager
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
//result z launch pickeru, pokud se zvolil soubor nebo jména z databáze, tak získá request code, který poté provede svoje
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            String[] names = resultData.getStringArrayExtra("selectedRows");
            View layout = findViewById(R.id.prubeznylayout);
            Resources res = getResources();
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
                        }
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Error reading file", Toast.LENGTH_SHORT).show();
                }}}
        else{
        }}}
