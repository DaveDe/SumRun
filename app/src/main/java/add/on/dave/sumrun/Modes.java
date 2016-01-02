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

public class Modes extends Activity {

    private String gridSize;

    private RelativeLayout rl;
    private TextView displayMode;
    private TextView displayWarning;
    private Button classic;
    private Button blitz;
    private Button suddenDeath;
    private Button nightmare;
    private Button back;

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modes);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        displayMode = (TextView) findViewById(R.id.current_mode);
        displayWarning = (TextView) findViewById(R.id.display_warning);
        classic = (Button) findViewById(R.id.classic);
        blitz = (Button) findViewById(R.id.blitz);
        suddenDeath = (Button) findViewById(R.id.sudden_death);
        nightmare = (Button) findViewById(R.id.nightmare);
        back = (Button) findViewById(R.id.back);

        classic.getBackground().setAlpha(1);
        blitz.getBackground().setAlpha(1);
        suddenDeath.getBackground().setAlpha(1);
        nightmare.getBackground().setAlpha(1);
        back.getBackground().setAlpha(1);

        settings = getSharedPreferences(GameView.PREFS_NAME_GAME, 0);
        editor = settings.edit();

        StaticMethods.changeTheme(rl, getBaseContext());
        String currentMode = settings.getString("mode","Classic");
        displayMode.setText("Current Mode:   " + currentMode);

        gridSize = settings.getString("gridSize","3x3");

        classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("mode", "Classic");
                if (gridSize.equals("3x3")) {
                    editor.putInt("time", 11);
                } else {
                    editor.putInt("time", 16);
                }
                editor.putInt("score", 0);
                editor.putInt("level", 1);
                editor.putInt("global", 0);
                editor.putBoolean("restore", false);
                editor.commit();
                displayMode.setText("Current Mode:   Classic");
            }
        });

        blitz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("mode", "Blitz");
                editor.putInt("time",6);
                editor.putInt("score", 0);
                editor.putInt("level", 1);
                editor.putInt("global", 0);
                editor.putBoolean("restore", false);
                editor.commit();
                displayMode.setText("Current Mode:   Blitz");
            }
        });

        suddenDeath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("mode", "Sudden Death");
                editor.putInt("score", 0);
                editor.putInt("level", 1);
                editor.putInt("global", 0);
                editor.putBoolean("restore", false);
                editor.commit();
                displayMode.setText("Current Mode:   Sudden Death");
            }
        });

        nightmare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("mode", "Nightmare");
                if (gridSize.equals("3x3")) {
                    editor.putInt("time", 11);
                } else {
                    editor.putInt("time", 16);
                }
                editor.putInt("score", 0);
                editor.putInt("level", 1);
                editor.putInt("global", 0);
                editor.putBoolean("restore", false);
                editor.commit();
                displayMode.setText("Current Mode:   Nightmare");
            }
        });

        displayWarning.setText("Warning: Changing game modes\nwill end your current game!");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Menu.class);
                startActivity(i);
            }
        });

    }

}
