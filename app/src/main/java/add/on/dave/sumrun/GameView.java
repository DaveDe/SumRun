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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.on.dave.sumrun.R;

import java.io.IOException;
import java.util.ArrayList;

public class GameView extends Activity {

    public static int global;
    public static boolean isMuted;
    public static InterstitialAd interstitial;
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
    private int numBoxes;
    private int longestPath;
    private float prevX, prevY, volume;
    private String mode;
    private String gridSize;

    private RelativeLayout rl;
    private TextView[] textViews;
    private TextView displayCurrentScore;
    private TextView displayTotalScore;
    private TextView displayLevel;
    private TextView displayTime;
    private TextView goal;
    private TextView displayMode;
    //private Button help;
    private Button menuButton;

    private CountDownTimer countDown;
    private SoundPool soundPool;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(PREFS_NAME_GAME, 0);
        editor = settings.edit();

        gridSize = settings.getString("gridSize","3x3");
        //set different layout based on gridsize
        if(gridSize.equals("3x3")){
            setContentView(R.layout.game_view_3x3);
        }else if(gridSize.equals("4x4")){
            setContentView(R.layout.game_view_4x4);
        }else{
            setContentView(R.layout.game_view);
        }
        if(gridSize.equals("3x3")){
            numBoxes = 9;
            longestPath = 3;
        }else if(gridSize.equals("4x4")){
            numBoxes = 16;
            longestPath = 4;
        }else{
            numBoxes = 25;
            longestPath = 5;
        }

        interstitial = new InterstitialAd(getBaseContext());
        interstitial.setAdUnitId("ca-app-pub-8421459443129126/5122852497");
        requestNewInterstitial();

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        displayCurrentScore = (TextView)findViewById(R.id.currentScore);
        displayTotalScore = (TextView) findViewById(R.id.totalScore);
        displayLevel = (TextView) findViewById(R.id.level);
        displayTime = (TextView) findViewById(R.id.time);
        goal = (TextView) findViewById(R.id.goal);
        displayMode = (TextView) findViewById(R.id.displayMode);
        menuButton = (Button) findViewById(R.id.menu_button);
        TextView tv1 = (TextView)findViewById(R.id.tv1);
        TextView tv2 = (TextView)findViewById(R.id.tv2);
        TextView tv3 = (TextView)findViewById(R.id.tv3);
        TextView tv4 = (TextView)findViewById(R.id.tv4);
        TextView tv5 = (TextView)findViewById(R.id.tv5);
        TextView tv6 = (TextView)findViewById(R.id.tv6);
        TextView tv7 = (TextView)findViewById(R.id.tv7);
        TextView tv8 = (TextView)findViewById(R.id.tv8);
        TextView tv9 = (TextView)findViewById(R.id.tv9);
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
            TextView tv10 = (TextView)findViewById(R.id.tv10);
            TextView tv11 = (TextView)findViewById(R.id.tv11);
            TextView tv12 = (TextView)findViewById(R.id.tv12);
            TextView tv13 = (TextView)findViewById(R.id.tv13);
            TextView tv14 = (TextView)findViewById(R.id.tv14);
            TextView tv15 = (TextView)findViewById(R.id.tv15);
            TextView tv16 = (TextView)findViewById(R.id.tv16);
            textViews[9] = tv10;
            textViews[10] = tv11;
            textViews[11] = tv12;
            textViews[12] = tv13;
            textViews[13] = tv14;
            textViews[14] = tv15;
            textViews[15] = tv16;
            if(numBoxes > 16){
                TextView tv17 = (TextView)findViewById(R.id.tv17);
                TextView tv18 = (TextView)findViewById(R.id.tv18);
                TextView tv19 = (TextView)findViewById(R.id.tv19);
                TextView tv20 = (TextView)findViewById(R.id.tv20);
                TextView tv21 = (TextView)findViewById(R.id.tv21);
                TextView tv22 = (TextView)findViewById(R.id.tv22);
                TextView tv23 = (TextView)findViewById(R.id.tv23);
                TextView tv24 = (TextView)findViewById(R.id.tv24);
                TextView tv25 = (TextView)findViewById(R.id.tv25);
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

        gameOver = settings.getBoolean("gameOver",false);
        if(!gameOver){
            restore = true;
        }else{
            restore = false;
        }

        gameOver = false;

        //restore values if returning to game

        mode = settings.getString("mode", "classic");

        level = settings.getInt("level",1);
        time = settings.getInt("time", 16);
        totalScore = settings.getInt("score", 0);
        global = settings.getInt("global", 0);

        if(time == 16){
            restore = false;
        }

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

        menuButton.getBackground().setAlpha(1);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Menu.class);
                startActivity(i);
            }
        });

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
        time = settings.getInt("time", 16);
        totalScore = settings.getInt("score", 0);
        global = settings.getInt("global",0);
        restore = settings.getBoolean("restore",true);
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

    public void generateNextLevel(){
        if(!isMuted){
            soundPool.play(soundID7, volume, volume, 1, 0, 1f);
        }

        totalScore += currentScore;

        level++;
        global++;
        tilesHit = 0;
        currentScore = 0;

        values = new int[numBoxes];
        isHit = new boolean[numBoxes];
        tvX = new float[numBoxes];
        tvY = new float[numBoxes];
        xRange = new float[numBoxes];
        yRange = new float[numBoxes];
        leftRange = new boolean[numBoxes];

        greatestPath = calculateGreatestPath(gridSize);

        if(level >= 8 && mode.equals("Classic")){
            time = 21;
        }else{
            if(mode.equals("Classic") || mode.equals("Nightmare")){
                time = 16;
            }else if(mode.equals("Blitz")){
                time = 6;
            }
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
        editor.putBoolean("gameOver",true);
        resetTiles();
        if (!isMuted) {
            soundPool.play(soundID6, volume, volume, 1, 0, 1f);
        }

        editor.putInt("score", 0);
        editor.putInt("level", 1);
        editor.putInt("global", 0);
        if(mode.equals("Classic") || mode.equals("Nightmare")){
            editor.putInt("time",16);
        }else if(mode.equals("Blitz")){
            editor.putInt("time",6);
        }

        int prevSeed = settings.getInt("seed",0);
        prevSeed++;
        editor.putInt("seed", prevSeed);
        String scoreKey = "highScore_" + mode + "_" + gridSize;
        String levelKey = "highLevel_" + mode + "_" + gridSize;
        int tempInt = settings.getInt(scoreKey,0);
        if(totalScore > tempInt){
            editor.putInt(scoreKey,totalScore);
            editor.putInt(levelKey,level);
        }
        editor.commit();
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
    public void onBackPressed() {
    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4A5BC2497191112F02A42DC7DBDFEA47")
                .build();

        interstitial.loadAd(adRequest);
    }

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

}
