package menu;

public class FacadeMenu {

    public void displayStudentMenu(){
        studentMenu.displayMenu();
    }

    public void displayProfessorMenu(){
        professorMenu.displayMenu();
    }

    public void displayLoginMenu(){
        loginMenu.displayMenu();
    }

    private StudentMenu studentMenu;
    private ProfessorMenu professorMenu;
    private LoginMenu loginMenu;
}
