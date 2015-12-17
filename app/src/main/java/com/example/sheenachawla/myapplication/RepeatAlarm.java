package com.example.sheenachawla.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.view.View;
import android.widget.Toolbar;
import android.widget.AdapterView.OnItemClickListener;


/**
 * Created by sheenachawla on 12/12/15.
 */
public class RepeatAlarm extends Activity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.days);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ListView listview = (ListView) findViewById(R.id.listView3);
        String[] days = new String[]{"Monday", "Tuesday",
                "Wednesday", "Thursday", "Friday", "Saturday",
                "Sunday"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_checked, android.R.id.text1, days);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                CheckedTextView check = (CheckedTextView) view;
                check.setChecked(!check.isChecked());

            }
        });

    }
}
