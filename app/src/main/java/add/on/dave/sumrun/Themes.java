package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.on.dave.sumrun.R;

import java.io.IOException;

public class Themes extends Activity {

    private RelativeLayout rl;
    private TextView currentThemeDisplay;
    private Button classic;
    private Button daylight;
    private Button midnight;
    private Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.themes);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        currentThemeDisplay = (TextView) findViewById(R.id.current_theme);
        classic = (Button) findViewById(R.id.classic);
        daylight = (Button) findViewById(R.id.daylight);
        midnight = (Button) findViewById(R.id.midnight);
        back = (Button) findViewById(R.id.back);

        StaticMethods.changeTheme(rl,getBaseContext());

        changeThemeDisplay();

        classic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.classic));
                try {
                    StaticMethods.write("theme.txt", "Classic", getBaseContext());
                } catch (IOException e) {
                }
                changeThemeDisplay();
            }
        });

        daylight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.daylight));
                try {
                    StaticMethods.write("theme.txt", "Daylight", getBaseContext());
                } catch (IOException e) {
                }
                changeThemeDisplay();
            }
        });

        midnight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.midnight));
                try {
                    StaticMethods.write("theme.txt", "Midnight", getBaseContext());
                } catch (IOException e) {
                }
                changeThemeDisplay();
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

    private void changeThemeDisplay(){
        try{
            String theme = StaticMethods.readFirstLine("theme.txt",getBaseContext());
            if(theme == null || theme.equals("0")){
                currentThemeDisplay.setText("Current Theme:   Classic");
            }else{
                currentThemeDisplay.setText("Current Theme:   "+theme);
            }
        }catch(IOException e){}
    }

    //back button does nothing
    @Override
    public void onBackPressed() {
    }

}
