import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class SoundLevelSensor
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        String fileName = "/app/data/soundlevel.txt";
        BufferedWriter bufferedWriter = null;
        Random random = new Random();
        while (true)
        {
            bufferedWriter = new BufferedWriter(new FileWriter(fileName, false));
            for (int i = 0; i < 10; i++)
            {
                int randomNumber = random.nextInt(40, 100);
                bufferedWriter.write(String.valueOf(randomNumber));
                bufferedWriter.write(" ");
            }
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
            System.out.println("New Values Detected!");
            Thread.sleep(20000);
        }


    }
}