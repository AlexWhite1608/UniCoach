package utility;

import java.util.List;

public class AverageCalculator {

    //Calcola la media dei voti forniti in input
    public static double calculateAverage(List<Integer> list) {
        int sum = 0;
        for (int value : list) {
            if(value != -1)
                sum += value;
        }
        return (float) sum / list.size();
    }
}
