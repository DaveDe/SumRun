package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.Releasable;
import com.on.dave.sumrun.R;

//DISCUSSION


//TODO
//themes,modes,gridsize

//BUGS
//comment out soundpool?
//tile spacing


public class Menu extends Activity {

    private Button done;
    private RelativeLayout rl;
    private TextView themeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        done = (Button) findViewById(R.id.done);
        themeButton = (TextView) findViewById(R.id.theme_button);

        StaticMethods.changeTheme(rl,getBaseContext());

        themeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(),Themes.class);
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
}
