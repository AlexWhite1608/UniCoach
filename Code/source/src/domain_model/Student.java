package domain_model;

import data_access.StudentGateway;

import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Observer {
    public Student(int id, String name, String surname) {
        super(id, name, surname);

        uniTranscript = new UniTranscript();
        subjects = new ArrayList<>();
    }

    public void displayUniTranscript(){
        uniTranscript.displayExams();
    }

    public UniTranscript getUniTranscript() {
        return uniTranscript;
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
