package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.on.dave.sumrun.R;

public class GameOver extends Activity {

    private int highScore;
    private int highLevel;

    private Button retry;
    private TextView displayInfo;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settings = getSharedPreferences(GameView.PREFS_NAME_GAME, 0);

        setContentView(R.layout.game_over);

        int score = getIntent().getIntExtra("score", 0);
        int level = getIntent().getIntExtra("level", 0);

        retry = (Button) findViewById(R.id.retry);
        displayInfo = (TextView) findViewById(R.id.displayInfo);

        retry.getBackground().setAlpha(1);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), GameView.class);
                startActivity(i);
            }
        });

        String mode = settings.getString("mode","Classic");
        String scoreKey = "highScore_"+mode;
        String levelKey = "highLevel_"+mode;

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
