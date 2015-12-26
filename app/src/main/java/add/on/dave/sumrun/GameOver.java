package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
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
    private Button mainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try{
            seed = Integer.parseInt(StaticMethods.readFirstLine("seed.txt",getBaseContext()));
        }catch (IOException e){}

        if (seed % 2 == 0) {
            GameView.interstitial.show();
        }

        setContentView(R.layout.game_over);

        int score = getIntent().getIntExtra("score", 0);
        int level = getIntent().getIntExtra("level", 0);
        int highLevel = 0;

        retry = (ImageButton) findViewById(R.id.retry);
        help = (ImageButton) findViewById(R.id.help);
        mute = (ImageButton) findViewById(R.id.mute);
        displayInfo = (TextView) findViewById(R.id.displayInfo);
        mainMenu = (Button) findViewById(R.id.main_menu);

        retry.setBackgroundResource(R.drawable.retry_unpressed);
        help.setBackgroundResource(R.drawable.button_2);
        if(GameView.isMuted){
            mute.setBackgroundResource(R.drawable.mutedbutton);
        }else{
            mute.setBackgroundResource(R.drawable.unmutedbutton);
        }

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retry.setBackgroundResource(R.drawable.retry_pressed);
                Intent i = new Intent(getBaseContext(), GameView.class);
                if(GameView.soundPool != null){
                    GameView.soundPool.release();
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
                if (GameView.soundPool != null) {
                    GameView.soundPool.release();
                }
                startActivity(i);
            }
        });

        mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (GameView.isMuted) {
                    GameView.isMuted = false;
                    mute.setBackgroundResource(R.drawable.unmutedbutton);
                } else {
                    GameView.isMuted = true;
                    mute.setBackgroundResource(R.drawable.mutedbutton);
                }

            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),MainActivity.class);
                startActivity(i);
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
