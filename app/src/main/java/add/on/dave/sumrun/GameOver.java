package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.on.dave.sumrun.R;

import java.io.IOException;

public class GameOver extends Activity {

    private int highScore;
    private int seed;

    private ImageButton retry;
    private ImageButton help;
    private ImageButton mute;
    private TextView displayInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            seed = Integer.parseInt(StaticMethods.readFirstLine("seed.txt",getBaseContext()));
        }catch (IOException e){}

        if (seed % 2 == 0) {
            MainActivity.interstitial.show();
        }

        setContentView(R.layout.game_over);
        //MainActivity.interstitial = null;

        int score = getIntent().getIntExtra("score", 0);
        int level = getIntent().getIntExtra("level", 0);
        int highLevel = 0;

        retry = (ImageButton) findViewById(R.id.retry);
        help = (ImageButton) findViewById(R.id.help);
        mute = (ImageButton) findViewById(R.id.mute);
        displayInfo = (TextView) findViewById(R.id.displayInfo);

        retry.setBackgroundResource(R.drawable.retry_unpressed);
        help.setBackgroundResource(R.drawable.button_2);
        if(MainActivity.isMuted){
            mute.setBackgroundResource(R.drawable.mutedbutton);
        }else{
            mute.setBackgroundResource(R.drawable.unmutedbutton);
        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry.setBackgroundResource(R.drawable.retry_pressed);
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                if(MainActivity.soundPool != null){
                    MainActivity.soundPool.release();
                }
                startActivity(i);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                help.setBackgroundResource(R.drawable.button_2_pressed);
                Intent i = new Intent(getBaseContext(), Help.class);
                i.putExtra("class", "gameOver");
                if (MainActivity.soundPool != null) {
                    MainActivity.soundPool.release();
                }
                startActivity(i);
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MainActivity.isMuted) {
                    MainActivity.isMuted = false;
                    mute.setBackgroundResource(R.drawable.unmutedbutton);
                } else {
                    MainActivity.isMuted = true;
                    mute.setBackgroundResource(R.drawable.mutedbutton);
                }

            }
        });

        try {
            String sHighScore = StaticMethods.readFirstLine("highScore3.txt", getBaseContext());
            String sHighLevel = StaticMethods.readFirstLine("level2.txt", getBaseContext());
            if(sHighScore != null && !sHighScore.equals("")){
                highScore = Integer.parseInt(sHighScore);
            }
            if(sHighLevel != null && !sHighLevel.equals("")){
                highLevel = Integer.parseInt(sHighLevel);
            }
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

    //back button does nothing
    @Override
    public void onBackPressed() {
    }

}
