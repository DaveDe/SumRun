package add.on.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

        Intent i = getIntent();
        final String sender = i.getStringExtra("class");

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                changeInstruction(sender);

            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count > 0){
                    count-=2;
                    changeInstruction(sender);
                }
                if(count == 0){
                    tv2.setText("");
                }

            }
        });

    }

    private void changeInstruction(String sender){

        switch (count) {

            case 0:
                Resources res = getResources();
                Drawable drawable = res.getDrawable(R.mipmap.instruction_1);
                rl.setBackground(drawable);
                tv.setText("Next");
                tv2.setText("");
                break;
            case 1:
                Resources res2 = getResources();
                Drawable drawable2 = res2.getDrawable(R.mipmap.instruction_2);
                rl.setBackground(drawable2);
                tv.setText("Next");
                tv2.setText("Prev");
                break;
            case 2:
                Resources res3 = getResources();
                Drawable drawable3 = res3.getDrawable(R.mipmap.instruction_3);
                rl.setBackground(drawable3);
                tv.setText("Next");
                tv2.setText("Prev");
                break;
            case 3:
                Resources res4 = getResources();
                Drawable drawable4 = res4.getDrawable(R.mipmap.instruction_4);
                rl.setBackground(drawable4);
                tv.setText("Done");
                tv2.setText("Prev");
                break;
            case 4:
                if (sender.equals("gameOver")) {
                    Intent i = new Intent(getBaseContext(), GameOver.class);
                    startActivity(i);
                } else {
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                }
                break;
        }

        count++;

    }

    //back button does nothing
    @Override
    public void onBackPressed() {
    }


}