package com.example.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class GameOver extends Activity {

    private int highScore;

    private Button retry;
    private TextView displayHighScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);

        retry = (Button) findViewById(R.id.retry);
        displayHighScore = (TextView) findViewById(R.id.highscore);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),MainActivity.class);
                startActivity(i);
            }
        });
        try{
            highScore = Integer.parseInt(StaticMethods.readFirstLine("highScore.txt",getBaseContext()));
        }catch(IOException e){}
        displayHighScore.setText("High Score: "+highScore);

    }


}
