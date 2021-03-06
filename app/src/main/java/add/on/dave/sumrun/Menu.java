package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.on.dave.sumrun.R;

import java.io.IOException;

//BUGS
//comment out soundpool?

public class Menu extends Activity {

    private Button done;
    private RelativeLayout rl;
    private Button themeButton;
    private Button modeButton;
    private Button highScores;
    private Button help;
    private ImageButton mute;

    private SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        done = (Button) findViewById(R.id.done);
        themeButton = (Button) findViewById(R.id.theme_button);
        modeButton = (Button) findViewById(R.id.mode_button);
        highScores = (Button) findViewById(R.id.high_scores);
        help = (Button) findViewById(R.id.help);
        mute = (ImageButton) findViewById(R.id.mute);

        String theme = "";
        try{
            theme = StaticMethods.readFirstLine("theme.txt",getBaseContext());
        }catch(IOException e){}
        if(theme.equals("0")){
            theme = "Classic";
        }
        settings = getSharedPreferences(GameView.PREFS_NAME_GAME, 0);
        String mode = settings.getString("mode","Classic");

        themeButton.setText(theme);
        modeButton.setText(mode);

        themeButton.getBackground().setAlpha(1);
        modeButton.getBackground().setAlpha(1);
        done.getBackground().setAlpha(1);
        highScores.getBackground().setAlpha(1);
        help.getBackground().setAlpha(1);

        StaticMethods.changeTheme(rl, getBaseContext());

        if(GameView.isMuted){
            mute.setBackgroundResource(R.drawable.mutedbutton);
        }else{
            mute.setBackgroundResource(R.drawable.unmutedbutton);
        }

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

        highScores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),HighScoreView.class);
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

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(),GameView.class);
                startActivity(i);

            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(),Help.class);
                startActivity(i);

            }
        });

    }

    //back button does nothing
    @Override
    public void onBackPressed() {
    }

}
