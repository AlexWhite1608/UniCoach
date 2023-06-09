package view;

import controller.Controller;
import domain_model.Student;
import manager.Activity;

import java.sql.SQLException;
import java.util.Scanner;

public class StudentMenu {

    public void displayMenu(Student student) throws SQLException {
        Scanner scanner = new Scanner(System.in);
        boolean terminate = false;
        int choice;
        controller = new Controller(student);

        while (!terminate){
            System.out.println("Scegli l'azione da compiere tra: \n" + actionList);
            choice = scanner.nextInt();

            switch (choice) {
                case 1:     //Scegli i nuovi corsi da seguire
                    controller.chooseCourses();
                    break;

                case 2:     //Visualizza libretto
                    controller.displayUniTranscript();
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

                    Activity activity = controller.addActivity(activityInfo, activityDate, activityStart, activityEnd);

                    System.out.println("E' stata inserità l'attività " + activity.getName());
                    break;

                case 5:     //Visualizza voto esame di un corso
                    System.out.println("Inserire il nome del corso: ");
                    String courseName = scanner.nextLine();

                    int grade = controller.getGrade(courseName);

                    if(grade != -1){
                        System.out.println("Voto all'esame di " + courseName + ": " + grade);
                    } else {
                        System.out.println("Non hai ancora il voto per l'esame di " + courseName);
                    }

                    break;

                case 6:     //Visualizza la media
                    float studentAvg = controller.getStudentAvg();

                    System.out.println("La tua media è" + studentAvg);
                    break;

                case 7:     //Visualizza grafico voti esame
                    controller.displayExamsGraph();
                    break;

                case 8:     //Inserisci study-time
                    controller.insertStudyTime();
                    break;

                case 9:     //Visualizza study-time
                    controller.getStudyInfo();
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

    private Controller controller = null;
}
