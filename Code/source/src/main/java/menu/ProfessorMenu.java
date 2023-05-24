package menu;

import controller.Controller;
import domain_model.Professor;
import javax.mail.MessagingException;
import javax.naming.directory.InvalidAttributesException;
import java.sql.SQLException;
import java.util.Scanner;

public class ProfessorMenu {

    public void displayMenu(Professor professor) throws SQLException, MessagingException, InvalidAttributesException {
        Scanner scanner = new Scanner(System.in);
        boolean terminate = false;
        int choice;
        controller = new Controller(professor);

        while (!terminate){
            System.out.println("Scegli l'azione da compiere tra: \n" + actionList);
            choice = scanner.nextInt();

            switch (choice) {
                case 1:     //Visualizza le attività
                    controller.displayActivities();
                break;

                case 2:     //Inserisci date esame
                    System.out.println("Inserire la data: ");
                    String dateExam = scanner.nextLine();

                    System.out.println("Inserire l'ora di inizio: ");
                    int startExam = scanner.nextInt();

                    System.out.println("Inserire l'ora di fine: ");
                    int endExam = scanner.nextInt();

                    controller.addExamDate(dateExam, startExam, endExam);
                    break;

                case 3:     //Inserisci date lezioni
                    System.out.println("Inserire il giorno: ");
                    int day = scanner.nextInt();

                    System.out.println("Inserire il mese: ");
                    int month = scanner.nextInt();

                    System.out.println("Inserire l'anno: ");
                    int year = scanner.nextInt();

                    System.out.println("Inserire l'ora di inizio: ");
                    int startLesson = scanner.nextInt();

                    System.out.println("Inserire l'ora di fine: ");
                    int endLesson = scanner.nextInt();

                    controller.scheduleLessons(day, month, year, startLesson, endLesson);
                    break;

                case 4:     //Aggiungi note alla lezione
                    System.out.println("Inserire la data: ");
                    String dateNotes = scanner.nextLine();

                    System.out.println("Inserire le note: ");
                    String notes = scanner.nextLine();

                    controller.addLectureNotes(dateNotes, notes);

                case 5:     //Inserisci voto studente
                    System.out.println("Inserire la matricola dello studente: ");
                    String studentId = scanner.nextLine();

                    System.out.println("Inserire la data dell'esame svolto: ");
                    String examDate = scanner.nextLine();

                    System.out.println("Inserire il voto: ");
                    int studentGrade = scanner.nextInt();

                    controller.setGrade(studentId, examDate, studentGrade);
                    break;

                case 6:     //Ottieni voto studente
                    System.out.println("Inserire la matricola dello studente: ");
                    studentId = scanner.nextLine();

                    int grade = controller.getGradeFromProfessor(studentId);

                    System.out.println("Lo studente con matricola " + studentId + " ha preso: " + grade);
                    break;

                case 7:     //Ottieni media studente
                    System.out.println("Inserire la matricola dello studente: ");
                    studentId = scanner.nextLine();

                    float avg = controller.getAverageStudent(studentId);

                    System.out.println("Lo studente con matricola " + studentId + " ha media: " + avg);
                    break;

                case 8:     //Ottieni media corso
                    float avgCourse = controller.getAverageCourse();

                    System.out.println("La media del corso vale: " + avgCourse);
                    break;

                case 9:     //Visualizza esami studente
                    System.out.println("Inserire la matricola dello studente: ");
                    studentId = scanner.nextLine();

                    controller.displayStudentExamsGraph(studentId);
                    break;

                case 10:    //Visualizza esami corso
                    controller.displayCourseExamsGraph();
                    break;

                case 11:    //Visualizza tutti i corsi
                    controller.displayAllCoursesGraph();
                    break;

                case 12:    //Visualizza study time studente
                    System.out.println("Inserire la matricola dello studente: ");
                    studentId = scanner.nextLine();

                    controller.displayStudentStudyTime(studentId);
                    break;

                case 13:    //Visualizza study time corso
                    controller.displayCourseStudyInfo();
                    break;
            }
        }


    }

    private final String actionList = """
            1: Visualizza le attività
            2: Inserisci date esame
            3: Inserisci date lezioni
            4: Aggiungi note alla lezione
            5: Inserisci voto studente
            6: Ottieni voto studente
            7: Ottieni media studente
            8: Ottieni media corso
            9: Visualizza esami studente
            10: Visualizza esami corso
            11: Visualizza tutti i corsi
            12: Visualizza study time studente
            13: Visualizza study time corso
            """;

    private Controller controller = null;
}
