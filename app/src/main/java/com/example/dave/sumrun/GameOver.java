package com.example.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;

public class GameOver extends Activity {

    private int highScore;
    private int seed;

    private ImageButton retry;
    private ImageButton help;
    private TextView displayInfo;

    private InterstitialAd interstitial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interstitial = new InterstitialAd(getBaseContext());
        interstitial.setAdUnitId("ca-app-pub-8421459443129126/5122852497");
        requestNewInterstitial();

        try{
            seed = Integer.parseInt(StaticMethods.readFirstLine("seed.txt",getBaseContext()));
        }catch (IOException e){}

        interstitial.setAdListener(new AdListener() {

            public void onAdLoaded(){
                if(seed % 2 == 0){
                    interstitial.show();
                }
            }

        });

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_over);

        int score = getIntent().getIntExtra("score", 0);
        int level = getIntent().getIntExtra("level", 0);
        int highLevel = 0;

        retry = (ImageButton) findViewById(R.id.retry);
        help = (ImageButton) findViewById(R.id.help);
        displayInfo = (TextView) findViewById(R.id.displayInfo);

        retry.setBackgroundResource(R.mipmap.retry_unpressed);
        help.setBackgroundResource(R.mipmap.button_2);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry.setBackgroundResource(R.mipmap.retry_pressed);
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help.setBackgroundResource(R.mipmap.button_2_pressed);
                Intent i = new Intent(getBaseContext(), Help.class);
                i.putExtra("class","gameOver");
                startActivity(i);
            }
        });

        try {
            highScore = Integer.parseInt(StaticMethods.readFirstLine("highScore3.txt", getBaseContext()));
            highLevel = Integer.parseInt(StaticMethods.readFirstLine("level2.txt", getBaseContext()));
        } catch (IOException e) {
        }
        if(score == highScore){
            displayInfo.setText("      New HighScore!\n\n      Score:   " + score + "           Level:   " + level +
                    "\n\n\n      Personal Best\n\n      Score:   " + highScore + "           Level:   " + highLevel);
        }else{
            displayInfo.setText("      Score:   " + score + "           Level:   " + level +
                    "\n\n\n      Personal Best\n\n      Score:   " + highScore + "           Level:   " + highLevel);
        }

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4A5BC2497191112F02A42DC7DBDFEA47")
                .build();

        interstitial.loadAd(adRequest);
    }


}
