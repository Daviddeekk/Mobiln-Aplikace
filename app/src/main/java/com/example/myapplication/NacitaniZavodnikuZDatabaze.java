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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabazeZavodniku dbHelper = new DatabazeZavodniku(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final String tableName = "mytable";
        final String columnId = "_id";
        final String columnName = "name";

        // Retrieve all the rows from the database table
        String query = "SELECT * FROM " + tableName;

        final Cursor cursor = db.rawQuery(query, null);
        System.out.println(cursor.getCount());

        // Create a list to store the selected rows
        final List<String> selectedRows = new ArrayList<>();

        // Create an array adapter to display the rows in the ListView
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice ){

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the view from the parent class
                View view = super.getView(position, convertView, parent);

                // Set the text color of the item view
                TextView textView = view.findViewById(android.R.id.text1);

                textView.setTextColor(Color.rgb(18, 94, 188));
                textView.setSingleLine();

                return view;
            }
        };

        // Add each row's data to the adapter
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(columnId));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(columnName));
                adapter.add(name);


            } while (cursor.moveToNext());
        }
        // Set the adapter on the ListView
        ListView listView = new ListView(this);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // Set choice mode to multiple

        listView.setBackgroundColor(isDarkMode(this)? Color.BLACK : this.getResources().getColor(R.color.lighterblue));
        listView.setAdapter(adapter);
          //  listView.setBackgroundColor(Color.GRAY);
        setContentView(listView);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        // Add an onItemClickListener to handle row selections and deselections
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Check if the maximum number of rows has already been selected
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
                    // Remove the position value from the selectedRows list
                    selectedRows.remove(String.valueOf(position));
                }


                // Get the row data from the cursor
                cursor.moveToPosition(position);
                @SuppressLint("Range") int rowId = cursor.getInt(cursor.getColumnIndex(columnId));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(columnName));


                // Add or remove the selected row from the selectedRows list
                if (listView.isItemChecked(position)) {
                    selectedRows.add(name);
                } else {
                    selectedRows.remove(name);
                }
            }
        });

        // Set a button to save the selected rows and return to the previous view
        Button saveButton = new Button(this);
        saveButton.setText("Vybrat");
        saveButton.setTextColor((isDarkMode(this)? Color.WHITE : Color.BLACK));
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                names = new String[selectedRows.size()];
                int index = 0;
                for (String row : selectedRows) {
                    String[] parts = row.split(" - ");
                    String name = parts[0];
                    names[index] = name;
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
    public int getNumber(int i) {
        maxSelections = i;
        return maxSelections;
    }
    private boolean isDarkMode(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
        return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
    }
    }

