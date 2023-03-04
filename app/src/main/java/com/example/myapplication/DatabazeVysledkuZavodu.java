package com.example.myapplication;
import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DatabazeVysledkuZavodu extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "racer_database.db";
    private static final int DATABASE_VERSION = 1;

    public DatabazeVysledkuZavodu(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RACER_TABLE = "CREATE TABLE racer (id INTEGER PRIMARY KEY, racer_name TEXT, position TEXT, racer_number TEXT, racer_time TEXT, race_id INTEGER, FOREIGN KEY(race_id) REFERENCES race(id))";
        db.execSQL(CREATE_RACER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS racer");
        onCreate(db);
    }
    public void displayDataByForeignId(LinearLayout layout, int foreignId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"position", "racer_name", "racer_number", "racer_time"}; // replace with your column names
        String selection = "race_id = ?";
        String[] selectionArgs = {String.valueOf(foreignId)};
        Cursor cursor = db.query("racer", columns, selection, selectionArgs, null, null, null);

        while (cursor.moveToNext()) {
            // get values from cursor
            @SuppressLint("Range") String pozice = cursor.getString(cursor.getColumnIndex("position"));
            @SuppressLint("Range") String jmeno = cursor.getString(cursor.getColumnIndex("racer_name"));
            @SuppressLint("Range") String cisloZavodnika = cursor.getString(cursor.getColumnIndex("racer_number"));
            @SuppressLint("Range") String cas = cursor.getString(cursor.getColumnIndex("racer_time"));

            // create a new row
            LinearLayout rowLayout = new LinearLayout(layout.getContext());
            rowLayout.setOrientation(LinearLayout.HORIZONTAL);
            rowLayout.setGravity(Gravity.CENTER_VERTICAL);
            rowLayout.setPadding(20, 16, 0, 16);
            LinearLayout.LayoutParams rowLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            rowLayout.setLayoutParams(rowLayoutParams);

            // create four text views to hold the values
            TextView column1TextView = new TextView(layout.getContext());
            LinearLayout.LayoutParams column1TextViewParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.2f
            );
            column1TextView.setLayoutParams(column1TextViewParams);
            column1TextView.setText(String.valueOf(pozice));
            column1TextView.setTextSize(20);
            column1TextView.setSingleLine(true);
            column1TextView.setGravity(Gravity.CENTER);

            column1TextView.setTextColor(isDarkMode(layout.getContext())? Color.WHITE : Color.BLACK);

            TextView column2TextView = new TextView(layout.getContext());
            LinearLayout.LayoutParams column2TextViewParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.3f
            );
            column2TextView.setLayoutParams(column2TextViewParams);
            column2TextView.setText(jmeno);
            column2TextView.setTextSize(20);
            column2TextView.setSingleLine(true);
            column2TextView.setHorizontallyScrolling(true);
            column2TextView.setMovementMethod(new ScrollingMovementMethod());
            column2TextView.setTextColor(isDarkMode(layout.getContext())? Color.WHITE : Color.BLACK);

            TextView column3TextView = new TextView(layout.getContext());
            LinearLayout.LayoutParams column3TextViewParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.15f
            );

            column3TextView.setLayoutParams(column3TextViewParams);
            column3TextView.setText(cisloZavodnika);
            column3TextView.setTextSize(20);
            column3TextView.setSingleLine(true);
            column3TextView.setGravity(Gravity.CENTER);

            column3TextView.setTextColor(isDarkMode(layout.getContext())? Color.WHITE : Color.BLACK);

            TextView column4TextView = new TextView(layout.getContext());
            LinearLayout.LayoutParams column4TextViewParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.35f
            );
            column4TextView.setLayoutParams(column4TextViewParams);
            column4TextView.setText(String.valueOf(cas));
            column4TextView.setTextSize(20);
            column4TextView.setSingleLine(true);
            column4TextView.setGravity(Gravity.CENTER);
            column4TextView.setTextColor(isDarkMode(layout.getContext())? Color.WHITE : Color.BLACK);

            // add the text views to the row layout
            rowLayout.addView(column1TextView);
            rowLayout.addView(column2TextView);
            rowLayout.addView(column3TextView);
            rowLayout.addView(column4TextView);

            // add the row layout to the parent layout
            layout.addView(rowLayout);
        }

// close the cursor and the database connection
        cursor.close();
        db.close();
    }
    public boolean isDarkMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
}
