package com.example.sheenachawla.myapplication;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import java.util.ArrayList;
import android.content.SharedPreferences;
import android.app.Activity;
import android.view.View;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.Button;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ArrayAdapter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Calendar;

/**
 * Created by sheenachawla on 13/12/15.
 */

public class AlarmActivity extends Activity {

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private TimePicker alarmTimePicker;
    private static AlarmActivity inst;
    private TextView alarmTextView;
    AlarmDetails alarmDetails;
    Intent myIntent;
    Intent serviceIntent;
    ListView listview;
    public  ArrayList<AlarmDetails> alarmDetailsArrayList;
    SharedPreferences sharedpreferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Boolean snoozeflag = false;
    String json,json1;
    Boolean objectflag = false;
    public SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    MyAdapter adapter;
    Calendar calendar;
    String alarmobj;
    AlarmDetails alarmDetails1;
    Boolean labelflag = false;
    String labels;
    Boolean onflag = false;
    Long alarm_time;
    Long current_time;
    int snooze = 0;


    public static AlarmActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm);
        alarmTimePicker = (TimePicker) findViewById(R.id.alarmTimePicker);
        alarmTextView = (TextView) findViewById(R.id.alarmText);
        Button alarmToggle = (Button) findViewById(R.id.alarmToggle);
        listview = (ListView) findViewById(R.id.listView2);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToggleClicked(v);
            }
        });
        String[] values = new String[]{"Label",
                "Snooze"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new ListClickHandler());
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        SharedPreferences e = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String alarm = e.getString("Alarm", "No Alarms Set");
        if(alarm.equals("No Alarms Set")){
            alarmDetailsArrayList =  new ArrayList<AlarmDetails>();
        }
        else{
            Gson gson = new Gson();

            alarmDetailsArrayList = gson.fromJson(alarm, new TypeToken<ArrayList<AlarmDetails>>(){}.getType() );
        }
        alarmDetails = new AlarmDetails();
        String time = (calendar.getTime().toString().split(" ", 5)[3]).split(":",2)[0]+ ":"+(calendar.getTime().toString().split(" ", 5)[3]).split(":",3)[1];
        alarmDetails.setName(time);
        String a = String.valueOf(calendar.getTimeInMillis());
        Long time1 = calendar.getTimeInMillis() - (calendar.getTimeInMillis()%(1000*60));
        String y = String.valueOf(time1);
        alarmDetails.setTimeMilli(calendar.getTimeInMillis());
        alarmDetails.setOnOff(true);

    }




    public void onToggleClicked(View view) {
        alarmDetails1 = new AlarmDetails();
        Intent intentLabel = getIntent();
        String label = intentLabel.getStringExtra("label");
        if (labelflag){
            alarmDetails.setLabel(labels);
        }
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.getCurrentHour());
        calendar.set(Calendar.MINUTE, alarmTimePicker.getCurrentMinute());
        int uniqueInt = (int) (calendar.getTimeInMillis() & 0xfffffff);
        myIntent = new Intent(AlarmActivity.this, AlarmService.class);
        String strLong = Long.toString(calendar.getTimeInMillis());
        String time = (calendar.getTime().toString().split(" ", 5)[3]).split(":",2)[0]+ ":"+(calendar.getTime().toString().split(" ", 5)[3]).split(":",3)[1];
        if (calendar.getTimeInMillis() < System.currentTimeMillis()){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        alarmDetails.setName(time);
        alarmDetails.setTimeMilli(calendar.getTimeInMillis());
        alarmDetails.setOnOff(true);

        editor.putString("alarm", time);
        editor.putLong("timeinmilisec", calendar.getTimeInMillis());
        editor.putBoolean("value", true);


            objectflag = true;

        for(int i = 0; i<alarmDetailsArrayList.size(); i++){
            alarm_time = alarmDetailsArrayList.get(i).getTimeMilli()- (alarmDetailsArrayList.get(i).getTimeMilli()%(1000*60));
            current_time = calendar.getTimeInMillis()-(calendar.getTimeInMillis()%(1000*60));
            if(alarm_time.equals(current_time)){
                onflag = true;
                alarmDetails.setOnOff(false);
                Toast.makeText(getBaseContext(),
                        "Alarm at this time is already set!", Toast.LENGTH_LONG).show();
            }

        }



    if(!onflag){
        alarmDetailsArrayList.add(alarmDetails);
        adapter = new MyAdapter(getBaseContext(), alarmDetailsArrayList);
        Gson gson = new Gson();
        json1 = gson.toJson(alarmDetails);
        json = gson.toJson(alarmDetailsArrayList);
        editor.putString("Alarm", json);
        editor.commit();
        adapter.notifyDataSetChanged();
        String timeMili = String.valueOf(calendar.getTimeInMillis());
        myIntent.putExtra("Alarm", json);
        myIntent.putExtra("snoozeflag", snoozeflag);
        myIntent.putExtra("Time", calendar.getTimeInMillis());
        pendingIntent = PendingIntent.getService(this, uniqueInt, myIntent, 0);
        if(snoozeflag){
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % (1000*60))), 2*60*1000, pendingIntent);
        }
        else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, (calendar.getTimeInMillis() - (calendar.getTimeInMillis()%(1000*60))), pendingIntent);
        }
        Toast.makeText(this,
                "Alarm is set!", Toast.LENGTH_SHORT).show();
    }

            setResult(2, myIntent);
            finish();
    }

    public void setAlarmText(String alarmText) {
        alarmTextView.setText(alarmText);
    }

    public class ListClickHandler implements OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3) {
            Intent intent;
                switch (position) {
                    case 0:
                        intent = new Intent(AlarmActivity.this, AddLabel.class);
                        intent.putExtra("object", alarmDetails);
                        labelflag = true;
                        startActivityForResult(intent, 3);
                        break;
                    case 1:
                        alarmDetails.setSnooze(true);
                        snoozeflag = true;
                        Toast.makeText(getBaseContext(),
                                "Alarm Snoozed!", Toast.LENGTH_SHORT).show();
                        break;

                }
        }

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==3)
        {
            labels = data.getStringExtra("label");

        }
    }
    @Override
    public void onBackPressed() {
        setResult(2, myIntent);
        AlarmActivity.this.finish();
    }

}

