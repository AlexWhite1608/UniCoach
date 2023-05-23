package menu;

import controller.Controller;
import domain_model.Professor;
import domain_model.Student;
import domain_model.User;
import utility.RandomStringGenerator;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class FacadeMenu {

    public void displayMenu() throws SQLException {
        Scanner scanner = new Scanner(System.in);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        boolean terminate = false;
        int choice;
        User user = null;

        while (!terminate) {
            System.out.println("Benvenuto su UniCoach!\n");
            System.out.println("Digita:\n1 Se non hai ancora un account \n2 Esegui il login \n3 Esci");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:

                    System.out.println("\nInserisci il tuo nome: ");
                    String name = scanner.nextLine();

                    System.out.println("\nInserisci il tuo cognome: ");
                    String surname = scanner.nextLine();

                    System.out.println("\nInserisci la tua mail: ");
                    String mail = scanner.nextLine();

                    System.out.println("Digita 1 se sei un docente, digita 2 se sei uno studente");
                    int userType = scanner.nextInt();

                    if(userType == 1){
                        user = new Professor(RandomStringGenerator.generateRandomString(6), name, surname, mail);
                        controller.addUser(user);

                        professorMenu.displayMenu();
                    } else if (userType == 2) {
                        user = new Student(RandomStringGenerator.generateRandomString(6), name, surname, mail);
                        controller.addUser(user);

                        studentMenu.displayMenu();
                    } else {
                        System.out.println("Errore");
                        break;
                    }

                    break;

                case 2:

                    if (user != null) {
                        if(controller.login(user))
                            System.out.println("Login effettuato!");
                        else {
                            System.out.println("Credenziali errate");
                            break;
                        }

                        if(user instanceof Professor)
                            professorMenu.displayMenu();
                        else if(user instanceof Student)
                            studentMenu.displayMenu();

                    } else {
                        System.out.println("Utente non registrato!");
                        break;
                    }

                    break;

                case 3:
                    terminate = true;
                    break;
            }
        }
    }

    private StudentMenu studentMenu;
    private ProfessorMenu professorMenu;
    private final Controller controller = new Controller();
}
