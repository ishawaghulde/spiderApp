package com.example.android.tictactoetrial;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.util.Log;

import java.util.Random;

public class MainActivity extends AppCompatActivity  {
    Button play;
    Button bestTime;
    Button timeList;

        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (Button) findViewById(R.id.play);
        bestTime = (Button) findViewById(R.id.bestTime);
        timeList = (Button) findViewById(R.id.timeList);
        play.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent1 = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent1);
            }
        });

        bestTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent2 = new Intent(MainActivity.this, FinalActivity.class);
                startActivity(intent2);
            }
        });

        timeList.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent3 = new Intent(MainActivity.this, DetailsActivity.class);
                startActivity(intent3);
            }
        });
    }

}



