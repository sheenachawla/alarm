package com.example.sheenachawla.myapplication;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import android.content.Intent;
/**
 * Created by sheenachawla on 14/12/15.
 */
public class AddLabel extends Activity {
    AlarmDetails alarmDetails;
    Intent i ;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_label);
        final Intent intent = getIntent();
        alarmDetails = (AlarmDetails)intent.getSerializableExtra("object");
        final EditText editText = (EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.button);
        i = new Intent(AddLabel.this, AlarmActivity.class);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hello", editText.getText().toString());
                alarmDetails.setLabel(editText.getText().toString());
                i.putExtra("label", editText.getText().toString() );
                Toast.makeText(AddLabel.this, "Label saved!", Toast.LENGTH_SHORT).show();
                setResult(3, i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        i.putExtra("label", "Alarm" );
        setResult(3, i);
        AddLabel.this.finish();
    }
}
