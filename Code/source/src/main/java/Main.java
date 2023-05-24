import view.FacadeMenu;

public class Main {
    public static void main(String[] args) {
        FacadeMenu facadeMenu = new FacadeMenu();
        try {
            facadeMenu.displayMenu();
        }
        catch (Exception e) {
            System.out.println("Errore visualizzazione menù");
        }
    }
}