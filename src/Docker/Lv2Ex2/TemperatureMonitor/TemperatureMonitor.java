
import java.io.*;
import java.util.Arrays;

class TemperatureProcessor
{
    public static final int LOW_BOUND = 5;
    public static final int MEDIUM_BOUND = 19;
    public static final int HIGH_BOUND = 35;

    public static double calculateAverageTemperature(String data)
    {
        return Arrays.stream(data.split("\\s+"))
                .mapToDouble(s -> Double.parseDouble(s))
                .average()
                .orElse(0);
    }

    public static String determineAverage(double averageTemperature)
    {
        if (averageTemperature >= LOW_BOUND && averageTemperature < MEDIUM_BOUND)
        {
            return "Low";
        }
        if (averageTemperature >= MEDIUM_BOUND && averageTemperature < HIGH_BOUND)
        {
            return "Medium";
        }
        if (averageTemperature >= HIGH_BOUND)
        {
            return "High";
        }
        return "Invalid State";
    }
}


public class TemperatureMonitor
{
    public static void main(String[] args) throws IOException, InterruptedException
    {
        String inputFileName = System.getenv("FILE_IN_PATH");
        String outputFileName = System.getenv("FILE_OUT_PATH");
        int timeToWait = 60000;
        BufferedReader reader = null;
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName, true));

        while (true)
        {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFileName)));
            double averageTemperature = TemperatureProcessor.calculateAverageTemperature(reader.readLine());
            String average = TemperatureProcessor.determineAverage(averageTemperature);
            writer.write(average);
            writer.newLine();
            writer.flush();
            reader.close();
            System.out.println("PROCESSED TEMPERATURES!");
            Thread.sleep(timeToWait);
        }
    }
}