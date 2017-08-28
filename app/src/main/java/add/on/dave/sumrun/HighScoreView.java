package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.on.dave.sumrun.R;

public class HighScoreView extends Activity{

    private RelativeLayout rl;
    private Button mode;
    private Button back;
    private TextView displayScore;

    private String currentMode;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        mode = (Button) findViewById(R.id.h_mode_button);
        back = (Button) findViewById(R.id.back);
        displayScore = (TextView) findViewById(R.id.display_high_score);

        mode.getBackground().setAlpha(1);
        back.getBackground().setAlpha(1);

        settings = getSharedPreferences(GameView.PREFS_NAME_GAME, 0);

        currentMode = settings.getString("mode", "Classic");

        mode.setText(currentMode);

        updateScoreDisplay();

        StaticMethods.changeTheme(rl, getBaseContext());

        mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentMode.equals("Classic")) {
                    currentMode = "Blitz";
                } else if (currentMode.equals("Blitz")) {
                    currentMode = "Sudden Death";
                } else if (currentMode.equals("Sudden Death")) {
                    currentMode = "Nightmare";
                } else if (currentMode.equals("Nightmare")) {
                    currentMode = "Classic";
                }
                mode.setText(currentMode);
                updateScoreDisplay();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),Menu.class);
                startActivity(i);
            }
        });

    }

    private void updateScoreDisplay(){

        int classicScore = settings.getInt("highScore_Classic", 0);
        int blitzScore = settings.getInt("highScore_Blitz",0);
        int suddenDeathScore = settings.getInt("highScore_Sudden Death",0);
        int nightmareScore = settings.getInt("highScore_Nightmare",0);

        int classicLevel = settings.getInt("highLevel_Classic", 0);
        int blitzLevel = settings.getInt("highLevel_Blitz",0);
        int suddenDeathLevel = settings.getInt("highLevel_Sudden Death",0);
        int nightmareLevel = settings.getInt("highLevel_Nightmare",0);

        String label = "Personal Best:\n\nScore:    ";
        if(currentMode.equals("Classic")){
            displayScore.setText(label+classicScore+"     Level: "+classicLevel);
        }else if(currentMode.equals("Blitz")){
            displayScore.setText(label+blitzScore+"     Level: "+blitzLevel);
        }else if(currentMode.equals("Sudden Death")){
            displayScore.setText(label+suddenDeathScore+"     Level: "+suddenDeathLevel);
        }else if(currentMode.equals("Nightmare")){
            displayScore.setText(label+nightmareScore+"     Level: "+nightmareLevel);
        }
    }

}
