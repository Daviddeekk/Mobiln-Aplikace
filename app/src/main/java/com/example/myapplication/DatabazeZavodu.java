package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

public class DatabazeZavodu extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "race_database.db";
    private static final int DATABASE_VERSION = 1;
    public DatabazeZavodu(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) { //vytvoří databázi pokud neexistuje
        String CREATE_RACE_TABLE = "CREATE TABLE race (id INTEGER PRIMARY KEY, name TEXT, time TEXT)";
        db.execSQL(CREATE_RACE_TABLE);
    }
    @Override //pokud existuje
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS race");
        onCreate(db);
    }
    public void displayAllData(LinearLayout layout) {//zobrazí data z databáze
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM race", null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));

            LinearLayout horizontalLayout = new LinearLayout(layout.getContext());
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setGravity(Gravity.CENTER_VERTICAL);
            horizontalLayout.setPadding(20, 26, 10, 16);
            LinearLayout.LayoutParams horizontalLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            horizontalLayout.setLayoutParams(horizontalLayoutParams);
            ImageView btn = new ImageView(layout.getContext());
            btn.setImageResource(android.R.drawable.ic_media_play);
            btn.setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.15f
            );
            btn.setLayoutParams(buttonParams);
            TextView textView2 = new TextView(layout.getContext());
            textView2.setText(time);
            textView2.setTextSize(14);
            textView2.setSingleLine(true);
            textView2.setHorizontallyScrolling(true);
            textView2.setEllipsize(TextUtils.TruncateAt.END);
            textView2.setTextColor(ContextCompat.getColor(layout.getContext(), isDarkMode(layout.getContext()) ? R.color.white : R.color.black));


            TextView textView = new TextView(layout.getContext());
            textView.setText(name);
            textView.setTextSize(20);
            textView.setSingleLine(true);
            textView.setHorizontallyScrolling(true);
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setTextColor(ContextCompat.getColor(layout.getContext(), isDarkMode(layout.getContext()) ? R.color.white : R.color.black));
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.5f
            );
            LinearLayout.LayoutParams textView2Params = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.35f
            );
            textViewParams.gravity = Gravity.CENTER_VERTICAL;
            textView.setLayoutParams(textViewParams);
            textView2.setLayoutParams(textView2Params);

            horizontalLayout.addView(textView);
            horizontalLayout.addView(textView2);
            horizontalLayout.addView(btn);
            horizontalLayout.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), VysledkyZavoduZDatabaze.class);
                intent.putExtra("id", id);
                v.getContext().startActivity(intent);
            });
            layout.addView(horizontalLayout);

            horizontalLayout.setOnLongClickListener(v -> {
                AlertDialog.Builder d = new AlertDialog.Builder(new ContextThemeWrapper(layout.getContext(), R.style.MyAlertDialogTheme));
                d.setMessage("Přejete si smazat vybraný závod?");
                d.setTitle("Smazat");
                d.setPositiveButton("Ano", (dialog, which) -> {
                    SQLiteDatabase writableDb = getWritableDatabase();
                    writableDb.delete("race", "id=?", new String[]{String.valueOf(id)});
                    writableDb.close();

                    DatabazeVysledkuZavodu racerDbHelper = new DatabazeVysledkuZavodu(layout.getContext());
                    SQLiteDatabase writableRacerDb = racerDbHelper.getWritableDatabase();
                    writableRacerDb.delete("racer", "race_id=?", new String[]{String.valueOf(id)});
                    writableRacerDb.close();

                    layout.removeView(horizontalLayout);
                });
                d.setNeutralButton("Ne", null);


                AlertDialog dialog = d.create();
                dialog.show();
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(layout.getContext(), R.color.white));
                dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(layout.getContext(), R.color.white));
                return true;
            });
        }

        cursor.close();
        db.close();
    }
    public boolean isDarkMode(Context context) { //pokud je dark mode
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
    public void zpet(View view) {


        Intent intent = new Intent(view.getContext(), Hlavni.class);
        view.getContext().startActivity(intent);
    }


}