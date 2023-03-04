package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
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
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RACE_TABLE = "CREATE TABLE race (id INTEGER PRIMARY KEY, name TEXT, time TEXT)";
        db.execSQL(CREATE_RACE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS race");
        onCreate(db);
    }
    public void displayAllData(LinearLayout layout) {
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

            TextView textView = new TextView(layout.getContext());
            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            textView.setLayoutParams(textViewParams);
            textView.setText( name + "    " + time);
            textView.setTextSize(20);
            textView.setSingleLine(true);
            textView.setHorizontallyScrolling(true);
            textView.setMovementMethod(new ScrollingMovementMethod());
            textView.setTextColor(isDarkMode(layout.getContext())? Color.WHITE : Color.BLACK);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(view.getContext(), VysledkyZavoduZDatabaze.class);
                    intent.putExtra("id", id);
                    view.getContext().startActivity(intent);
                }
            });
            textView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v) {

                        AlertDialog.Builder resetAlert = new AlertDialog.Builder(new ContextThemeWrapper(layout.getContext(), R.style.MyAlertDialogTheme));
                        resetAlert.setMessage("Přejete si smazat vybraný závod?");
                        resetAlert.setTitle("Smazat");
                        resetAlert.setPositiveButton("Ano", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                SQLiteDatabase db = getWritableDatabase();
                                db.delete("race", "id=?", new String[] { String.valueOf(id) });
                                DatabazeVysledkuZavodu racerDbHelper = new DatabazeVysledkuZavodu(layout.getContext());
                                SQLiteDatabase dbs = racerDbHelper.getWritableDatabase();
                                dbs.delete("racer", "race_id=?", new String[] { String.valueOf(id) });
                                db.close();
                                dbs.close();
                                layout.removeView(horizontalLayout);
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
                        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(layout.getContext(), R.color.white));
                        dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(layout.getContext(), R.color.white));

                    return true;
                }


            });


// Start the animation




            horizontalLayout.addView(textView);

            layout.addView(horizontalLayout);

           /*delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SQLiteDatabase db = getWritableDatabase();
                    db.delete("race", "id=?", new String[] { String.valueOf(id) });
                    db.close();
                    layout.removeView(horizontalLayout);
                }
            });*/
        }

        cursor.close();
        db.close();
    }
    public boolean isDarkMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
    public void zpet(View view) {
        SQLiteDatabase db = getReadableDatabase();
        // do something with the database
        Intent intent = new Intent(view.getContext(), Hlavni.class);
        view.getContext().startActivity(intent);
    }


}