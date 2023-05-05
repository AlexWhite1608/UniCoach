package manager_implementation;

import java.util.Arrays;

//FIXME: CAPIRE COSA FARE CON QUESTA CLASSE!!
public class CalendarManager {

    public CalendarManager() {

        //Inizializza il calendario vuoto
        calendar = new String[7][24];
        for (String[] row : calendar) {
            Arrays.fill(row, "");
        }


    }

    public void addActivity(String activity, int day, int startTime, int endTime) {
        if (day >= 0 && day <= 6 && startTime >= 0 && endTime >= 0 && endTime <= 23 && startTime < endTime) {
            for (int i = startTime; i < endTime; i++) {
                calendar[day][i] = activity;
            }
        } else {
            throw new IllegalArgumentException("Giorno o orario non valido");
        }
    }

    public void removeActivity(int day, int startTime, int endTime) {
        if (day >= 0 && day <= 6 && startTime >= 0 && endTime >= 0 && endTime <= 23 && startTime < endTime) {
            for (int i = startTime; i < endTime; i++) {
                calendar[day][i] = "";
            }
        } else {
            throw new IllegalArgumentException("Giorno o orario non valido");
        }
    }

    public void printCalendar() {
        String[] days = {"Lunedì", "Martedì", "Mercoledì", "Giovedì", "Venerdì", "Sabato", "Domenica"};
        System.out.println("\t\t\tCALENDARIO SETTIMANALE\n");
        System.out.println("\t\t" + String.join("\t\t", days) + "\n");

        for (int i = 0; i < 24; i++) {
            String[] time = new String[7];
            for (int j = 0; j < 7; j++) {
                time[j] = calendar[j][i];
            }
            System.out.println(String.format("%02d", i) + ":00\t\t" + String.join("\t\t", time));
        }
    }

    public String[][] getCalendar() {
        return calendar;
    }

    private String[][] calendar;


}
