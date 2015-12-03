package com.example.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.CountDownTimer;
import android.support.v4.view.MotionEventCompat;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

//TEST DIFFERENT DEVICES
//add sounds
//refine scoring
//icon with rounded corners
//change title (Add-On or AddOn or Add On)
public class MainActivity extends Activity {

    public static int global;

    private int tilesHit;

    private float[] tvX;
    private float[] tvY;
    private float[] xRange;
    private float[] yRange;
    private float tvLength;
    private float tvHeight;
    private int[] values;
    private boolean[] isHit;
    private boolean[] leftRange;
    private int level;
    private int totalScore;
    private int time;
    private int currentScore;
    private int greatestPath;
    private boolean pause;
    private float prevX, prevY;

    private TextView[] textViews;
    private TextView displayCurrentScore;
    private TextView displayTotalScore;
    private TextView displayLevel;
    private TextView displayTime;
    private TextView goal;

    private CountDownTimer countDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        displayCurrentScore = (TextView)findViewById(R.id.currentScore);
        displayTotalScore = (TextView) findViewById(R.id.totalScore);
        displayLevel = (TextView) findViewById(R.id.level);
        displayTime = (TextView) findViewById(R.id.time);
        goal = (TextView) findViewById(R.id.goal);
        TextView tv1 = (TextView)findViewById(R.id.tv1);
        TextView tv2 = (TextView)findViewById(R.id.tv2);
        TextView tv3 = (TextView)findViewById(R.id.tv3);
        TextView tv4 = (TextView)findViewById(R.id.tv4);
        TextView tv5 = (TextView)findViewById(R.id.tv5);
        TextView tv6 = (TextView)findViewById(R.id.tv6);
        TextView tv7 = (TextView)findViewById(R.id.tv7);
        TextView tv8 = (TextView)findViewById(R.id.tv8);
        TextView tv9 = (TextView)findViewById(R.id.tv9);
        TextView tv10 = (TextView)findViewById(R.id.tv10);
        TextView tv11 = (TextView)findViewById(R.id.tv11);
        TextView tv12 = (TextView)findViewById(R.id.tv12);
        TextView tv13 = (TextView)findViewById(R.id.tv13);
        TextView tv14 = (TextView)findViewById(R.id.tv14);
        TextView tv15 = (TextView)findViewById(R.id.tv15);
        TextView tv16 = (TextView)findViewById(R.id.tv16);
        TextView tv17 = (TextView)findViewById(R.id.tv17);
        TextView tv18 = (TextView)findViewById(R.id.tv18);
        TextView tv19 = (TextView)findViewById(R.id.tv19);
        TextView tv20 = (TextView)findViewById(R.id.tv20);
        TextView tv21 = (TextView)findViewById(R.id.tv21);
        TextView tv22 = (TextView)findViewById(R.id.tv22);
        TextView tv23 = (TextView)findViewById(R.id.tv23);
        TextView tv24 = (TextView)findViewById(R.id.tv24);
        TextView tv25 = (TextView)findViewById(R.id.tv25);

        textViews = new TextView[25];
        textViews[0] = tv1;
        textViews[1] = tv2;
        textViews[2] = tv3;
        textViews[3] = tv4;
        textViews[4] = tv5;
        textViews[5] = tv6;
        textViews[6] = tv7;
        textViews[7] = tv8;
        textViews[8] = tv9;
        textViews[9] = tv10;
        textViews[10] = tv11;
        textViews[11] = tv12;
        textViews[12] = tv13;
        textViews[13] = tv14;
        textViews[14] = tv15;
        textViews[15] = tv16;
        textViews[16] = tv17;
        textViews[17] = tv18;
        textViews[18] = tv19;
        textViews[19] = tv20;
        textViews[20] = tv21;
        textViews[21] = tv22;
        textViews[22] = tv23;
        textViews[23] = tv24;
        textViews[24] = tv25;

        GreatestPath g = new GreatestPath();

        Tile[][] tiles = g.getTiles();

        ArrayList<Integer> tempValues = new ArrayList<Integer>();

        for(int i = 0; i < tiles.length; i++){

            for(int j = 0; j < tiles[i].length; j++){

                tempValues.add(tiles[i][j].c.value);
                int temp = g.findGreatestPath(tiles[i][j]);
                if(temp > greatestPath){
                    greatestPath = temp;
                }

            }

        }
        values = new int[25];
        isHit = new boolean[25];
        tvX = new float[25];
        tvY = new float[25];
        xRange = new float[25];
        yRange = new float[25];
        leftRange = new boolean[25];

        level = 1;
        time = 16;
        pause = false;

        countDown = new CountDownTimer(16000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(!pause){
                    time--;
                    displayTime.setText("Time\n"+time);
                }
            }

