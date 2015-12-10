package add.on.dave.sumrun;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class StaticMethods {

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
