package com.example.sheenachawla.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import  android.widget.Toast;
import android.widget.ListView;
import android.view.MenuItem;
import android.widget.AdapterView.AdapterContextMenuInfo;
import  android.view.ContextMenu;
import android.widget.AdapterView;
import android.view.ContextMenu.ContextMenuInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sheenachawla on 12/12/15.
 */
public class TwoFragment extends Fragment{
    MyAdapter adapter;
    ArrayList<AlarmDetails> results;
    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    Intent myIntent;
    public SharedPreferences.OnSharedPreferenceChangeListener prefListener;
    ArrayList<AlarmDetails> searchResults;
    ListView lv;
    public TwoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences sharedpreferences;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_alarm, container, false);
        ImageButton imageButton = (ImageButton) v.findViewById(R.id.imageButton);
        final SharedPreferences editor = this.getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);

        ArrayList<String> a = new ArrayList<>();
        ArrayList<String> b = new ArrayList<>();
        a.add(editor.getString("alarm", "No Alarms Set"));
        b.add(editor.getString("alarm1", "Alarm"));
        String a1 = (editor.getString("alarm", "none"));
        lv = (ListView)v.findViewById(R.id.listView);
        editor.registerOnSharedPreferenceChangeListener(prefListener);
        String e = editor.getString("Alarm", "No Alarm Set");
        if(e.equals("No Alarm Set")){
            searchResults = new ArrayList<AlarmDetails>();
        }
        else{
            Gson gson = new Gson();
            searchResults = gson.fromJson(e, new TypeToken<ArrayList<AlarmDetails>>(){}.getType() );

        }
        adapter = new MyAdapter(getActivity(), searchResults);
        lv.setAdapter(adapter);
        prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {


            }
        };

        registerForContextMenu(lv);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addListenerOnButton(v);
            }
        });
        return v;
    }
    public void addListenerOnButton(View v) {
        Intent intent = new Intent(getActivity(), AlarmActivity.class);
        startActivityForResult(intent, 2);

    }
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            if(data!=null){
                String s = data.getStringExtra("Alarm");
                Gson gson = new Gson();
                searchResults = gson.fromJson(s,new TypeToken<ArrayList<AlarmDetails>>(){}.getType());
                lv.setAdapter(adapter);
            }

        }
    }
    protected boolean onLongListItemClick(View v, int pos, long id) {
        Log.i("list", "onLongListItemClick id=" + id);
        return true;
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Delete Alarm");
            menu.add("Yes");
            menu.add("No");
        }
    }
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Yes"){
            Toast.makeText(getActivity()," Alarm Deleted",Toast.LENGTH_LONG).show();
            AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
            SharedPreferences.Editor editor = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE).edit();
            alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            Gson gson = new Gson();
            SharedPreferences editor1 = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
            String s = editor1.getString("Alarm", "No Alarm Set");
            searchResults = gson.fromJson(s, new TypeToken<ArrayList<AlarmDetails>>() {
            }.getType());
            editor.remove("alarm");
            editor.remove("alarm1");
            editor.remove("timeinmilisec");

            myIntent = new Intent(getActivity(), AlarmService.class);
            pendingIntent = PendingIntent.getService(getActivity(), 0, myIntent, 0);

            Long time = searchResults.get(info.position).getTimeMilli();
            int unique = (int)(time  & 0xfffffff);
            pendingIntent = PendingIntent.getService(this.getActivity(), unique, myIntent, 0);
            alarmManager.cancel(pendingIntent);
            searchResults.remove(info.position);
            String si = gson.toJson(searchResults);
            editor.putString("Alarm", si);
            editor.commit();
            adapter = new MyAdapter(this.getContext(), searchResults);
            lv.setAdapter(adapter);

            alarmManager.cancel(pendingIntent);
        }
        else if(item.getTitle()=="No"){

        }else{
            return false;
        }
        return true;
    }


    }