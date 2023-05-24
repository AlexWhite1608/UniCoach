package manager;

import data_access.ManagerGateway;
import domain_model.Course;
import domain_model.Exam;
import domain_model.Student;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.sql.SQLException;
import java.util.*;

public class StudyTimeManager {

    // Qui lo studente inserisce i dati relativi allo studio giornaliero
    public static void compileForm(Student student) throws SQLException {

        //Per ogni corso memorizzo quante ore ho studiato per ciascuna tipologia
        Map<Exam, List<Map<StudyType, Integer>>> studyInfo = new HashMap<>();

        //Inserimento delle informazioni di studio
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digita il nome della materia che hai studiato oggi: ");
        String examName = scanner.nextLine();

        while (!examName.equals("0")) {
            Exam exam = student.getUniTranscript().findExamByName(examName);

            if(exam != null){
                System.out.println("Digita cosa hai studiato per " + exam.getName() + " (Lezione, Ripasso, Progetto, Studio per esame): ");
                String studyTypeString = scanner.nextLine();

                List<Map<StudyType, Integer>> dedicatedStudyList = new ArrayList<>();

                while(!studyTypeString.equals("0")){
                    //TODO: fai controllo che abbia scritto per bene lo study type e le ore di studio!

                    StudyType studyType = StudyType.getStudyTypeFromString(studyTypeString);

                    System.out.println("Digita le ore impiegate nell'attività: ");
                    int hours = scanner.nextInt();
                    scanner.nextLine();

                    //Inserisco le informazioni nella Mappa
                    Map<StudyType, Integer> dedicatedStudy = new HashMap<>();

                    dedicatedStudy.put(studyType, hours);
                    dedicatedStudyList.add(dedicatedStudy);

                    System.out.println("Digita cosa hai studiato per " + exam.getName() + " (Lezione, Ripasso, Progetto, Studio per esame) oppure digita 0 per terminare: ");
                    studyTypeString = scanner.nextLine();
                }

                // Verifica se l'exam esiste già in studyInfo
                if(studyInfo.containsKey(exam)){
                    List<Map<StudyType, Integer>> existingStudyList = studyInfo.get(exam);
                    existingStudyList.addAll(dedicatedStudyList);
                } else {
                    studyInfo.put(exam, dedicatedStudyList);
                }

            } else {
                System.out.println("Il corso inserito non esiste");
                continue;
            }

            System.out.println("Digita il nome successivo (oppure premi 0 per terminare): ");
            if (scanner.hasNextLine()) {
                examName = scanner.nextLine();
            } else {
                break;
            }
        }

        // Inserisco le informazioni di studio da studyInfo al database
        insertInfoInDb(studyInfo);
    }

    //Inserisce le informazioni contenute in studyInfo nel db (tabella OreStudio)
    private static void insertInfoInDb(Map<Exam, List<Map<StudyType, Integer>>> studyInfo) throws SQLException {
        ManagerGateway.insertInfoInDb(studyInfo);
    }

    //Il professore richiede le informazioni di studio degli studenti iscritti al suo corso -> numero di ore spese confrontato con il voto ottenuto?
    public static void getCourseStudyInfo(Course course) throws SQLException {

        Map<StudyType, Integer> studyHoursByType = ManagerGateway.getCourseStudyInfo(course);

        //Costruisco il dataset del grafico a torta e lo passo alla funzione delegata a costruire il grafico
        DefaultPieDataset dataset = ChartManager.buildStudyTypeDataset(studyHoursByType);
        ChartManager.displayStudyTypeData(dataset, course);
    }

    //Serve sia allo studente che al professore per vedere quanto ha studiato con istogramma
    public static void getStudentStudyInfo(Student student) throws SQLException {
        Map<Exam, List<Map<StudyType, Integer>>> info = ManagerGateway.getStudentStudyInfo(student);

        DefaultCategoryDataset dataset = ChartManager.getStudentInfoDataset(info);
        ChartManager.displayStudentStudyInfo(dataset, student);
    }
}