package menu;

import controller.Controller;
import domain_model.Student;
import manager_implementation.Activity;

import java.sql.SQLException;
import java.util.Scanner;

public class StudentMenu {

    public void displayMenu(Student student) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean terminate = false;
        int choice;

        while (!terminate){
            System.out.println("Scegli l'azione da compiere tra: \n" + actionList);
            choice = scanner.nextInt();

            switch (choice) {
                case 1:     //Scegli i nuovi corsi da seguire
                    controller.chooseCourses(student);
                    break;

                case 2:     //Visualizza libretto
                    controller.displayStudentTranscript(student);
                    break;

                case 3:     //Visualizza attività
                    controller.displayActivities(student);
                    break;

                case 4:     //Aggiungi attività
                    System.out.println("Inserire l'attività': ");
                    String activityInfo = scanner.nextLine();

                    System.out.println("Inserire la data: ");
                    String activityDate = scanner.nextLine();

                    System.out.println("Inserire la data: ");
                    int activityStart = scanner.nextInt();

                    System.out.println("Inserire la data: ");
                    int activityEnd = scanner.nextInt();

                    Activity activity = controller.addActivity(student, activityInfo, activityDate, activityStart, activityEnd);

                    System.out.println("E' stata inserità l'attività " + activity.getName());
                    break;

                case 5:     //Visualizza voto esame di un corso
                    System.out.println("Inserire il nome del corso: ");
                    String courseName = scanner.nextLine();

                    int grade = controller.getGrade(student, courseName);

                    if(grade != -1){
                        System.out.println("Voto all'esame di " + courseName + ": " + grade);
                    } else {
                        System.out.println("Non hai ancora il voto per l'esame di " + courseName);
                    }

                    break;

                case 6:     //Visualizza la media
                    float studentAvg = controller.getStudentAvg(student);

                    System.out.println("La tua media è" + studentAvg);
                    break;

                case 7:     //Visualizza grafico voti esame
                    controller.displayExamsGraph(student);
                    break;

                case 8:     //Inserisci study-time
                    controller.insertStudyTime(student);
                    break;

                case 9:     //Visualizza study-time
                    controller.getStudyInfo(student);
                    break;
            }
        }
    }

    private final String actionList = """
            1: Scegli i nuovi corsi da seguire
            2: Visualizza libretto
            3: Visualizza attività
            4: Aggiungi attività
            5: Visualizza voto esame di un corso
            6: Visualizza la media
            7: Visualizza grafico voti esame
            8: Inserisci study-time
            9: Visualizza study-time
            """;

    private final Controller controller = new Controller();
}
