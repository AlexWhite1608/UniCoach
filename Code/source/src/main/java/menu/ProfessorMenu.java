package menu;

import domain_model.User;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class ProfessorMenu {

    public void displayMenu(){
        //TODO: azioni professore
        Scanner scanner = new Scanner(System.in);
        boolean terminate = false;
        int choice;

        while (!terminate){
            System.out.println("Scegli l'azione da compiere tra: \n" + actionList);
            choice = scanner.nextInt();

            switch (choice) {
                case 1:

                case 2:

                case 3:

                case 4:

                case 5:

                case 6:

                case 7:

                case 8:

                case 9:

                case 10:

                case 11:

                case 12:

                case 13:
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
}
