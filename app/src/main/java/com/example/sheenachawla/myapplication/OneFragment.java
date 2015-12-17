package com.example.sheenachawla.myapplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sheenachawla.myapplication.R;

import java.text.DateFormat;
import java.util.Date;
/**
 * Created by sheenachawla on 12/12/15.
 */

public class OneFragment extends Fragment{

    public OneFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.layout, container, false);
        //TextView time = (TextView) v.findViewById(R.id.textView);
        //time.setTextColor(Color.BLACK);
        //time.setTypeface(null, Typeface.BOLD);
        //time.setText(DateFormat.getDateTimeInstance().format(new Date()));
        return v;
    }

}