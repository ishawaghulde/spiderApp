package com.example.android.tictactoetrial;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.os.CountDownTimer;

import java.util.Random;

public class BoardView extends View{
        private static final int LINE_THICK = 5;
        TextView turn;
        private static final int ELEMENT_MARGIN = 20;
        private static final int ELEMENT_STROKE_WIDTH = 15;
        private int width, height, elementWidth, elementHeight;
        private Paint gridPaint, symbolPaint, textPaint;
        private static final Random RANDOM = new Random();
        private char[][] boardChars;
        private String currentPlayer;
        private boolean ended;
        private char win = ' ';
        private boolean flag = false;
        private boolean timeListFlag = true;
        private boolean horFlag = false;
        private boolean verFlag = false;
        private boolean leftDiagFlag = false;
        private boolean rightDiagFlag = false;

        public int counter;
        private SharedPreferences prefs;
        private SharedPreferences.Editor editor;
        private int bestTime;
        DbHandler dbHandler;


    public BoardView(Context context) {
            super(context);
//            this.myCallback = myCallback;
            gridPaint = new Paint();
            symbolPaint = new Paint();
            symbolPaint.setAntiAlias(true);
            symbolPaint.setColor(Color.BLACK);
            symbolPaint.setStyle(Paint.Style.STROKE);
            symbolPaint.setStrokeWidth(ELEMENT_STROKE_WIDTH);
            textPaint = new Paint();
            textPaint.setColor(Color.BLACK);
            textPaint.setTextSize(64);

            boardChars = new char[3][3];
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    boardChars[i][j] = ' ';
                }
            }
            newGame();

            prefs = PreferenceManager.getDefaultSharedPreferences(context);
            editor = prefs.edit();
            dbHandler = new DbHandler(context);
        }



        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            width = View.MeasureSpec.getSize(widthMeasureSpec);
            height = View.MeasureSpec.getSize(heightMeasureSpec);
            elementWidth = (width - LINE_THICK) / 3;
            elementHeight = (height - LINE_THICK) / 3;

            setMeasuredDimension(width, height);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            drawGrid(canvas);
            drawBoard(canvas);
            drawTurn(canvas);
            drawTimer(canvas);
        }

        CountDownTimer countDownTimer =  new CountDownTimer(Long.MAX_VALUE, 1000){
            @Override
            public void onTick(long millisUntilFinished){
                if(win != 'T' && win!= 'X' && win!= 'O'){
                    invalidate();
                    counter++;
                }
                else{
                    if(timeListFlag){
                        String entry = String.valueOf(counter);

                        dbHandler.insertUserDetails(entry);

                        bestTime = prefs.getInt("time", 0);

                        if(counter<bestTime || bestTime == 0){
                            bestTime = counter;
                            prefs.edit().putInt("time", bestTime).apply();
                        }

                        timeListFlag = false;
                    }

                }
            }
            public void onFinish(){
                invalidate();
            }
        }.start();

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if(!isEnded() && event.getAction() == MotionEvent.ACTION_DOWN) {
                if(event.getY()>130) {
                    int x = (int) (event.getX() / elementWidth);
                    int y = (int) (event.getY() / elementHeight);
                    win = play(x, y);
                    invalidate();
                }

            }
            return super.onTouchEvent(event);
        }



        private void drawGrid(Canvas canvas) {
            for (int i = 0; i < 2; i++) {
                // vertical lines
                float left = elementWidth * (i + 1);
                float right = left + LINE_THICK;
                float top = 150;
                float bottom = height -150;

                canvas.drawRect(left, top, right, bottom, gridPaint);

                //horizintal lines
                float left2 = 50;
                float right2 = width - 50;
                float top2 = elementHeight * (i + 1);
                float bottom2 = top2 + LINE_THICK;

                canvas.drawRect(left2, top2, right2, bottom2, gridPaint);
            }
        }

        private void drawBoard(Canvas canvas) {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    drawElement(canvas,getChar(i, j), i, j);
                }
            }
        }

        private void drawTurn(Canvas canvas){
            String myString = "";
            if(win == ' ' || win == 'F' || !flag){
                myString = currentPlayer + "'s TURN";
                flag = true;
            }
            else if(win ==  'T'){
                myString = "IT'S A TIE!";
            }
            else{
                if(currentPlayer.equals("X")){
                    currentPlayer = "O";
                }
                else{
                    currentPlayer = "X";
                }
                myString = currentPlayer + " WINS!";
            }
            canvas.drawText(myString, 150,70,textPaint);

        }

        private void drawTimer(Canvas canvas){
            String timeString = String.valueOf(counter);
            canvas.drawText(timeString, 40,70,textPaint);
        }

        private void drawElement(Canvas canvas, char c, int x, int y) {
            if (c == 'O') {
                float cx = (elementWidth * x) + elementWidth / 2;
                float cy = (elementHeight * y) + elementHeight / 2;

                canvas.drawCircle(cx, cy, Math.min(elementWidth, elementHeight) / 2 - ELEMENT_MARGIN * 2, symbolPaint);
            } else if (c == 'X') {
                float startX = (elementWidth * x) + ELEMENT_MARGIN + (float) 0.25*elementWidth;
                float startY = (elementHeight * y) + ELEMENT_MARGIN + (float) 0.25*elementHeight;
                float endX =  (elementWidth * (x+1)) - ELEMENT_MARGIN - (float) 0.25*elementWidth;
                float endY = (elementHeight * (y+1)) - (float) 0.25*elementHeight;

                canvas.drawLine(startX, startY, endX, endY, symbolPaint);

                float startX2 = (elementWidth * (x + 1)) - ELEMENT_MARGIN - (float) 0.25*elementWidth;
                float startY2 = (elementHeight * y) + ELEMENT_MARGIN + (float) 0.25*elementHeight;
                float endX2 = (elementWidth * (x)) + ELEMENT_MARGIN + (float) 0.25*elementWidth;
                float endY2 = (elementHeight * (y+1)) - (float) 0.25*elementHeight;

                canvas.drawLine(startX2, startY2, endX2, endY2, symbolPaint);
            }
        }

        public boolean isEnded() {
            return ended;
        }



        public char play(int x, int y) {
            if(!ended && boardChars[x][y] == ' ') {
                if(currentPlayer.equals("X")){
                    boardChars[x][y] = 'X';
                    currentPlayer= "O";

                }
                else{
                    boardChars[x][y] = 'O';
                    currentPlayer = "X";
                }

            }
            return checkEnd();
        }

        public char getChar(int x, int y){

            return boardChars[x][y];
        }



        public void newGame() {
            for(int i = 0; i < 3; i++) {
                for(int j = 0; j<3; j++){
                    boardChars[i][j] = ' ';
                }
            }
            currentPlayer = "X";
            ended = false;
            flag = false;
            timeListFlag = true;
            currentPlayer = "X";
            counter = 0;
            horFlag = false;
            verFlag = false;
            leftDiagFlag= false;
            rightDiagFlag= false;
            invalidate();
        }

        public char checkEnd(){
            for(int i =0; i<3 ; i++){
                if(getChar(i,0) == getChar(i,1) && getChar(i,0) == getChar(i,2) && getChar(i,0)!= ' '){
                    ended = true;
                    horFlag = true;
                    return getChar(i,0);
                }

                if(getChar(0,i) == getChar(1,i) && getChar(0,i) == getChar(2,i) && getChar(0,i)!= ' '){
                    ended = true;
                    verFlag = true;
                    return getChar(0,i);
                }
            }
            if(getChar(0,0) == getChar(1,1) && getChar(0,0)==getChar(2,2) && getChar(0,0)!= ' '){
                ended = true;
                leftDiagFlag = true;
                return getChar(0,0);
            }
            if(getChar(0,2) == getChar(1,1) && getChar(0,2)==getChar(2,0) && getChar(0,2)!= ' '){
                ended = true;
                rightDiagFlag = true;
                return getChar(0,2);
            }
            for(int i =0; i<3; i++){
                for(int j =0; j<3; j++){
                    if(getChar(i,j) == ' '){
                        return 'F';
                    }
                }
            }
            return 'T';

        }
}
