package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.UiModeManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "mydatabase.db";
    private static final int DATABASE_VERSION = 1;

    // Define your database schema here
    private static final String TABLE_NAME = "mytable";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle any necessary upgrades to the database schema
    }

    public void addData(String name) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public ArrayList<String> getAllData() {
        ArrayList<String> dataList = new ArrayList<String>();
        SQLiteDatabase db = getReadableDatabase();

        String[] columns = { COLUMN_ID, COLUMN_NAME };
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            dataList.add(id + ": " + name);
        }

        cursor.close();
        db.close();

        return dataList;
    }
    public void displayAllData(LinearLayout layout) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = { COLUMN_ID, COLUMN_NAME };
        Cursor cursor = db.query(TABLE_NAME, columns, null, null, null, null, null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));

            LinearLayout horizontalLayout = new LinearLayout(layout.getContext());
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setOrientation(LinearLayout.HORIZONTAL);
            horizontalLayout.setGravity(Gravity.CENTER_VERTICAL);
            horizontalLayout.setPadding(0, 16, 0, 16);


            LinearLayout.LayoutParams horizontalLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            horizontalLayout.setLayoutParams(horizontalLayoutParams);

           TextView textView = new TextView(layout.getContext());


            LinearLayout.LayoutParams textViewParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.7f);
            textView.setLayoutParams(textViewParams);
            textView.setText("  "+name);
            textView.setTextSize(25);
            textView.setSingleLine();


            Button delete = new Button(layout.getContext());
            LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    0.3f
            );

            TypedValue typedValue = new TypedValue();

            int textColor = typedValue.data;

            delete.setText("odstranit");
            delete.setTextColor(isDarkMode(layout.getContext())? Color.WHITE : Color.BLACK);
            delete.setBackgroundColor(Color.TRANSPARENT);

            buttonParams.gravity = Gravity.END;
            delete.setLayoutParams(buttonParams);

            horizontalLayout.addView(textView);
            horizontalLayout.addView(delete);
            layout.addView(horizontalLayout);

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SQLiteDatabase db = getWritableDatabase();
                    db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[] { String.valueOf(id) });
                    db.close();

                    layout.removeView(horizontalLayout);
                }
            });

        }

        cursor.close();
        db.close();
    }
    public boolean isDarkMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }

}
