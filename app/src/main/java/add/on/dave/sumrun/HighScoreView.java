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
    private Button gridSize;
    private Button back;
    private TextView displayScore;

    private String currentMode;
    private String currentGridSize;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        mode = (Button) findViewById(R.id.h_mode_button);
        gridSize = (Button) findViewById(R.id.h_grid_size_button);
        back = (Button) findViewById(R.id.back);
        displayScore = (TextView) findViewById(R.id.display_high_score);

        mode.getBackground().setAlpha(1);
        gridSize.getBackground().setAlpha(1);
        back.getBackground().setAlpha(1);

        settings = getSharedPreferences(GameView.PREFS_NAME_GAME, 0);

        currentMode = settings.getString("mode", "Classic");
        currentGridSize = settings.getString("gridSize", "3x3");

        mode.setText(currentMode);
        gridSize.setText(currentGridSize);

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

        gridSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentGridSize.equals("3x3")) {
                    currentGridSize = "4x4";
                } else if (currentGridSize.equals("4x4")) {
                    currentGridSize = "5x5";
                } else if (currentGridSize.equals("5x5")) {
                    currentGridSize = "3x3";
                }
                gridSize.setText(currentGridSize);
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

        int classic3Score = settings.getInt("highScore_Classic_3x3", 0);
        int classic4Score = settings.getInt("highScore_Classic_4x4",0);
        int classic5Score = settings.getInt("highScore_Classic_5x5",0);
        int blitz3Score = settings.getInt("highScore_Blitz_3x3",0);
        int blitz4Score = settings.getInt("highScore_Blitz_4x4",0);
        int blitz5Score = settings.getInt("highScore_Blitz_5x5",0);
        int suddenDeath3Score = settings.getInt("highScore_Sudden Death_3x3",0);
        int suddenDeath4Score = settings.getInt("highScore_Sudden Death_4x4",0);
        int suddenDeath5Score = settings.getInt("highScore_Sudden Death_5x5",0);
        int nightmare3Score = settings.getInt("highScore_Nightmare_3x3",0);
        int nightmare4Score = settings.getInt("highScore_Nightmare_4x4",0);
        int nightmare5Score = settings.getInt("highScore_Nightmare_5x5",0);

        int classic3Level = settings.getInt("highLevel_Classic_3x3", 0);
        int classic4Level = settings.getInt("highLevel_Classic_4x4",0);
        int classic5Level = settings.getInt("highLevel_Classic_5x5",0);
        int blitz3Level = settings.getInt("highLevel_Blitz_3x3",0);
        int blitz4Level = settings.getInt("highLevel_Blitz_4x4",0);
        int blitz5Level = settings.getInt("highLevel_Blitz_5x5",0);
        int suddenDeath3Level = settings.getInt("highLevel_Sudden Death_3x3",0);
        int suddenDeath4Level = settings.getInt("highLevel_Sudden Death_4x4",0);
        int suddenDeath5Level = settings.getInt("highLevel_Sudden Death_5x5",0);
        int nightmare3Level = settings.getInt("highLevel_Nightmare_3x3",0);
        int nightmare4Level = settings.getInt("highLevel_Nightmare_4x4",0);
        int nightmare5Level = settings.getInt("highLevel_Nightmare_5x5",0);

        if(currentMode.equals("Classic") && currentGridSize.equals("3x3")){
            displayScore.setText("HighScore: "+classic3Score+"\n\nLevel: "+classic3Level);
        }else if(currentMode.equals("Classic") && currentGridSize.equals("4x4")){
            displayScore.setText("HighScore: "+classic4Score+"\n\nLevel: "+classic4Level);
        }else if(currentMode.equals("Classic") && currentGridSize.equals("5x5")){
            displayScore.setText("HighScore: "+classic5Score+"\n\nLevel: "+classic5Level);
        }else if(currentMode.equals("Blitz") && currentGridSize.equals("3x3")){
            displayScore.setText("HighScore: "+blitz3Score+"\n\nLevel: "+blitz3Level);
        }else if(currentMode.equals("Blitz") && currentGridSize.equals("4x4")){
            displayScore.setText("HighScore: "+blitz4Score+"\n\nLevel: "+blitz4Level);
        }else if(currentMode.equals("Blitz") && currentGridSize.equals("5x5")){
            displayScore.setText("HighScore: "+blitz5Score+"\n\nLevel: "+blitz5Level);
        }else if(currentMode.equals("Sudden Death") && currentGridSize.equals("3x3")){
            displayScore.setText("HighScore: "+suddenDeath3Score+"\n\nLevel: "+suddenDeath3Level);
        }else if(currentMode.equals("Sudden Death") && currentGridSize.equals("4x4")){
            displayScore.setText("HighScore: "+suddenDeath4Score+"\n\nLevel: "+suddenDeath4Level);
        }else if(currentMode.equals("Sudden Death") && currentGridSize.equals("5x5")){
            displayScore.setText("HighScore: "+suddenDeath5Score+"\n\nLevel: "+suddenDeath5Level);
        }else if(currentMode.equals("Nightmare") && currentGridSize.equals("3x3")){
            displayScore.setText("HighScore: "+nightmare3Score+"\n\nLevel: "+nightmare3Level);
        }else if(currentMode.equals("Nightmare") && currentGridSize.equals("4x4")){
            displayScore.setText("HighScore: "+nightmare4Score+"\n\nLevel: "+nightmare4Level);
        }else if(currentMode.equals("Nightmare") && currentGridSize.equals("5x5")){
            displayScore.setText("HighScore: "+nightmare5Score+"\n\nLevel: "+nightmare5Level);
        }
    }

}
