package com.example.sheenachawla.myapplication;
import com.example.sheenachawla.myapplication.SimpleGestureFilter.SimpleGestureListener;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;
import android.view.WindowManager;
import  com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.Date;
import java.text.DateFormat;
import java.util.ArrayList;
/**
 * Created by sheenachawla on 14/12/15.
 */
public class SwipeScreen extends Activity implements SimpleGestureListener{
    private SimpleGestureFilter detector;
    Intent intent;
    private PendingIntent pendingIntent;
    SharedPreferences editor;
    SharedPreferences.Editor pref;
    AlarmManager alarmManager;
    ArrayList<AlarmDetails> alarmDetailsArrayList;
    Long timeinmili;
    MyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        TextView tv = (TextView) findViewById(R.id.textView);
        TextView time = (TextView) findViewById(R.id.textView6);
        time.setTextColor(Color.BLUE);
        time.setTypeface(null, Typeface.BOLD);
        time.setText(DateFormat.getDateTimeInstance().format(new Date()));
        editor = this.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        pref = editor.edit();
        String s = editor.getString("Alarm", "No Alarms Saved");
        Gson gson = new Gson();
        intent = getIntent();
        timeinmili = intent.getLongExtra("Time", 0);
        intent = new Intent(this, AlarmService.class);
        alarmDetailsArrayList = gson.fromJson(s,  new TypeToken<ArrayList<AlarmDetails>>(){}.getType() );
        detector = new SimpleGestureFilter(this,this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public  void onSwipe(int direction) {
        String str = "";

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int unique = (int)(timeinmili  & 0xfffffff);
        pendingIntent = PendingIntent.getService(this, unique, intent, 0);
        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";
                adapter = new MyAdapter(getBaseContext(), alarmDetailsArrayList);
                for(int i = 0; i<alarmDetailsArrayList.size(); i++){
                    if((alarmDetailsArrayList.get(i).getTimeMilli()).equals(timeinmili)){
                        alarmDetailsArrayList.get(i).setOnOff(false);
                        Gson gson = new Gson();
                        String j = gson.toJson(alarmDetailsArrayList);
                        pref.putString("Alarm", j);
                        pref.commit();
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
                alarmManager.cancel(pendingIntent);
                stopService(intent);
                this.finish();
                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                stopService(intent);
                this.finish();
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                stopService(intent);
                this.finish();
                break;

        }
        this.finish();
    }

    @Override
    public void onDoubleTap() {
        editor = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        intent = new Intent(this, AlarmService.class);
        stopService(intent);
        this.finish();
    }


}