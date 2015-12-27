package add.on.dave.sumrun;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.RelativeLayout;

import com.on.dave.sumrun.R;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StaticMethods {

    public static void changeTheme(RelativeLayout rl, Context c){
        try{
            String theme = StaticMethods.readFirstLine("theme.txt",c);
                switch (theme) {
                    case "Classic":
                        rl.setBackground(ContextCompat.getDrawable(c, R.drawable.classic));
                        break;
                    case "Daylight":
                        rl.setBackground(ContextCompat.getDrawable(c, R.drawable.daylight));
                        break;
                    case "Midnight":
                        rl.setBackground(ContextCompat.getDrawable(c, R.drawable.midnight));
                        break;
                    default:
                        rl.setBackground(ContextCompat.getDrawable(c, R.drawable.classic));
                }
        }catch(IOException e){}
    }

    public static void write (String filename, String data, Context c) throws IOException{
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(c.openFileOutput(filename, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
        }
    }

    public static String readFirstLine (String filename,Context c) throws IOException{
        String ret = "0";
        try {
            InputStream inputStream = c.openFileInput(filename);
            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader br = new BufferedReader(inputStreamReader);
                ret = br.readLine();
                inputStream.close();
            }
        }
        catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
        return ret;
    }

}
