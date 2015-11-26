package com.example.dave.sumrun;

import android.app.Activity;
import android.content.res.Resources;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends Activity {

    private int tilesHit;

    private float[] tvX;
    private float[] tvY;
    private float[] xRange;
    private float[] yRange;
    private float tvLength;
    private float tvHeight;
    private int[] values;
    private boolean[] isHit;
    private int level;
    private int totalScore;
    private int time;
    private int currentScore;
    private int greatestPath;

    private TextView[] textViews;
    private TextView display;
    private TextView displayTotalScore;
    private TextView displayLevel;
    private TextView displayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = (TextView)findViewById(R.id.display);
        displayTotalScore = (TextView) findViewById(R.id.totalScore);
        displayLevel = (TextView) findViewById(R.id.level);
        displayTime = (TextView) findViewById(R.id.time);
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

        g.printTiles();

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

        for(int i = 0; i < 25; i++){
            values[i] = tempValues.get(i);
            textViews[i].setText(Integer.toString(values[i]));
            textViews[i].setBackgroundResource(R.color.white);
            isHit[i] = false;
        }
        displayLevel.setText("Level\n0");
        displayTotalScore.setText("Score\n0");
        displayTime.setText("Time\n0");
        display.setText("0");

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

        //////////////////////CANT BACKTRACK,ONLY LOSE WHEN TIME RUNS OUT. MUST RELEASE AFTER PATH OF 5 HAS BEEN REACHED////////////////////////////////
        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                for(int i = 0; i < 25; i++) {
                    if((eventX >= tvX[i]-padding && eventX <= xRange[i]+padding) && (eventY >= tvY[i]-padding && eventY <= yRange[i]+padding) && !isHit[i]) {
                        textViews[i].setBackgroundResource(R.color.red);
                        tilesHit++;
                        isHit[i] = true;
                    }
                }
                if(tilesHit > 5){
                    for(int i = 0; i < 25; i++){
                        textViews[i].setBackgroundResource(R.color.white);
                        isHit[i] = false;
                    }
                    tilesHit = 0;
                }
                return true;

            case (MotionEvent.ACTION_MOVE) :
                for(int i = 0; i < 25; i++) {

                    if((eventX >= tvX[i]-padding && eventX <= xRange[i]+padding) && (eventY >= tvY[i]-padding && eventY <= yRange[i]+padding) && !isHit[i]) {
                        textViews[i].setBackgroundResource(R.color.red);
                        tilesHit++;
                        isHit[i] = true;
                    }
                }
                if(tilesHit > 5){
                    for(int i = 0; i < 25; i++){
                        textViews[i].setBackgroundResource(R.color.white);
                        isHit[i] = false;
                    }
                    tilesHit = 0;
                }

                return true;
            case (MotionEvent.ACTION_UP) :
                for(int i = 0; i < 25; i++){
                    textViews[i].setBackgroundResource(R.color.white);
                    isHit[i] = false;
                    tilesHit = 0;
                }
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

}