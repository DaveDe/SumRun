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
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.on.dave.sumrun.R;

import java.io.IOException;
import java.util.ArrayList;

//grid changes when going to menu and back

public class GameView extends Activity {

    public static int global;
    public static boolean isMuted;
    public static SoundPool soundPool;
    public static InterstitialAd interstitial;
    public static final String PREFS_NAME = "game_data3";

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
    private float prevX, prevY, volume;

    private RelativeLayout rl;
    private TextView[] textViews;
    private TextView displayCurrentScore;
    private TextView displayTotalScore;
    private TextView displayLevel;
    private TextView displayTime;
    private TextView goal;
    private ImageButton help;
    private Button menuButton;

    private CountDownTimer countDown;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_view);

        interstitial = new InterstitialAd(getBaseContext());
        interstitial.setAdUnitId("ca-app-pub-8421459443129126/5122852497");
        requestNewInterstitial();

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        displayCurrentScore = (TextView)findViewById(R.id.currentScore);
        displayTotalScore = (TextView) findViewById(R.id.totalScore);
        displayLevel = (TextView) findViewById(R.id.level);
        displayTime = (TextView) findViewById(R.id.time);
        goal = (TextView) findViewById(R.id.goal);
        help = (ImageButton) findViewById(R.id.help);
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

        StaticMethods.changeTheme(rl,getBaseContext());

        gameOver = false;
        restore = true;

        //restore values if returning to game
        settings = getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();

        level = settings.getInt("level",1);
        time = settings.getInt("time",16);
        totalScore = settings.getInt("score",0);
        global = settings.getInt("global",0);

        if(time == 16){
            restore = false;
        }

        displayLevel.setText("Level\n   "+level);
        displayTotalScore.setText("Score\n    "+totalScore);
        displayTime.setText("" + time);

        GreatestPath g = new GreatestPath();

        Tile[][] tiles = g.getTiles();

        ArrayList<Integer> tempValues = new ArrayList<Integer>();//stores matrix values

        for(int i = 0; i < tiles.length; i++){

            for(int j = 0; j < tiles[i].length; j++){

                tempValues.add(tiles[i][j].c.value);
                int temp = g.findGreatestPath(tiles[i][j]);
                if(temp > greatestPath){
                    greatestPath = temp;
                }

            }

        }
        goal.setText("Objective: "+greatestPath);

        values = new int[25];
        isHit = new boolean[25];
        tvX = new float[25];
        tvY = new float[25];
        xRange = new float[25];
        yRange = new float[25];
        leftRange = new boolean[25];

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


        for(int i = 0; i < 25; i++){
            values[i] = tempValues.get(i);
            textViews[i].setText(Integer.toString(values[i]));
            textViews[i].setBackgroundResource(R.color.white);
            textViews[i].getBackground().setAlpha(300);
            isHit[i] = false;
        }

        help.setBackgroundResource(R.drawable.button_1);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help.setBackgroundResource(R.drawable.button_1_pressed);
                Intent i = new Intent(getBaseContext(), Help.class);
                i.putExtra("class", "Menu");
                startActivity(i);
            }
        });

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
                    //reset if moving off grid
                    if(eventX < tvX[0] || eventX > tvX[4] + tvLength || eventY > tvY[24] + tvHeight || eventY < tvY[0]){
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
        time = settings.getInt("time",16);
        totalScore = settings.getInt("score",0);
        global = settings.getInt("global",0);
        //restore grid when returning from exit
        if(restore){
            int[] restore = new int[25];
            for(int i = 0; i < 25; i++){
                restore[i] = settings.getInt("grid "+i,0);
                textViews[i].setText(Integer.toString(restore[i]));
            }
            greatestPath = settings.getInt("objective",0);
            goal.setText("Objective: "+greatestPath);
        }

        initializeCountdown(time);
    }

    @Override
    protected void onPause() {
        super.onPause();
        countDown.cancel();
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
            for(int i = 0; i < 25; i++){
                editor.putInt("grid " + i, Integer.parseInt(textViews[i].getText().toString()));
            }
            editor.putInt("objective",greatestPath);
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
                    soundPool.play(soundID2, volume, volume, 1, 0, 1f);
                    break;
                case 2:
                    soundPool.play(soundID3, volume, volume, 1, 0, 1f);
                    break;
                case 3:
                    soundPool.play(soundID4, volume, volume, 1, 0, 1f);
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
        for(int i = 0; i < 25; i++){
            textViews[i].setBackgroundResource(R.color.white);
            textViews[i].getBackground().setAlpha(300);
            isHit[i] = false;
            leftRange[i] = false;
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
            tickNums = 21;
            time = 21;
        }else{
            time = 16;
            tickNums = 16;
        }

        countDown.cancel();
        //countDown = null;
        initializeCountdown(tickNums);

        for(int i = 0; i < 25; i++){
            values[i] = tempValues.get(i);
            textViews[i].setText(Integer.toString(values[i]));
            textViews[i].setBackgroundResource(R.color.white);
            textViews[i].getBackground().setAlpha(300);
            isHit[i] = false;
        }

        goal.setText("Objective: " + greatestPath);
        if(totalScore < 10){
            displayTotalScore.setText("Score\n    " + totalScore);
        }else if(totalScore < 100){
            displayTotalScore.setText("Score\n  " + totalScore);
        }else if(totalScore < 1000){
            displayTotalScore.setText("Score\n " + totalScore);
        }else{
            displayTotalScore.setText("Score\n" + totalScore);
        }
        displayLevel.setText("Level\n   "+level);

    }

    public void gameOver(){
        gameOver = true;
        resetTiles();
        if(time == 1) {
            if (!isMuted) {
                soundPool.play(soundID6, volume, volume, 1, 0, 1f);
            }
        }

        editor.putInt("score", 0);
        editor.putInt("level", 1);
        editor.putInt("time",16);
        editor.putInt("global", 0);
        editor.commit();

        try{
            int tempInt = 0;
            String temp = StaticMethods.readFirstLine("highScore3.txt",getBaseContext());
            if(temp != null && !temp.equals("")){
                tempInt = Integer.parseInt(temp);
            }
            int prevSeed = Integer.parseInt(StaticMethods.readFirstLine("seed.txt",getBaseContext()));
            prevSeed++;
            StaticMethods.write("seed.txt",Integer.toString(prevSeed),getBaseContext());
            if(totalScore > tempInt){
                StaticMethods.write("highScore3.txt",Integer.toString(totalScore),getBaseContext());
                StaticMethods.write("level2.txt",Integer.toString(level),getBaseContext());
            }
        }catch (IOException e){}
        Intent i = new Intent(getBaseContext(),GameOver.class);
        i.putExtra("score", totalScore);
        i.putExtra("level", level);
        startActivity(i);


    }

    public void initializeCountdown(int numTicks){

        countDown = new CountDownTimer(numTicks*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //if(!pause){
                time--;
                if(time == 3){
                    displayTime.setTextColor(getResources().getColor(R.color.yellow));
                    if(!isMuted){
                        soundPool.play(soundID8, volume, volume, 1, 0, 1f);
                    }
                }else if (time == 2){
                    displayTime.setTextColor(getResources().getColor(R.color.orange));
                    if(!isMuted){
                        soundPool.play(soundID8, volume, volume, 1, 0, 1f);
                    }
                }else if(time == 1){
                    displayTime.setTextColor(getResources().getColor(R.color.red2));
                    if(!isMuted){
                        soundPool.play(soundID8, volume, volume, 1, 0, 1f);
                    }
                }else{
                    displayTime.setTextColor(getResources().getColor(R.color.white));
                }

                displayTime.setText("" + time);
                //}else{
                // }
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

}
