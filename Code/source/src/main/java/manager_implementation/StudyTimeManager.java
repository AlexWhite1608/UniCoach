package manager_implementation;

import java.util.Timer;
import java.util.TimerTask;

//TODO: la classe deve gestire la suddivisione dello studio dello studente?
public class StudyTimeManager {

    public static void setDailyStudyTime() {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                //TODO: qui chiami il metodo che deve essere svolto periodicamente
                compileForm();
            }
        };

        timer.scheduleAtFixedRate(timerTask, delay, period);
    }

    // Qui lo studente inserisce i dati relativi allo studio giornaliero
    private static void compileForm(){

    }

    private static int period = 1000;   // Tempo in millisecondi tra esecuzioni successive del task
    private static int delay = 500;     // Tempo in millisecondi prima che il task venga eseguito.
}
