/*

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TemperatureSensor
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        BufferedWriter writer = null;
        Random random = new Random();
        String fileName = System.getenv("FILE_OUT_PATH");
        int timeToWait = 30000;
        while (true)
        {
            writer = new BufferedWriter(new FileWriter(fileName, false));
            ArrayList<Integer> data = new ArrayList<>();
            IntStream.range(0, 10)
                    .forEach(a ->
                            data.add(random.nextInt(5, 50))
                    );
            writer.write(data.stream()
                    .map(num -> String.valueOf(num))
                    .collect(Collectors.joining(" ")));
            writer.newLine();
            writer.flush();
            writer.close();
            System.out.println("DETECTED NEW DATA!");
            Thread.sleep(timeToWait);
        }
    }
}*/
