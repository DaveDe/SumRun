package com.example.dave.sumrun;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;

public class Help extends Activity {

    private RelativeLayout rl;
    private TextView tv;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.help);

        try{
            int prevSeed = Integer.parseInt(StaticMethods.readFirstLine("seed.txt", getBaseContext()));
            prevSeed++;
            StaticMethods.write("seed.txt", Integer.toString(prevSeed), getBaseContext());
        }catch(IOException e){}


        rl = (RelativeLayout) findViewById(R.id.relativeLayout);
        tv = (TextView) findViewById(R.id.tv);

        Intent i = getIntent();
        final String sender = i.getStringExtra("class");

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (count) {

                    case 0:
                        Resources res = getResources();
                        Drawable drawable = res.getDrawable(R.mipmap.instruction_2);
                        rl.setBackground(drawable);
                        break;
                    case 1:
                        Resources res2 = getResources();
                        Drawable drawable2 = res2.getDrawable(R.mipmap.instruction_3);
                        rl.setBackground(drawable2);
                        break;
                    case 2:
                        Resources res3 = getResources();
                        Drawable drawable3 = res3.getDrawable(R.mipmap.instruction_4);
                        rl.setBackground(drawable3);
                        tv.setText("Done");
                        break;
                    case 3:
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
        });

    }


}