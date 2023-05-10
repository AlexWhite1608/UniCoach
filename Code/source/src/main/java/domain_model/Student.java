package domain_model;

import data_access.StudentGateway;
import manager_implementation.Activity;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Student extends User implements Observer {
    public Student(String id, String name, String surname, String email) throws SQLException {
        super(id, name, surname, email);

        uniTranscript = new UniTranscript();
        subjects = new ArrayList<>();

        studentGateway.addStudent(this);
    }

    @Override
    public void displayActivities() throws SQLException {
        studentGateway.displayActivities(this);
    }

    public Student(String id, String name, String surname) throws SQLException {
        super(id, name, surname);

        uniTranscript = new UniTranscript();
        subjects = new ArrayList<>();

        studentGateway.addStudent(this);
    }

    public void chooseCourse() {
        try {
            System.out.println("Tutti i corsi disponibli sono i seguenti: ");
            studentGateway.displayCourse();

            System.out.println("Digita il codice del corso che vuoi seguire (premi 0 per uscire)");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

             do{
                studentGateway.linkStudentToCourse(input, this);
                System.out.println("Digita il codice del corso che vuoi seguire (premi 0 per uscire)");
                input = scanner.nextLine();
            } while (!input.equals("0"));

        } catch (SQLException e) {
            System.err.println("Errore durante l'accesso al database: " + e.getMessage());
        }
    }

    public void displayUniTranscript(){
        uniTranscript.displayExams();
    }

    public UniTranscript getUniTranscript() {
        return uniTranscript;
    }

    public int getGrade(Course course, Student student) throws SQLException {
        return studentGateway.getGrade(course, student);
    }

    public float getAverage(Student student) throws SQLException {
        return studentGateway.getAverage(student);
    }

    public StudentGateway getStudentGateway() {
        return studentGateway;
    }

    @Override
    public void update(Activity activity) throws SQLException {
        studentGateway.addActivity(activity, this);
    }

    @Override
    public void attach(Course course) {
        Professor professor = course.getProfessor();

        subjects.add(professor);
        professor.subscribe(this);
    }

    //TODO: andrebbe testata anche la cancellazione dello studente dal corso!
    @Override
    public void detach(Course course) {
        Professor professor = course.getProfessor();

        subjects.remove(professor);
        professor.unsubscribe(this);
    }

    private UniTranscript uniTranscript;
    private List<Subject> subjects;
    private StudentGateway studentGateway = new StudentGateway();

}
