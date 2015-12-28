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

import java.io.IOException;

//TODO
//gridsize
//sound for themes

//BUGS
//write to memory using shared preferences
//comment out soundpool?
//tile spacing


public class Menu extends Activity {

    public static final String PREFS_NAME_GAME = "game_data3";

    private Button done;
    private RelativeLayout rl;
    private TextView themeButton;
    private TextView modeButton;
    private TextView gridSizeButton;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        done = (Button) findViewById(R.id.done);
        themeButton = (TextView) findViewById(R.id.theme_button);
        modeButton = (TextView) findViewById(R.id.mode_button);
        gridSizeButton = (TextView) findViewById(R.id.grid_size_button);

        String theme = "";
        try{
            theme = StaticMethods.readFirstLine("theme.txt",getBaseContext());
        }catch(IOException e){}
        settings = getSharedPreferences(PREFS_NAME_GAME, 0);
        String mode = settings.getString("mode","Classic");

        themeButton.setText(theme);
        modeButton.setText(mode);

        StaticMethods.changeTheme(rl,getBaseContext());

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
                Intent i = new Intent(getBaseContext(),Modes.class);
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
