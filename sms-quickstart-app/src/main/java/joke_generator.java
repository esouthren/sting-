import java.io.*;

import java.util.ArrayList;
import java.util.Random;

import static java.lang.Math.abs;

public class joke_generator {

    public static String generate_joke() throws FileNotFoundException, IOException{
        ArrayList<String> lines = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader("BeeJokes.txt"));
        String line;
        while ((line = reader.readLine()) != null)
        {
            lines.add(line);
        }
        reader.close();
        Random rn = new Random();
        int i = abs(rn.nextInt())%lines.size();
        return lines.get(i);
    }
}
