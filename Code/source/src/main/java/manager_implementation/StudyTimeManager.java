package manager_implementation;

import data_access.DBConnection;
import domain_model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

public class StudyTimeManager {

    //executeOnce si usa quando si testa per farlo eseguire una volta sola, poi si lascia sempre false!
    public static void setDailyStudyTime(boolean executeOnce) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    compileForm();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                if (executeOnce) {
                    timer.cancel(); // Interrompe il timer dopo la prima esecuzione
                }
            }
        };

        timer.scheduleAtFixedRate(timerTask, delay, period);
    }


    // Qui lo studente inserisce i dati relativi allo studio giornaliero
    private static void compileForm() throws SQLException {
        //TODO: Per ciascuna materia alla quale lo studente è iscritto, deve inserire giornalmente lo studio e aggiornare le ore già presenti

        //Per ogni corso memorizzo quante ore ho studiato per ciascuna tipologia
        Map<StudyType, Integer> dedicatedStudy = new HashMap<>();
        Map<Course, Map<StudyType, Integer>> studyInfo = new HashMap<>();

        //Inserimento delle informazioni di studio
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digita il nome della materia che hai studiato oggi: ");
        String courseName = scanner.nextLine();

        while (!courseName.equals("0")) {
            Course course = CoursesManager.findCourseByName(courseName);

            System.out.println("Digita il nome successivo (oppure premi 0 per terminare): ");

            if(course != null){
                System.out.println("Digita cosa hai studiato per " + course.getName() + " (Lezione, Ripasso, Progetto, Studio per esame): ");
                String studyTypeString = scanner.nextLine();

                //TODO: fai controllo che abbia scritto per bene lo study type e le ore di studio!

                StudyType studyType = StudyType.getStudyTypeFromString(studyTypeString);

                System.out.println("Digita le ore impiegate nell'attività: ");
                int hours = scanner.nextInt();

                //Inserisco le informazioni nella Map
                dedicatedStudy.put(studyType, hours);
                studyInfo.put(course, dedicatedStudy);

            } else {
                System.out.println("Il corso inserito non esiste");
                continue;
            }

            courseName = scanner.nextLine();
        }

        // Inserisco le informazioni di studio da studyInfo al database
        insertInfoInDb(studyInfo);

    }

    private static void insertInfoInDb(Map<Course, Map<StudyType, Integer>> studyInfo) throws SQLException {

        String sql = "INSERT INTO TabellaOreStudio (Codice, TipoStudio, Ore)" +
                     "VALUES (?, ?, ?)" +
                     "ON DUPLICATE KEY UPDATE Ore = Ore + VALUES(Ore)";

        Connection connection = DBConnection.connect("../database/unicoachdb.db");
        PreparedStatement statement = connection.prepareStatement(sql);

        for(Map.Entry<Course, Map<StudyType, Integer>> entry : studyInfo.entrySet()) {
            Course course = entry.getKey();
            Map<StudyType, Integer> values = entry.getValue();

            for(Map.Entry<StudyType, Integer> value : values.entrySet()){

                //FIXME: ora inserisco come Codice l'id del corso, ma bisogna mettere l'id dell'esame??
                statement.setString(1, course.getId());
                statement.setString(2, value.getKey().getDisplayName());
                statement.setInt(3, value.getValue());

                statement.executeUpdate();
            }

        }

        statement.close();
        DBConnection.disconnect();
    }

    public static void setPeriod(int period) {
        StudyTimeManager.period = period;
    }

    public static void setDelay(int delay) {
        StudyTimeManager.delay = delay;
    }

    private static int period = 1000;   // Tempo in millisecondi tra esecuzioni successive del task
    private static int delay = 500;     // Tempo in millisecondi prima che il task venga eseguito.
}
