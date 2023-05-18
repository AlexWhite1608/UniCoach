package manager_implementation;

import data_access.DBConnection;
import domain_model.Course;
import domain_model.Exam;
import domain_model.Student;
import org.jfree.data.general.DefaultPieDataset;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class StudyTimeManager {

    //FIXME: non so come testarlo
    public static void setDailyStudyTime(Student student, boolean executeOnce) {
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    compileForm(student);
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
    public static void compileForm(Student student) throws SQLException {

        //Per ogni corso memorizzo quante ore ho studiato per ciascuna tipologia

        Map<Exam, Map<StudyType, Integer>> studyInfo = new HashMap<>();

        //Inserimento delle informazioni di studio
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digita il nome della materia che hai studiato oggi: ");
        String examName = scanner.nextLine();

        while (!examName.equals("0")) {
            Exam exam = student.getUniTranscript().findExamByName(examName);

            if(exam != null){
                System.out.println("Digita cosa hai studiato per " + exam.getName() + " (Lezione, Ripasso, Progetto, Studio per esame): ");
                String studyTypeString = scanner.nextLine();

                //TODO: fai controllo che abbia scritto per bene lo study type e le ore di studio!

                StudyType studyType = StudyType.getStudyTypeFromString(studyTypeString);

                System.out.println("Digita le ore impiegate nell'attività: ");
                int hours = scanner.nextInt();
                scanner.nextLine();

                //Inserisco le informazioni nelle Mappe
                //FIXME: controlla che la chiave (nome esame) non sia già stata inserita, altrimenti aggiorna il valore corrispondente!
                Map<StudyType, Integer> dedicatedStudy = new HashMap<>();
                dedicatedStudy.put(studyType, hours);

                studyInfo.put(exam, dedicatedStudy);

            } else {
                System.out.println("Il corso inserito non esiste");
                continue;
            }

            System.out.println("Digita il nome successivo (oppure premi 0 per terminare): ");

            examName = scanner.nextLine();
        }

        // Inserisco le informazioni di studio da studyInfo al database
        insertInfoInDb(studyInfo);

    }

    private static void insertInfoInDb(Map<Exam, Map<StudyType, Integer>> studyInfo) throws SQLException {

        String sql = "INSERT OR IGNORE INTO OreStudio (Codice, TipoStudio, Ore)" +
                     "VALUES (?, ?, ?)" +
                     "ON CONFLICT (Codice, TipoStudio) DO UPDATE SET Ore = Ore + excluded.Ore";

        Connection connection = DBConnection.connect("../database/unicoachdb.db");
        PreparedStatement statement = connection.prepareStatement(sql);

        for(Map.Entry<Exam, Map<StudyType, Integer>> entry : studyInfo.entrySet()) {
            Exam exam = entry.getKey();
            Map<StudyType, Integer> values = entry.getValue();

            for(Map.Entry<StudyType, Integer> value : values.entrySet()){

                statement.setString(1, exam.getId());
                statement.setString(2, value.getKey().getDisplayName());
                statement.setInt(3, value.getValue());

                statement.executeUpdate();
            }

        }

        statement.close();
        DBConnection.disconnect();
    }

    //Il professore richiede le informazioni di studio degli studenti iscritti al suo corso -> numero di ore spese confrontato con il voto ottenuto?
    public static void getCourseStudyInfo(Course course) throws SQLException {

        String sql = """
                SELECT TipoStudio, SUM(Ore) AS TotaleOre
                FROM OreStudio
                WHERE Codice = (SELECT Codice
                                FROM Esame
                                WHERE Corso = ?)
                GROUP BY TipoStudio""";

        Connection connection = DBConnection.connect("../database/unicoachdb.db");
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, course.getId());
        ResultSet resultSet = statement.executeQuery();

        Map<StudyType, Integer> studyHoursByType = new HashMap<>();
        while (resultSet.next()) {
            String studyTypeString = resultSet.getString("TipoStudio");
            int totalHours = resultSet.getInt("TotaleOre");

            StudyType studyType = StudyType.getStudyTypeFromString(studyTypeString);
            studyHoursByType.put(studyType, totalHours);
        }

        resultSet.close();
        statement.close();
        DBConnection.disconnect();

        //TODO: in qualche modo fare i grafici con le informazioni contenute in studyHoursByType (richiama GradesManager)

        //Costruisco il dataset del grafico a torta e lo passo alla funzione delegata a costruire il grafico
        DefaultPieDataset dataset = GradesManager.buildStudyTypeDataset(studyHoursByType);
        GradesManager.visualizeStudyTypeData(dataset, course);
    }

    //Il professore richiede le informazioni di studio degli studenti per tutti i corsi -> numero di ore spese confrontato con il voto ottenuto?
    public static void getAllCoursesStudyInfo() {
        //TODO: in qualche modo fare anche i grafici!
    }

    public static void setPeriod(int period) {
        StudyTimeManager.period = period;
    }

    public static void setDelay(int delay) {
        StudyTimeManager.delay = delay;
    }

    private static int period = 1000;   // Tempo in millisecondi tra esecuzioni successive del task
    private static int delay = 600;     // Tempo in millisecondi prima che il task venga eseguito.
}
