package domain_model;

import data_access.StudentGateway;
import manager_implementation.Activity;
import manager_implementation.CoursesManager;
import manager_implementation.GradesManager;
import manager_implementation.StudyTimeManager;

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

    public void chooseCourses() {
        CoursesManager.chooseCourses(this);
    }

    public void displayUniTranscript() throws SQLException{
        studentGateway.displayTranscript(this);
    }

    public UniTranscript getUniTranscript() {
        return uniTranscript;
    }

    public List<Exam> getExams(Student student) throws SQLException {
        return studentGateway.getExams(this);

    }

    public int getGrade(Course course, Student student) throws SQLException {
        return studentGateway.getGrade(course, student);
    }

    public float getAverage(Student student) throws SQLException {
        return studentGateway.getAverage(student);
    }

    public void displayExamsGraph() throws SQLException {
        GradesManager.displayExamsGraph(this);
    }

    public StudentGateway getStudentGateway() {
        return studentGateway;
    }

    @Override
    public void update(Activity activity) throws SQLException {
        studentGateway.addActivity(activity, this);
    }

    public Activity addActivity(String name, String date, int startTime, int endTime) throws SQLException{
        Activity activity = new Activity(name, date, startTime, endTime);
        studentGateway.addActivity(activity, this);
        return activity;
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

    public void getStudyInfo() {
        StudyTimeManager.getStudentStudyInfo(this);
    }

    private UniTranscript uniTranscript;
    private List<Subject> subjects;
    private StudentGateway studentGateway = new StudentGateway();

}
