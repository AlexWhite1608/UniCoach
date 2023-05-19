package domain_model;

import data_access.DBConnection;
import manager_implementation.CoursesManager;
import utility.RandomStringGenerator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Course {
    public Course(String name, int CFU, Professor professor, ExamType examType) throws SQLException {
        this.id = RandomStringGenerator.generateRandomString(8);
        this.name = name;
        this.CFU = CFU;
        this.professor = professor;
        this.examType = examType;
        this.addCourse();

        professor.setCourse(this);

        CoursesManager.addCourse(this);
    }

    //FIXME: Vedere se serve questo costruttore
    public Course(String id, String name, int CFU, Professor professor, ExamType examType) throws SQLException {
        this.id = id;
        this.name = name;
        this.CFU = CFU;
        this.professor = professor;
        this.examType = examType;
        this.addCourse();

        professor.setCourse(this);

        CoursesManager.addCourse(this);
    }

    //Aggiunge il corso al db quando viene istanziato
    private void addCourse() throws SQLException {
        Connection connection = DBConnection.connect("../database/unicoachdb.db");

        //Aggiungo il corso relativo all'esame
        String courseSql = "INSERT OR IGNORE INTO Corso (Codice, Nome, CFU, Docente, TipoEsame) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement courseStatement = connection.prepareStatement(courseSql);
        courseStatement.setString(1, this.getId());
        courseStatement.setString(2, this.getName());
        courseStatement.setInt(3, this.getCFU());
        courseStatement.setString(4, this.getProfessor().getId());
        courseStatement.setString(5, this.getExamType().getDisplayName());

        courseStatement.executeUpdate();
        courseStatement.close();

        connection = DBConnection.disconnect();
    }

    public ExamType getExamType() {
        return examType;
    }

    public String getName() {
        return name;
    }

    public int getCFU() {
        return CFU;
    }

    public Professor getProfessor() {
        return professor;
    }

    public String getId() {
        return id;
    }

    private final String id;
    private String name;
    private int CFU;
    private Professor professor;
    private ExamType examType;
}
