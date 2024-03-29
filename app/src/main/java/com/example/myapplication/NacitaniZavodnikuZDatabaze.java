package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.UiModeManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class NacitaniZavodnikuZDatabaze extends AppCompatActivity {
    private boolean isToastDisplayed = false;
    private static final int READ_REQUEST_CODE = 42;
    private static final int REQUEST_CODE = 1;
    public static int maxSelections;
   public String[] names;
    @Override
    protected void onCreate(Bundle savedInstanceState) { //z databáze načte závodníky a umožní je vybírat
        super.onCreate(savedInstanceState);
        DatabazeZavodniku dbHelper = new DatabazeZavodniku(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final String tableName = "mytable";
        final String columnId = "_id";
        final String columnName = "name";

        String query = "SELECT * FROM " + tableName;
        final Cursor cursor = db.rawQuery(query, null);
        final List<String> selectedRows = new ArrayList<>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice ){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);

                TextView textView = view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.rgb(18, 94, 188));
                textView.setSingleLine();

                return view;
            }
        };
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(columnId));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(columnName));
                adapter.add(name);


            } while (cursor.moveToNext());
        }
        ListView listView = new ListView(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        listView.setBackgroundColor(isDarkMode(this)? Color.BLACK : this.getResources().getColor(R.color.lighterblue));
        listView.setAdapter(adapter);
        setContentView(listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listView.isItemChecked(position)) {
                    if (selectedRows.size() >= maxSelections) {
                        if (!isToastDisplayed) {
                            Toast.makeText(getApplicationContext(), "Už jste zvolili maximální počet", Toast.LENGTH_SHORT).show();
                            isToastDisplayed = true;
                        }
                        listView.setItemChecked(position, false);
                        return;
                    }
                }else {
                    selectedRows.remove(String.valueOf(position));
                }
                cursor.moveToPosition(position);
                @SuppressLint("Range") int rowId = cursor.getInt(cursor.getColumnIndex(columnId));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(columnName));

                if (listView.isItemChecked(position)) {
                    selectedRows.add(name);
                } else {
                    selectedRows.remove(name);
                }
            }
        });

        Button saveButton = new Button(this);
        saveButton.setText("Vybrat");
        saveButton.setTextColor((isDarkMode(this)? Color.WHITE : Color.BLACK));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names = new String[selectedRows.size()];
                int index = 0;
                for (String row : selectedRows) { //foreach
                    names[index] = row;
                    index++;
                }
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selectedRows", names);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        listView.addFooterView(saveButton);

    }
    public int getNumber(int i) { //maximální počet závodníků
        maxSelections = i;
        return maxSelections;
    }
    private boolean isDarkMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
    }

