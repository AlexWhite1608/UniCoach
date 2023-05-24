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

        studentGateway.addStudent(this);
    }

    public Student(String id, String name, String surname) throws SQLException {
        super(id, name, surname);

        uniTranscript = new UniTranscript();
        subjects = new ArrayList<>();

        studentGateway.addStudent(this);
    }

    //Lo studente sceglie i corsi ai quali iscriversi
    public void chooseCourses() {
        CoursesManager.chooseCourses(this);
    }

    public void displayUniTranscript() throws SQLException{
        studentGateway.displayTranscript(this);
    }

    @Override
    public void displayActivities() throws SQLException {
        studentGateway.displayActivities(this);
    }

    public UniTranscript getUniTranscript() {
        return uniTranscript;
    }

    public List<Exam> getExams(Student student) throws SQLException {
        return studentGateway.getExams(this);

    }

    public int getGrade(Course course) throws SQLException {
        return studentGateway.getGrade(course, this);
    }

    public float getAverage() throws SQLException {
        return studentGateway.getAverage(this);
    }

    //Mostra il grafico dei voti ottenuti dallo studente
    public void displayExamsGraph() throws SQLException {
        ChartManager.displayExamsGraph(this);
    }

    //Mostra le ore di studio per ciascun esame dato
    public void getStudyInfo() throws SQLException {
        StudyTimeManager.getStudentStudyInfo(this);
    }

    public StudentGateway getStudentGateway() {
        return studentGateway;
    }

    public Activity addActivity(String name, String date, int startTime, int endTime) throws SQLException{
        Activity activity = new Activity(name, date, startTime, endTime);
        studentGateway.addActivity(activity, this);
        return activity;
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
