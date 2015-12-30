package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.on.dave.sumrun.R;

import java.io.IOException;

//DISCUSS
//show correct path at gameover
//move mute button from gameover to menu
//move help to menu, possibly keep on game screen and gameover
//highscore page design

//TODO
//gridsize
//highscores

//BUGS
//write to memory using shared preferences
//comment out soundpool?
//tile spacing


public class Menu extends Activity {

    private Button done;
    private RelativeLayout rl;
    private Button themeButton;
    private Button modeButton;
    private Button gridSizeButton;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        done = (Button) findViewById(R.id.done);
        themeButton = (Button) findViewById(R.id.theme_button);
        modeButton = (Button) findViewById(R.id.mode_button);
        gridSizeButton = (Button) findViewById(R.id.grid_size_button);

        String theme = "";
        try{
            theme = StaticMethods.readFirstLine("theme.txt",getBaseContext());
        }catch(IOException e){}
        settings = getSharedPreferences(GameView.PREFS_NAME_GAME, 0);
        String mode = settings.getString("mode","Classic");
        String gridSize = settings.getString("gridSize","3x3");

        themeButton.setText(theme);
        modeButton.setText(mode);
        gridSizeButton.setText(gridSize);

        themeButton.getBackground().setAlpha(1);
        modeButton.getBackground().setAlpha(1);
        gridSizeButton.getBackground().setAlpha(1);
        done.getBackground().setAlpha(1);

        StaticMethods.changeTheme(rl, getBaseContext());

        themeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Themes.class);
                startActivity(i);
            }
        });

        modeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Modes.class);
                startActivity(i);
            }
        });

        gridSizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),GridSize.class);
                startActivity(i);
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(),GameView.class);
                startActivity(i);

            }
        });

    }

    //back button does nothing
    @Override
    public void onBackPressed() {
    }

}
