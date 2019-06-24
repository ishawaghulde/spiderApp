package com.example.android.tictactoetrial;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class FinalActivity extends AppCompatActivity {
    TextView time;
    int bestTime = 0;
    SharedPreferences prefs;
    ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        time = findViewById(R.id.time);
        back = (ImageView) findViewById(R.id.back);

        prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        bestTime = prefs.getInt("time", 0);
        String text = String.valueOf(bestTime) + " seconds";
        time.setText(text);
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(FinalActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
