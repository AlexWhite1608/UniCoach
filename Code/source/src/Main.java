import data_access.DBConnection;
import domain_model.*;
import user_login.LoginManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws SQLException {
        LoginManager loginManager = new LoginManager();

        //Aggiunta studenti
        Student student1 = new Student("7033449", "Alessandro", "Bianco");
        Student student2 = new Student("6758129", "Riccardo", "Becciolini");
        Student student3 = new Student("1200843", "Paolo", "Verdi");
        loginManager.addUser(student1);
        loginManager.addUser(student2);
        loginManager.addUser(student3);

        //Aggiunta professori
        Professor professor1 = new Professor("2261890", "Enrico", "Vicario");
        Professor professor2 = new Professor("2267491", "Marco", "Bertini");
        Professor professor3 = new Professor("7732209", "Carlo", "Colombo");
        loginManager.addUser(professor1);
        loginManager.addUser(professor2);
        loginManager.addUser(professor3);

        //login a caso di uno studente per vedere se funziona
        loginManager.login(student1);

        //Aggiunta di un esame per gli studenti
        Course swe = new Course("Swe", 6, professor1, ExamType.PROJECT);
        Exam examSwe = new Exam(swe, "14/03/2023", 30);
        student1.addExam(examSwe);
        student2.addExam(examSwe);
        student3.addExam(examSwe);

        //Professore ottiene voto di uno studente
        professor1.getGrade(student1);

        //Professore ottiene voto di tutti gli studenti
        ArrayList<Student> studentsList =new ArrayList<Student>();
        studentsList.add(student1);
        studentsList.add(student2);
        studentsList.add(student3);
        professor1.getGrade(studentsList);
    }

//        Student student = new Student("7033449", "Alessandro", "Bianco");
//        Professor professor = new Professor("382156", "Enrico", "Vicario");
//
//        student.displayAttributes();
//        professor.displayAttributes();
//
//        UniTranscript transcript = student.getUniTranscript();
//        transcript.addExam(new Exam("Prova"));
//
//        student.displayUniTranscript();
//
//        // Test login manager
//        LoginManager loginManager = new LoginManager();
//
//        loginManager.addUser(student);
//        loginManager.addUser(professor);
//
//        loginManager.login(student);
//        loginManager.login(professor);
//
//        loginManager.getAllUsers();
//
//        professor.getGrade(student);
//
//        loginManager.logout(student);
//        loginManager.logout(professor);
//
//    }



}