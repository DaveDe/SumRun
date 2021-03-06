package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.on.dave.sumrun.R;

import java.io.IOException;

public class Help extends Activity {

    private RelativeLayout rl;
    private TextView tv;
    private TextView tv2;
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        try{
            int prevSeed = Integer.parseInt(StaticMethods.readFirstLine("seed.txt", getBaseContext()));
            prevSeed++;
            StaticMethods.write("seed.txt", Integer.toString(prevSeed), getBaseContext());
        }catch(IOException e){}


        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        tv = (TextView) findViewById(R.id.tv);
        tv2 = (TextView) findViewById(R.id.tv2);

        count = 1;

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeInstruction();

            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count > 0){
                    count-=2;
                    changeInstruction();
                }
                if(count == 0){
                    tv2.setText("");
                }

            }
        });

    }

    private void changeInstruction(){

        switch (count) {

            case 0:
                rl.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.instruction_1));
                tv.setText("Next");
                tv2.setText("");
                break;
            case 1:
                rl.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.instruction_2));
                tv.setText("Next");
                tv2.setText("Prev");
                break;
            case 2:
                rl.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.instruction_3));
                tv.setText("Next");
                tv2.setText("Prev");
                break;
            case 3:
                rl.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.instruction_4));
                tv.setText("Next");
                tv2.setText("Prev");
                break;
            case 4:
                rl.setBackground(ContextCompat.getDrawable(getBaseContext(), R.drawable.instruction_5));
                tv.setText("Done");
                tv2.setText("Prev");
                break;
            case 5:
                Intent i = new Intent(getBaseContext(), Menu.class);
                startActivity(i);
                break;
        }

        count++;

    }

    //back button does nothing
    @Override
    public void onBackPressed() {
    }


}