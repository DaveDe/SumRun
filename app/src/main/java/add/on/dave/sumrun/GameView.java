package add.on.dave.sumrun;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.view.MotionEventCompat;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.on.dave.sumrun.R;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

import java.io.IOException;
import java.util.ArrayList;

public class GameView extends Activity {

    public static int global;
    public static boolean isMuted;
    public static final String PREFS_NAME_GAME = "game_data3";

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
    private boolean gameOver;
    private boolean restore;
    private boolean firstTime;
    private int level;
    private int totalScore;
    private int time;
    private int currentScore;
    private int greatestPath;
    private int soundID1;
    private int soundID2;
    private int soundID3;
    private int soundID4;
    private int soundID5;
    private int soundID6;
    private int soundID7;
    private int soundID8;
    private int soundID9;
    private int soundID10;
    private int numBoxes;
    private int longestPath;
    private float prevX, prevY, volume;
    private String mode;
    private String gridSize;
    private String theme;

    private ViewFlipper vf;
    private RelativeLayout rl;
    private TextView[] textViews;
    private TextView displayCurrentScore;
    private TextView displayTotalScore;
    private TextView displayLevel;
    private TextView displayTime;
    private TextView goal;
    private TextView displayMode;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private TextView tv8;
    private TextView tv9;
    private TextView tv10;
    private TextView tv11;
    private TextView tv12;
    private TextView tv13;
    private TextView tv14;
    private TextView tv15;
    private TextView tv16;
    private TextView tv17;
    private TextView tv18;
    private TextView tv19;
    private TextView tv20;
    private TextView tv21;
    private TextView tv22;
    private TextView tv23;
    private TextView tv24;
    private TextView tv25;

    private Button menuButton;

    private CountDownTimer countDown;
    private SoundPool soundPool;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StartAppSDK.init(this, "200144513", false);

        settings = getSharedPreferences(PREFS_NAME_GAME, 0);
        editor = settings.edit();

        setContentView(R.layout.grids);

        //restore values if returning to game
        mode = settings.getString("mode", "Classic");

        level = settings.getInt("level",1);
        time = settings.getInt("time", 8);
        totalScore = settings.getInt("score", 0);
        global = settings.getInt("global", 0);


        vf = (ViewFlipper) findViewById(R.id.viewFlipper);
        //set different layout based on gridsize
        setLayout();

        gameOver = settings.getBoolean("gameOver",false);
        firstTime = settings.getBoolean("firstTime",true);

        if(!gameOver && !firstTime){
            restore = true;
        }else{
            restore = false;
        }

        gameOver = false;
        if(firstTime){
            editor.putBoolean("firstTime",false);
            editor.putBoolean("restore", false);
        }

        //restore values only if comming from menu (writes appropriate restore to sharedpreferences)
        restoreValues();

        displayMode.setText("Mode: "+mode);
        displayLevel.setText("Level\n   "+level);
        displayTotalScore.setText("Score\n    "+totalScore);
        if(mode.equals("Sudden Death")){
            displayTime.setText("-");
        }else if(mode.equals("Nightmare")){
            displayTime.setText("?");
        }else{
            displayTime.setText("" + time);
        }

        values = new int[numBoxes];
        isHit = new boolean[numBoxes];
        tvX = new float[numBoxes];
        tvY = new float[numBoxes];
        xRange = new float[numBoxes];
        yRange = new float[numBoxes];
        leftRange = new boolean[numBoxes];

        greatestPath = calculateGreatestPath(gridSize);//also saves matrix in values[]

        if(mode.equals("Classic") || mode.equals("Blitz")){
            goal.setText("Objective: "+greatestPath);
        }

        initializeSoundPool();

        soundID1 = soundPool.load(this, R.raw.one, 1);
        soundID2 = soundPool.load(this, R.raw.two, 1);
        soundID3 = soundPool.load(this, R.raw.three, 1);
        soundID4 = soundPool.load(this, R.raw.four, 1);
        soundID5 = soundPool.load(this, R.raw.five, 1);
        soundID6 = soundPool.load(this, R.raw.loss, 1);
        soundID7 = soundPool.load(this, R.raw.win, 1);
        soundID8 = soundPool.load(this, R.raw.tick, 1);
        soundID9 = soundPool.load(this, R.raw.cluck, 1);
        soundID10 = soundPool.load(this, R.raw.owl_hoot, 1);

        AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volume = curVolume/maxVolume;


        for(int i = 0; i < numBoxes; i++){
            textViews[i].setText(Integer.toString(values[i]));
            textViews[i].setBackgroundResource(R.color.white);
            textViews[i].getBackground().setAlpha(300);
            isHit[i] = false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        int action = MotionEventCompat.getActionMasked(event);

        tvLength = textViews[0].getWidth();//textViews have same width,height
        tvHeight = textViews[0].getHeight();

        for(int i = 0; i < numBoxes; i++){
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

                for(int i = 0; i < numBoxes; i++) {
                    if((eventX >= tvX[i]-padding && eventX <= xRange[i]+padding) && (eventY >= tvY[i]-padding && eventY <= yRange[i]+padding) && !isHit[i]) {
                        prevX = eventX;
                        prevY = eventY;
                        tileHit(i);
                    }
                }

                return true;

            case (MotionEvent.ACTION_MOVE) :

                if(tilesHit < longestPath){
                    for(int i = 0; i < numBoxes; i++) {

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
                    //reset if moving off grid
                    int upperRightmostTile = longestPath-1;
                    int lastTile = numBoxes-1;
                    if(eventX < tvX[0] || eventX > tvX[upperRightmostTile] + tvLength || eventY > tvY[lastTile] + tvHeight || eventY < tvY[0]){
                        resetTiles();
                    }
                }


                if(tilesHit == 10){
                    resetTiles();
                }
                return true;
            case (MotionEvent.ACTION_UP) :
                if(currentScore == greatestPath){
                    generateNextLevel();
                    displayCurrentScore.setText("");
                }else{
                    resetTiles();
                    displayCurrentScore.setText("");
                }

                return true;
            default :
                return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        level = settings.getInt("level", 1);
        time = settings.getInt("time", 8);
        totalScore = settings.getInt("score", 0);
        global = settings.getInt("global",0);
        restore = settings.getBoolean("restore", true);
        gridSize = settings.getString("gridSize","3x3");
        //restore grid when returning from exit
        if(restore){
            int[] restore = new int[numBoxes];
            for(int i = 0; i < numBoxes; i++){
                restore[i] = settings.getInt("grid "+i,0);
                textViews[i].setText(Integer.toString(restore[i]));
            }
            if(mode.equals("Classic") || mode.equals("Blitz")){
                greatestPath = settings.getInt("objective",0);
                goal.setText("Objective: "+greatestPath);
            }
        }
        if(!mode.equals("Sudden Death")){
            initializeCountdown(time);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!mode.equals("Sudden Death")){
            countDown.cancel();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //assume youre in mid game, gameOver resets values
        if(!gameOver){
            editor.putInt("score", totalScore);
            editor.putInt("level", level);
            editor.putInt("time",time);
            editor.putInt("global", global);
            editor.putString("gridSize",gridSize);
            //save grid
            for(int i = 0; i < numBoxes; i++){
                editor.putInt("grid " + i, Integer.parseInt(textViews[i].getText().toString()));
            }
            if(mode.equals("Classic") || mode.equals("Blitz")){
                editor.putInt("objective", greatestPath);
            }
            editor.putBoolean("restore",true);
            editor.commit();
        }

    }

    public void tileHit(int index){

        if(!isMuted){
            switch(tilesHit){
                case 0:
                    soundPool.play(soundID1, volume, volume, 1, 0, 1f);
                    break;
                case 1:
                    if(longestPath == 3){
                        soundPool.play(soundID3, volume, volume, 1, 0, 1f);
                    }else{
                        soundPool.play(soundID2, volume, volume, 1, 0, 1f);
                    }
                    break;
                case 2:
                    if(longestPath == 3){
                        soundPool.play(soundID5, volume, volume, 1, 0, 1f);
                    }else{
                        soundPool.play(soundID3, volume, volume, 1, 0, 1f);
                    }
                    break;
                case 3:
                    if(longestPath == 4){
                        soundPool.play(soundID5, volume, volume, 1, 0, 1f);
                    }else{
                        soundPool.play(soundID4, volume, volume, 1, 0, 1f);
                        soundPool.play(soundID4, volume, volume, 1, 0, 1f);
                    }
                    break;
                case 4:
                    soundPool.play(soundID5, volume, volume, 1, 0, 1f);

            }

        }

        int color = R.color.purple;
        try{
            String theme = StaticMethods.readFirstLine("theme.txt",getBaseContext());
            if(theme != null && !theme.equals("0")){
                switch(theme){
                    case "Daylight":
                        color = R.color.daylight_tile;
                        break;
                    case "Midnight":
                        color = R.color.midnight_tile;
                        break;
                    default:
                        color = R.color.purple;
                }
            }
        }catch(IOException e){}

        textViews[index].setBackgroundResource(color);
        tilesHit++;
        isHit[index] = true;
        currentScore += Integer.parseInt(textViews[index].getText().toString());
        displayCurrentScore.setText(Integer.toString(currentScore));

    }

    public void resetTiles(){
        currentScore = 0;
        for(int i = 0; i < numBoxes; i++){
            textViews[i].setBackgroundResource(R.color.white);
            textViews[i].getBackground().setAlpha(300);
            isHit[i] = false;
            leftRange[i] = false;
        }
        if((mode.equals("Sudden Death") || mode.equals("Nightmare")) && !gameOver && greatestPath != 0 && tilesHit > 0){
            gameOver();
        }
        tilesHit = 0;
    }

    public void generateNextLevel() {

        totalScore += currentScore;

        level++;
        global++;
        tilesHit = 0;
        currentScore = 0;

        if(level <=3){
            editor.putString("gridSize","3x3");
            setLayout();
        } else if (level > 3 && level <= 7){
            editor.putString("gridSize","4x4");
            setLayout();
        }else{
            editor.putString("gridSize","5x5");
            setLayout();
        }
        editor.commit();
        if(!isMuted){
            if(theme.equals("Daylight")){
                soundPool.play(soundID9, volume, volume, 1, 0, 1f);
            }else if(theme.equals("Midnight")){
                soundPool.play(soundID10, volume, volume, 1, 0, 1f);
            }else{
                soundPool.play(soundID7, volume, volume, 1, 0, 1f);
            }
        }

        values = new int[numBoxes];
        isHit = new boolean[numBoxes];
        tvX = new float[numBoxes];
        tvY = new float[numBoxes];
        xRange = new float[numBoxes];
        yRange = new float[numBoxes];
        leftRange = new boolean[numBoxes];

        greatestPath = calculateGreatestPath(gridSize);

        if((mode.equals("Classic") || mode.equals("Nightmare")) && gridSize.equals("3x3")){
            time = 8;
        }else if((mode.equals("Classic") || mode.equals("Nightmare")) && gridSize.equals("4x4")){
            time = 11;
        }else if((mode.equals("Classic") || mode.equals("Nightmare")) && gridSize.equals("5x5") && level < 9){
            time = 16;
        }else if((mode.equals("Classic") || mode.equals("Nightmare")) && gridSize.equals("5x5") && level >= 9){
            time = 21;
        }else if(mode.equals("Blitz") && (gridSize.equals("3x3"))){
            time = 4;
        }else if(mode.equals("Blitz") && (gridSize.equals("4x4"))){
            time = 6;
        }else if(mode.equals("Blitz") && (gridSize.equals("5x5"))){
            time = 8;
        }


        if(!mode.equals("Sudden Death")){
            countDown.cancel();
            initializeCountdown(time);
            if(!mode.equals("Nightmare")){
                goal.setText("Objective: " + greatestPath);
            }
        }

        for(int i = 0; i < numBoxes; i++){
            textViews[i].setText(Integer.toString(values[i]));
            textViews[i].setBackgroundResource(R.color.white);
            textViews[i].getBackground().setAlpha(300);
            isHit[i] = false;
        }

        if(totalScore < 10){
            displayTotalScore.setText("Score\n    " + totalScore);
        }else if(totalScore < 100){
            displayTotalScore.setText("Score\n  " + totalScore);
        }else if(totalScore < 1000){
            displayTotalScore.setText("Score\n " + totalScore);
        }else{
            displayTotalScore.setText("Score\n" + totalScore);
        }
        displayLevel.setText("Level\n   " + level);

    }

    public void gameOver(){
        gameOver = true;
        editor.putBoolean("gameOver", true);
        resetTiles();
        if (!isMuted) {
            soundPool.play(soundID6, volume, volume, 1, 0, 1f);
        }

        editor.putInt("score", 0);
        editor.putInt("level", 1);
        editor.putInt("global", 0);
        editor.putString("gridSize","3x3");
        if(mode.equals("Classic") || mode.equals("Nightmare")){
            editor.putInt("time",8);
        }else if(mode.equals("Blitz")){
            editor.putInt("time",4);
        }

        int prevSeed = settings.getInt("seed",0);
        prevSeed++;
        editor.putInt("seed", prevSeed);
        editor.commit();
        saveHighScore();
        Intent i = new Intent(getBaseContext(),GameOver.class);
        i.putExtra("score", totalScore);
        i.putExtra("level", level);
        startActivity(i);

    }

    public void initializeCountdown(int numTicks){

        countDown = new CountDownTimer(numTicks*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                time--;
                if(!mode.equals("Nightmare")) {
                    if (time == 3) {
                        displayTime.setTextColor(getResources().getColor(R.color.yellow));
                        if (!isMuted) {
                            soundPool.play(soundID8, volume, volume, 1, 0, 1f);
                        }
                    } else if (time == 2) {
                        displayTime.setTextColor(getResources().getColor(R.color.orange));
                        if (!isMuted) {
                            soundPool.play(soundID8, volume, volume, 1, 0, 1f);
                        }
                    } else if (time == 1) {
                        displayTime.setTextColor(getResources().getColor(R.color.red2));
                        if (!isMuted) {
                            soundPool.play(soundID8, volume, volume, 1, 0, 1f);
                        }
                    } else {
                        displayTime.setTextColor(getResources().getColor(R.color.white));
                    }
                    displayTime.setText("" + time);
                }else{
                    displayTime.setTextColor(getResources().getColor(R.color.white));
                }


            }

            @Override
            public void onFinish() {
                gameOver();
            }
        };
        countDown.start();
    }

    //back button does nothing
    @Override
    public void onBackPressed() {}

    @TargetApi(21)
    private void initializeSoundPool(){
        SoundPool.Builder spb;

        if((android.os.Build.VERSION.SDK_INT) >= 21){
            spb = new SoundPool.Builder();
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            spb.setMaxStreams(8);
            spb.setAudioAttributes(attributes);
            soundPool = spb.build();
        }else{
            soundPool = new SoundPool(8, AudioManager.STREAM_MUSIC, 0);
        }
    }

    private int calculateGreatestPath(String size){

        int ret = 0;
        ArrayList<Integer> tempValues = new ArrayList<Integer>();//stores matrix values

        if(size.equals("3x3")){
            GreatestPath3 g = new GreatestPath3();

            Tile[][] tiles = g.getTiles();

            int greatest = 0;
            for(int i = 0; i < tiles.length; i++){

                for(int j = 0; j < tiles[i].length; j++){

                    tempValues.add(tiles[i][j].c.value);
                    int temp = g.findGreatestPath(tiles[i][j]);
                    if(temp > greatest){
                        greatest = temp;
                    }

                }

            }
            ret = greatest;
        }else if(size.equals("4x4")){
            GreatestPath4 g = new GreatestPath4();

            Tile[][] tiles = g.getTiles();

            int greatest = 0;
            for(int i = 0; i < tiles.length; i++){

                for(int j = 0; j < tiles[i].length; j++){

                    tempValues.add(tiles[i][j].c.value);
                    int temp = g.findGreatestPath(tiles[i][j]);
                    if(temp > greatest){
                        greatest = temp;
                    }

                }

            }
            ret = greatest;
        }else{
            GreatestPath g = new GreatestPath();

            Tile[][] tiles = g.getTiles();

            int greatest = 0;
            for(int i = 0; i < tiles.length; i++){

                for(int j = 0; j < tiles[i].length; j++){

                    tempValues.add(tiles[i][j].c.value);
                    int temp = g.findGreatestPath(tiles[i][j]);
                    if(temp > greatest){
                        greatest = temp;
                    }

                }

            }
            ret = greatest;
        }
        //save matrix before leaving
        for(int i = 0; i < numBoxes; i++){
            values[i] = tempValues.get(i);
        }
        return ret;
    }

    private void restoreValues(){

        if((mode.equals("Classic") || mode.equals("Nightmare")) && gridSize.equals("3x3") && time == 8){
            restore = false;
            editor.putBoolean("restore",false);
        }
        if((mode.equals("Classic") || mode.equals("Nightmare")) && gridSize.equals("4x4") && time == 11){
            restore = false;
            editor.putBoolean("restore",false);
        }
        if((mode.equals("Classic") || mode.equals("Nightmare")) && gridSize.equals("5x5") && time == 16 && level < 9){
            restore = false;
            editor.putBoolean("restore",false);
        }
        if((mode.equals("Classic") || mode.equals("Nightmare")) && gridSize.equals("5x5") && time == 21 && level >= 9){
            restore = false;
            editor.putBoolean("restore",false);
        }
        if(mode.equals("Blitz") && time == 4 && gridSize.equals("3x3")){
            restore = false;
            editor.putBoolean("restore",false);
        }
        if(mode.equals("Blitz") && time == 6 && gridSize.equals("4x4")){
            restore = false;
            editor.putBoolean("restore",false);
        }
        if(mode.equals("Blitz") && time == 8 && gridSize.equals("5x5")){
            restore = false;
            editor.putBoolean("restore",false);
        }
        if(mode.equals("Sudden Death")){
            restore = false;
            editor.putBoolean("restore",false);
        }
        editor.commit();
    }

    private void saveHighScore(){
        String scoreKey = "highScore_" + mode;
        String levelKey = "highLevel_" + mode;
        int tempInt = settings.getInt(scoreKey,0);
        if(totalScore > tempInt){
            editor.putInt(scoreKey,totalScore);
            editor.putInt(levelKey, level);
        }
        editor.commit();
    }

    private void setLayout(){
        gridSize = settings.getString("gridSize","3x3");
        if(gridSize.equals("3x3")){
            vf.setDisplayedChild(0);
        }else if(gridSize.equals("4x4")){
            vf.setDisplayedChild(1);
        }else{
            vf.setDisplayedChild(2);
        }
        if(gridSize.equals("3x3")){
            numBoxes = 9;
            longestPath = 3;
            rl = (RelativeLayout) findViewById(R.id.relativeLayout3);
            displayCurrentScore = (TextView)findViewById(R.id.currentScore3);
            displayTotalScore = (TextView) findViewById(R.id.totalScore3);
            displayLevel = (TextView) findViewById(R.id.level3);
            displayTime = (TextView) findViewById(R.id.time3);
            goal = (TextView) findViewById(R.id.goal3);
            displayMode = (TextView) findViewById(R.id.displayMode3);
            menuButton = (Button) findViewById(R.id.menu_button3);
            tv1 = (TextView)findViewById(R.id.tv1_3);
            tv2 = (TextView)findViewById(R.id.tv2_3);
            tv3 = (TextView)findViewById(R.id.tv3_3);
            tv4 = (TextView)findViewById(R.id.tv4_3);
            tv5 = (TextView)findViewById(R.id.tv5_3);
            tv6 = (TextView)findViewById(R.id.tv6_3);
            tv7 = (TextView)findViewById(R.id.tv7_3);
            tv8 = (TextView)findViewById(R.id.tv8_3);
            tv9 = (TextView)findViewById(R.id.tv9_3);
        }else if(gridSize.equals("4x4")){
            numBoxes = 16;
            longestPath = 4;
            rl = (RelativeLayout) findViewById(R.id.relativeLayout4);
            displayCurrentScore = (TextView)findViewById(R.id.currentScore4);
            displayTotalScore = (TextView) findViewById(R.id.totalScore4);
            displayLevel = (TextView) findViewById(R.id.level4);
            displayTime = (TextView) findViewById(R.id.time4);
            goal = (TextView) findViewById(R.id.goal4);
            displayMode = (TextView) findViewById(R.id.displayMode4);
            menuButton = (Button) findViewById(R.id.menu_button4);
            tv1 = (TextView)findViewById(R.id.tv1_4);
            tv2 = (TextView)findViewById(R.id.tv2_4);
            tv3 = (TextView)findViewById(R.id.tv3_4);
            tv4 = (TextView)findViewById(R.id.tv4_4);
            tv5 = (TextView)findViewById(R.id.tv5_4);
            tv6 = (TextView)findViewById(R.id.tv6_4);
            tv7 = (TextView)findViewById(R.id.tv7_4);
            tv8 = (TextView)findViewById(R.id.tv8_4);
            tv9 = (TextView)findViewById(R.id.tv9_4);
            tv10 = (TextView)findViewById(R.id.tv10_4);
            tv11 = (TextView)findViewById(R.id.tv11_4);
            tv12 = (TextView)findViewById(R.id.tv12_4);
            tv13 = (TextView)findViewById(R.id.tv13_4);
            tv14 = (TextView)findViewById(R.id.tv14_4);
            tv15 = (TextView)findViewById(R.id.tv15_4);
            tv16 = (TextView)findViewById(R.id.tv16_4);
        }else{
            numBoxes = 25;
            longestPath = 5;
            rl = (RelativeLayout) findViewById(R.id.relativeLayout5);
            displayCurrentScore = (TextView)findViewById(R.id.currentScore5);
            displayTotalScore = (TextView) findViewById(R.id.totalScore5);
            displayLevel = (TextView) findViewById(R.id.level5);
            displayTime = (TextView) findViewById(R.id.time5);
            goal = (TextView) findViewById(R.id.goal5);
            displayMode = (TextView) findViewById(R.id.displayMode5);
            menuButton = (Button) findViewById(R.id.menu_button5);
            tv1 = (TextView)findViewById(R.id.tv1_5);
            tv2 = (TextView)findViewById(R.id.tv2_5);
            tv3 = (TextView)findViewById(R.id.tv3_5);
            tv4 = (TextView)findViewById(R.id.tv4_5);
            tv5 = (TextView)findViewById(R.id.tv5_5);
            tv6 = (TextView)findViewById(R.id.tv6_5);
            tv7 = (TextView)findViewById(R.id.tv7_5);
            tv8 = (TextView)findViewById(R.id.tv8_5);
            tv9 = (TextView)findViewById(R.id.tv9_5);
            tv10 = (TextView)findViewById(R.id.tv10_5);
            tv11 = (TextView)findViewById(R.id.tv11_5);
            tv12 = (TextView)findViewById(R.id.tv12_5);
            tv13 = (TextView)findViewById(R.id.tv13_5);
            tv14 = (TextView)findViewById(R.id.tv14_5);
            tv15 = (TextView)findViewById(R.id.tv15_5);
            tv16 = (TextView)findViewById(R.id.tv16_5);
            tv17 = (TextView)findViewById(R.id.tv17_5);
            tv18 = (TextView)findViewById(R.id.tv18_5);
            tv19 = (TextView)findViewById(R.id.tv19_5);
            tv20 = (TextView)findViewById(R.id.tv20_5);
            tv21 = (TextView)findViewById(R.id.tv21_5);
            tv22 = (TextView)findViewById(R.id.tv22_5);
            tv23 = (TextView)findViewById(R.id.tv23_5);
            tv24 = (TextView)findViewById(R.id.tv24_5);
            tv25 = (TextView)findViewById(R.id.tv25_5);
        }

        textViews = new TextView[numBoxes];
        textViews[0] = tv1;
        textViews[1] = tv2;
        textViews[2] = tv3;
        textViews[3] = tv4;
        textViews[4] = tv5;
        textViews[5] = tv6;
        textViews[6] = tv7;
        textViews[7] = tv8;
        textViews[8] = tv9;
        if(numBoxes > 9){

            textViews[9] = tv10;
            textViews[10] = tv11;
            textViews[11] = tv12;
            textViews[12] = tv13;
            textViews[13] = tv14;
            textViews[14] = tv15;
            textViews[15] = tv16;
            if(numBoxes > 16){
                textViews[16] = tv17;
                textViews[17] = tv18;
                textViews[18] = tv19;
                textViews[19] = tv20;
                textViews[20] = tv21;
                textViews[21] = tv22;
                textViews[22] = tv23;
                textViews[23] = tv24;
                textViews[24] = tv25;
            }

        }
        StaticMethods.changeTheme(rl,getBaseContext());

        try{
            theme = StaticMethods.readFirstLine("theme.txt",this);
        }catch(IOException e){}

        if(mode.equals("Sudden Death")){
            displayTime.setText("-");
        }else if(mode.equals("Nightmare")){
            displayTime.setText("?");
        }else{
            displayTime.setText("" + time);
        }

        menuButton.getBackground().setAlpha(1);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveHighScore();
                Intent i = new Intent(getBaseContext(), Menu.class);
                startActivity(i);
            }
        });
    }

}
