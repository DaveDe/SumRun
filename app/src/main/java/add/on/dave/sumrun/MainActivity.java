package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.on.dave.sumrun.R;

//DISCUSSION
//help to main menu
//go to main menu from game over
//create my own android buttons??
//free update, only adfree version is paid

//TODO
//themes,modes,gridsize

//BUGS
//comment out soundpool?
//tile spacing

public class MainActivity extends Activity {

    private Button play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        play = (Button) findViewById(R.id.play);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getBaseContext(),GameView.class);
                startActivity(i);

            }
        });

    }


}