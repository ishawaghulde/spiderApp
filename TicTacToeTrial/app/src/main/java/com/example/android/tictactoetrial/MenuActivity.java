package com.example.android.tictactoetrial;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity {


    LinearLayout linearLayout;
    ImageView restart;
    ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        linearLayout = findViewById(R.id.linearLayout);
        restart = (ImageView) findViewById(R.id.restart);
        back = (ImageView) findViewById(R.id.back);
        final BoardView boardView = new BoardView(this);
        linearLayout.addView(boardView);
        restart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                boardView.newGame();
            }
        });
        back.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
