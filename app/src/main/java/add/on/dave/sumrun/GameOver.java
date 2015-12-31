package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private int highLevel;
    private int seed;

    private Button retry;
    private Button help;
    //
    private TextView displayInfo;

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(GameView.PREFS_NAME_GAME, 0);
        editor = settings.edit();

        seed = settings.getInt("seed",0);

        if (seed % 2 == 0) {
            GameView.interstitial.show();
        }

        setContentView(R.layout.game_over);

        int score = getIntent().getIntExtra("score", 0);
        int level = getIntent().getIntExtra("level", 0);

        retry = (Button) findViewById(R.id.retry);
        help = (Button) findViewById(R.id.help);
        displayInfo = (TextView) findViewById(R.id.displayInfo);

        retry.getBackground().setAlpha(1);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), GameView.class);
                startActivity(i);
            }
        });

        help.setText("?");
        help.getBackground().setAlpha(1);
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Help.class);
                i.putExtra("class", "gameOver");
                startActivity(i);
            }
        });

        String mode = settings.getString("mode","Classic");
        String gridSize = settings.getString("gridSize","3x3");
        String scoreKey = "highScore_"+mode + "_" + gridSize;
        String levelKey = "highLevel_"+mode + "_" + gridSize;

        highScore = settings.getInt(scoreKey,0);
        highLevel = settings.getInt(levelKey,0);
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
