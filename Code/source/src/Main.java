import data_access.DBConnection;
import domain_model.*;
import user_login.LoginManager;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        System.out.println("Hello world!");

        Student student = new Student(7033449, "Alessandro", "Bianco");
        Professor professor = new Professor(456321, "Enrico", "Vicario");

        student.displayAttributes();
        professor.displayAttributes();

        UniTranscript transcript = student.getUniTranscript();
        transcript.addExam(new Exam("Prova"));

        student.displayUniTranscript();

        // Test login manager
        LoginManager loginManager = new LoginManager();
        loginManager.addUser(student);
        loginManager.addUser(professor);

        loginManager.login(student);
        loginManager.login(professor);

        loginManager.getAllUsers();

        professor.getGrade(student);

        loginManager.logout(student);
        loginManager.logout(professor);

    }

}