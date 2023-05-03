package domain_model;

import data_access.StudentGateway;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Observer {
    public Student(String id, String name, String surname) throws SQLException {
        super(id, name, surname);

        uniTranscript = new UniTranscript();
        subjects = new ArrayList<>();

        studentGateway.addStudent(this);
    }

    //TODO: quando lo studente si registra deve scegliere i corsi da seguire --> utilizzare una vista?

    //TODO: lo studente sceglierà (tramite notifiche observer) la data dell'esame?

    public void displayUniTranscript(){
        uniTranscript.displayExams();
    }

    public UniTranscript getUniTranscript() {
        return uniTranscript;
    }

    public int getGrade(Course course, Student student) throws SQLException {
        return studentGateway.getGrade(course, student);
    }

    @Override
    public void update() {

    }

    @Override
    public void attach(Course course) {
        Professor professor = course.getProfessor();

        subjects.add(professor);
        professor.subscribe(this);
    }

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
