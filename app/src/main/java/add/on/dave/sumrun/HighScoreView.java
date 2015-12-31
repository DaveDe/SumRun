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

    private TextView display;
    private RelativeLayout rl;
    private Button back;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        display = (TextView) findViewById(R.id.display);
        back = (Button) findViewById(R.id.back);

        back.getBackground().setAlpha(1);

        settings = getSharedPreferences(GameView.PREFS_NAME_GAME, 0);

        StaticMethods.changeTheme(rl,getBaseContext());

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

        display.setText(
                "Classic     3x3         "+classic3Score+"        "+classic3Level + "\n" +
                "Classic     4x4         "+classic4Score+"        "+classic4Level + "\n" +
                "Classic     5x5         "+classic5Score+"        "+classic5Level + "\n" +
                "Blitz           3x3         "+blitz3Score+"        "+blitz3Level + "\n" +
                        "Blitz           4x4         "+blitz4Score+"        "+blitz4Level + "\n" +
                        "Blitz           5x5         "+blitz5Score+"        "+blitz5Level + "\n" +
                        "Sudden Death    3x3         "+suddenDeath3Score+"        "+suddenDeath3Level + "\n" +
                        "Sudden Death    4x4         "+suddenDeath4Score + "        " + suddenDeath4Level + "\n" +
                        "Sudden Death    5x5         "+suddenDeath5Score + "        " + suddenDeath5Level + "\n" +
                        "Nightmare       3x3         "+nightmare3Score + "        " + nightmare3Level + "\n" +
                        "Nightmare       4x4         "+nightmare4Score + "        " + nightmare4Level + "\n" +
                        "Nightmare       5x5         "+nightmare5Score + "        " + nightmare5Level + "\n" );

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),Menu.class);
                startActivity(i);
            }
        });

    }

}
