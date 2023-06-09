package domain_model;

import data_access.StudentGateway;
import manager.Activity;
import manager.ChartManager;
import manager.CoursesManager;
import manager.StudyTimeManager;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Student extends User implements Observer {
    public Student(String id, String name, String surname, String email) throws SQLException {
        super(id, name, surname, email);

        uniTranscript = new UniTranscript();
        subjects = new ArrayList<>();

    }

    public Student(String id, String name, String surname) throws SQLException {
        super(id, name, surname);

        uniTranscript = new UniTranscript();
        subjects = new ArrayList<>();
    }

    @Override
    public void update(Activity activity, StudentGateway studentGateway) throws SQLException {
        studentGateway.addActivity(activity, this);
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

    public UniTranscript getUniTranscript() {
        return uniTranscript;
    }

    private UniTranscript uniTranscript;
    private List<Subject> subjects;
}
