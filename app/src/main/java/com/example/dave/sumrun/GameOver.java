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

import java.io.IOException;

public class GameOver extends Activity {

    private int highScore;

    private ImageButton retry;
    private TextView displayInfo;
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.game_over);

        int score = getIntent().getIntExtra("score", 0);
        int level = getIntent().getIntExtra("level", 0);
        int highLevel = 0;

        retry = (ImageButton) findViewById(R.id.retry);
        displayInfo = (TextView) findViewById(R.id.displayInfo);
        rl = (RelativeLayout) findViewById(R.id.relativeLayout);

        retry.setBackgroundResource(R.mipmap.retry_unpressed);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry.setBackgroundResource(R.mipmap.retry_pressed);
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
            }
        });
        try {
            highScore = Integer.parseInt(StaticMethods.readFirstLine("highScore2.txt", getBaseContext()));
            highLevel = Integer.parseInt(StaticMethods.readFirstLine("level.txt", getBaseContext()));
        } catch (IOException e) {
        }
        displayInfo.setText("      Score:   " + score + "           Level:   " + level +
                "\n\n\n      Personal Best\n\n      Score:   " + highScore + "           Level:   "+highLevel);

    }


}
