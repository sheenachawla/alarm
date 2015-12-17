package com.example.sheenachawla.myapplication;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by sheenachawla on 13/12/15.
 */
public class AlarmService extends Service {
    private static final String TAG = "AlarmService";
    private NotificationManager alarmNotificationManager;
    SwipeScreen swipeScreen;
    Long time;
    Ringtone ringtone;
    SharedPreferences sharedpreferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public  ArrayList<AlarmDetails> alarmDetailsArrayList;
    MyAdapter adapter;
    Boolean snoozeflag;
    public AlarmService() {
        super();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,intent.getLongExtra("Time",0)+"");
        time = intent.getLongExtra("Time", 0);
        snoozeflag = intent.getBooleanExtra("snoozeflag", false);
        if (time >= System.currentTimeMillis() || snoozeflag){
            sendNotification("hi");
        }
        /*else{
            Gson gson = new Gson();
            sharedpreferences = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            String s = sharedpreferences.getString("Alarm","No Alarm Set");
            alarmDetailsArrayList = gson.fromJson(s,new TypeToken<ArrayList<AlarmDetails>>(){}.getType());
            for(int i = 0; i<alarmDetailsArrayList.size();i++){
                if(alarmDetailsArrayList.get(i).getTimeMilli().equals(time)){
                    alarmDetailsArrayList.get(i).setOnOff(false);
                    adapter = new MyAdapter(getBaseContext(), alarmDetailsArrayList);
                    String json = gson.toJson(alarmDetailsArrayList);
                    sharedpreferences.edit().putString("Alarm", json);
                    sharedpreferences.edit().commit();
                    adapter.notifyDataSetChanged();
                }
            }

        }*/
        return super.onStartCommand(intent, flags, startId);
    }

    private void sendNotification(String msg) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        setAlarmRingtone(this);
        Log.d("AlarmService", "Notification sent.");
    }

    public void setAlarmRingtone(Context context){
        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();
        Intent dialogIntent = new Intent(this, SwipeScreen.class);
        dialogIntent.putExtra("Time", time);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(dialogIntent);

    }

    @Override
    public void onDestroy() {
        ringtone.stop();

        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
