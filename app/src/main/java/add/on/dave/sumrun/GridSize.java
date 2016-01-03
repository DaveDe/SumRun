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

public class GridSize extends Activity {

    private String mode;

    private RelativeLayout rl;
    private TextView displayGridSize;
    private TextView displayWarning;
    private Button three;
    private Button four;
    private Button five;
    private Button back;

    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_size);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        displayGridSize = (TextView) findViewById(R.id.current_grid_size);
        displayWarning = (TextView) findViewById(R.id.display_warning);
        three = (Button) findViewById(R.id.three);
        four = (Button) findViewById(R.id.four);
        five = (Button) findViewById(R.id.five);
        back = (Button) findViewById(R.id.back);

        three.getBackground().setAlpha(1);
        four.getBackground().setAlpha(1);
        five.getBackground().setAlpha(1);
        back.getBackground().setAlpha(1);

        settings = getSharedPreferences(GameView.PREFS_NAME_GAME, 0);
        editor = settings.edit();

        StaticMethods.changeTheme(rl, getBaseContext());
        String currentGridSize = settings.getString("gridSize","3x3");
        displayGridSize.setText("Current Size:   " + currentGridSize);

        mode = settings.getString("mode", "Classic");

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("Blitz")){
                    editor.putInt("time", 6);
                }else{
                    editor.putInt("time", 11);
                }
                editor.putString("gridSize", "3x3");
                editor.putInt("score", 0);
                editor.putInt("level", 1);
                editor.putInt("global", 0);
                editor.putBoolean("restore", false);
                editor.commit();
                displayGridSize.setText("Current Size:   3x3");
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("Blitz")){
                    editor.putInt("time", 6);
                }else{
                    editor.putInt("time", 13);
                }
                editor.putString("gridSize", "4x4");
                editor.putInt("score", 0);
                editor.putInt("level", 1);
                editor.putInt("global", 0);
                editor.putBoolean("restore", false);
                editor.commit();
                displayGridSize.setText("Current Size:   4x4");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mode.equals("Blitz")){
                    editor.putInt("time", 6);
                }else{
                    editor.putInt("time", 16);
                }
                editor.putString("gridSize", "5x5");
                editor.putInt("score", 0);
                editor.putInt("level", 1);
                editor.putInt("global", 0);
                editor.putBoolean("restore", false);
                editor.commit();
                displayGridSize.setText("Current Size:   5x5");
            }
        });

        displayWarning.setText("Warning: Changing grid size\nwill end your current game!");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), Menu.class);
                startActivity(i);
            }
        });

    }

}
