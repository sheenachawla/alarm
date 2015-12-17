package com.example.sheenachawla.myapplication;

/**
 * Created by sheenachawla on 13/12/15.
 */
import java.util.ArrayList;
import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import android.util.Log;


public class MyAdapter extends BaseAdapter {
    private static ArrayList<AlarmDetails> alarmDetailsArrayList;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    Intent myIntent;
    private Context con;
    private LayoutInflater mInflater;
    private SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    public View convertView;
    private SharedPreferences preferences;
    Parcelable state;
    Long objTime;
    Calendar calendar;


    public MyAdapter(Context context, ArrayList<AlarmDetails> results) {
        alarmDetailsArrayList = results;
        mInflater = LayoutInflater.from(context);
        con = context;
    }

    public int getCount() {
        return alarmDetailsArrayList.size();
    }

    public Object getItem(int position) {

        return alarmDetailsArrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position,  View convertView, ViewGroup parent) {
        final  ViewHolder holder;
        preferences = con.getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row, null);
            holder = new ViewHolder();
            holder.txtName = (TextView) convertView.findViewById(R.id.text1);
            holder.txtLabel = (TextView) convertView
                    .findViewById(R.id.text2);
            holder.aSwitch = (Switch) convertView.findViewById(R.id.switch1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtName.setText(alarmDetailsArrayList.get(position).getName());
        holder.txtLabel.setText(alarmDetailsArrayList.get(position).getLabel());
        holder.aSwitch.setChecked(alarmDetailsArrayList.get(position).getOnOff());
        notifyDataSetChanged();
        holder.aSwitch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            preferences.registerOnSharedPreferenceChangeListener(prefListener);
            myIntent = new Intent(con, AlarmService.class);
            alarmDetailsArrayList.get(position).setOnOff(true);
            int unique = (int) (alarmDetailsArrayList.get(position).getTimeMilli() & 0xfffffff);
            if (((Switch) v).isChecked()) {
                Toast.makeText(con,
                        "Alarm on!", Toast.LENGTH_SHORT).show();


                objTime = alarmDetailsArrayList.get(position).getTimeMilli() - alarmDetailsArrayList.get(position).getTimeMilli()%(1000*60);
                Log.d("yo",alarmDetailsArrayList.get(position).getTimeMilli()+"" );
                Log.d("aha", System.currentTimeMillis()+"");
                myIntent.putExtra("snoozeflag", false);
                if (alarmDetailsArrayList.get(position).getTimeMilli() < System.currentTimeMillis()){
                    while(alarmDetailsArrayList.get(position).getTimeMilli() <= System.currentTimeMillis()){
                        calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(alarmDetailsArrayList.get(position).getTimeMilli());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        alarmDetailsArrayList.get(position).setTimeMilli(calendar.getTimeInMillis());
                        Log.d("time", calendar.getTimeInMillis()+"");
                    }


                    myIntent.putExtra("Time", calendar.getTimeInMillis());
                    unique = (int) (calendar.getTimeInMillis() & 0xfffffff);
                    pendingIntent = PendingIntent.getService(con, unique, myIntent, 0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, ((calendar.getTimeInMillis()-calendar.getTimeInMillis()%(1000*60))), pendingIntent);
                }
                else{
                    //unique = (int) (alarmDetailsArrayList.get(position).getTimeMilli() & 0xfffffff);
                    myIntent.putExtra("Time",alarmDetailsArrayList.get(position).getTimeMilli());
                    pendingIntent = PendingIntent.getService(con, unique, myIntent, 0);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, objTime , pendingIntent);
                }

            } else {
                pendingIntent = PendingIntent.getService(con, unique, myIntent, 0);
                alarmManager.cancel(pendingIntent);
                alarmDetailsArrayList.get(position).setOnOff(false);
                Toast.makeText(con,
                        "Alarm off!", Toast.LENGTH_SHORT).show();
            }

            holder.aSwitch.setChecked(alarmDetailsArrayList.get(position).getOnOff());
            Gson gson = new Gson();
            String s = gson.toJson(alarmDetailsArrayList);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("Alarm", s);
            editor.commit();
            notifyDataSetChanged();

            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView txtName;
        TextView txtLabel;
        Switch aSwitch;
    }
}