            @Override
            public void onFinish() {
                gameOver();
            }
        };
        countDown.start();

        for(int i = 0; i < 25; i++){
            values[i] = tempValues.get(i);
            textViews[i].setText(Integer.toString(values[i]));
            textViews[i].setBackgroundResource(R.color.white);
            textViews[i].getBackground().setAlpha(300);
            isHit[i] = false;
        }

        displayCurrentScore.setTextSize(30);
        displayLevel.setText("Level\n0");
        displayTotalScore.setText("Score\n0");
        displayTime.setText("Time\n"+time);
        goal.setText("Goal: "+greatestPath);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        tvLength = textViews[0].getWidth();//textViews have same width,height
        tvHeight = textViews[0].getHeight();

        for(int i = 0; i < 25; i++){
            int[] temp = new int[2];
            textViews[i].getLocationOnScreen(temp);
            tvX[i] = temp[0];
            tvY[i] = temp[1];
            xRange[i] = tvX[i] + tvLength;
            yRange[i] = tvY[i] + tvHeight;
        }

        float eventX = event.getRawX();
        float eventY = event.getRawY();

        Resources r = getResources();
        float padding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics());//convert 15 dp to px

        switch(action) {

            case (MotionEvent.ACTION_DOWN) :

                for(int i = 0; i < 25; i++) {
                    if((eventX >= tvX[i]-padding && eventX <= xRange[i]+padding) && (eventY >= tvY[i]-padding && eventY <= yRange[i]+padding) && !isHit[i]) {
                        prevX = eventX;
                        prevY = eventY;
                        tileHit(i);
                    }
                }

                return true;

            case (MotionEvent.ACTION_MOVE) :

                if(tilesHit < 5){
                    for(int i = 0; i < 25; i++) {

                        //tile got hit for first time
                        if((eventX >= tvX[i]-padding && eventX <= xRange[i]+padding)
                                && (eventY >= tvY[i]-padding && eventY <= yRange[i]+padding)
                                && (!isHit[i])) {
                            //diagonal move was attempted
                            if(!(prevX >= tvX[i]-padding && prevX <= xRange[i]+padding)&&!(prevY >= tvY[i]-padding && prevY <= yRange[i]+padding)){//move vertical
                                resetTiles();
                            }else{
                                tileHit(i);
                                prevX = eventX;
                                prevY = eventY;
                            }

                        }
                        //dont allow overlap

                        //tile is hit, and finger moved somewhere else
                        if((!(eventX >= tvX[i]-padding && eventX <= xRange[i]+padding)
                                || !(eventY >= tvY[i]-padding && eventY <= yRange[i]+padding))
                                && isHit[i]
                                && !leftRange[i]){
                            leftRange[i] = true;
                        }
                        //finger moves back to a tile that was already hit
                        if((eventX >= tvX[i]-padding && eventX <= xRange[i]+padding)
                                && (eventY >= tvY[i]-padding && eventY <= yRange[i]+padding)
                                && isHit[i]
                                && leftRange[i]){
                            tilesHit = 10;
                        }
                    }
                }


                if(tilesHit == 10){
                    resetTiles();
                }
                return true;
            case (MotionEvent.ACTION_UP) :
                resetTiles();
                displayCurrentScore.setText("");
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(pause){
            pause = false;
            countDown = new CountDownTimer(time*1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if(!pause){
                        time--;
                        displayTime.setText("Time\n"+time);
                    }
                }

                @Override
                public void onFinish() {
                    gameOver();
                }
            };
            countDown.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        pause = true;
        countDown.cancel();

    }

    public void tileHit(int index){

        textViews[index].setBackgroundResource(R.color.purple);
        tilesHit++;
        isHit[index] = true;
        currentScore += Integer.parseInt(textViews[index].getText().toString());
        displayCurrentScore.setText(Integer.toString(currentScore));
        if(currentScore == greatestPath){
            generateNextLevel();
        }
    }

    public void resetTiles(){
        currentScore = 0;
        for(int i = 0; i < 25; i++){
            textViews[i].setBackgroundResource(R.color.white);
            isHit[i] = false;
            leftRange[i] = false;
        }
        tilesHit = 0;
    }

    public void generateNextLevel(){

        //playsound
        totalScore += currentScore;

        level++;
        global++;
        GreatestPath g = new GreatestPath();

        Tile[][] tiles = g.getTiles();

        ArrayList<Integer> tempValues = new ArrayList<Integer>();

        greatestPath = 0;

        for(int i = 0; i < tiles.length; i++){

            for(int j = 0; j < tiles[i].length; j++){

                tempValues.add(tiles[i][j].c.value);
                int temp = g.findGreatestPath(tiles[i][j]);
                if(temp > greatestPath){
                    greatestPath = temp;
                }

            }

        }
        values = new int[25];
        resetTiles();

        int tickNums;
        if(level >= 8){
            tickNums = 21000;
            time = 21;
        }else{
            time = 16;
            tickNums = 16000;
        }

        countDown.cancel();
        countDown = new CountDownTimer(tickNums, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(!pause){
                    time--;
                    displayTime.setText("Time\n"+time);
                }
            }

            @Override
            public void onFinish() {
                gameOver();
            }
        };
        countDown.start();

        for(int i = 0; i < 25; i++){
            values[i] = tempValues.get(i);
            textViews[i].setText(Integer.toString(values[i]));
            textViews[i].setBackgroundResource(R.color.white);
            isHit[i] = false;
        }

        goal.setText("Goal: " + greatestPath);
        displayTotalScore.setText("Score\n" + totalScore);
        displayLevel.setText("Level\n"+level);

    }

    public void gameOver(){

        if(time == 1){
            global = 0;
            try{
                int temp = Integer.parseInt(StaticMethods.readFirstLine("highScore2.txt",getBaseContext()));
                if(totalScore > temp){
                    StaticMethods.write("highScore2.txt",Integer.toString(totalScore),getBaseContext());
                    StaticMethods.write("level.txt",Integer.toString(level),getBaseContext());
                }
            }catch (IOException e){}
            Intent i = new Intent(getBaseContext(),GameOver.class);
            i.putExtra("score", totalScore);
            i.putExtra("level", level);
            startActivity(i);
        }

    }

}