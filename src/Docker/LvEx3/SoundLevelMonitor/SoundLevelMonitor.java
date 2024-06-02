/*

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

class WeatherDataProcessor
{

    public static final int LOW = Integer.parseInt(System.getenv("LOW_SOUNDLEVEL"));
    public static final int MEDIUM  = Integer.parseInt(System.getenv("MEDIUM_SOUNDLEVEL"));
    public static final int HIGH = Integer.parseInt(System.getenv("HIGH_SOUNDLEVEL"));
    public static String processData(List<Integer> data)
    {
        double averageValue = data.stream().mapToDouble(a -> a.doubleValue()).average().orElse(0);

        if (averageValue >= LOW && averageValue < MEDIUM) return "Low";
        if (averageValue >= MEDIUM && averageValue < HIGH) return "Medium";
        if (averageValue >= HIGH) return "High";

        return "Invalid Value";
    }
}

public class SoundLevelMonitor
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        String inputFilePath = "/app/dataIn/soundlevel.txt";
        String outputFilePath = "/app/dataOut/noisepollution.txt";

        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outputFilePath, true));
        String line = null;

        while (true)
        {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath)));
            line = bufferedReader.readLine();
            List<Integer> data = Arrays.stream(line.split("\\s+")).mapToInt(c -> Integer.parseInt(c)).boxed().collect(Collectors.toList());
            bufferedReader.close();

            String outcome = WeatherDataProcessor.processData(data);
            bufferedWriter.write(outcome);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Processed New Data!");
            Thread.sleep(30000);
        }
    }
}*/
