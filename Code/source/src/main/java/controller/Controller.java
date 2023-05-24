package controller;

import domain_model.Course;
import domain_model.Professor;
import domain_model.Student;
import domain_model.User;
import manager_implementation.Activity;
import manager_implementation.CoursesManager;
import manager_implementation.StudyTimeManager;
import manager_implementation.LoginManager;

import javax.mail.MessagingException;
import javax.naming.directory.InvalidAttributesException;
import java.sql.SQLException;

public class Controller {

    public Controller(Professor professor) {
        this.user = professor;
    }

    public Controller(Student student) {
        this.user = student;
    }

    public void addUser(User user) throws SQLException {
        loginManager.addUser(user);
    }

    public boolean login(User user) throws SQLException {
        return loginManager.login(user);
    }

    public void logout(User user) {
        loginManager.logout(user);
    }

    public void displayActivities() throws SQLException {
        user.displayActivities();
    }

    public void addExamDate(String examDate, int start, int end) throws MessagingException, SQLException {
        ((Professor)user).addExamDate(examDate, start, end);
    }

    public void scheduleLessons(int day, int month, int year, int start, int end) throws SQLException, MessagingException, InvalidAttributesException {
        ((Professor) user).scheduleLessons(day, month, year, start, end);
    }

    public void addLectureNotes(String date, String msg) throws MessagingException, SQLException {
        ((Professor) user).addLectureNotes(date, msg);
    }

    public void setGrade(String studentId, String date, int grade) throws SQLException, MessagingException {
        ((Professor) user).setGrade(((Professor) user).getStudentFromId(studentId), grade, date, true);
    }

    public int getGradeFromProfessor(String studentId) throws SQLException {
        return ((Professor) user).getGrade(((Professor) user).getStudentFromId(studentId));
    }

    public float getAverageStudent(String studentId) throws SQLException {
        return ((Professor) user).getAverage(((Professor) user).getStudentFromId(studentId));
    }

    public float getAverageCourse() throws SQLException {
        return ((Professor) user).getAverage();
    }

    public void displayStudentExamsGraph(String studentId) throws SQLException {
        ((Professor) user).displayExamsGraph(((Professor) user).getStudentFromId(studentId));
    }

    public void displayCourseExamsGraph() throws SQLException {
        ((Professor) user).displayExamsGraph(((Professor) user).getCourse());
    }

    public void displayAllCoursesGraph() throws SQLException {
        ((Professor) user).displayExamsGraph();
    }

    public void displayStudentStudyTime(String studentId) throws SQLException {
        ((Professor) user).getStudentStudyInfo(((Professor) user).getStudentFromId(studentId));
    }

    public void displayCourseStudyInfo() throws SQLException {
        ((Professor) user).getCourseStudyInfo(((Professor) user).getCourse());
    }

    public void chooseCourses() {
        ((Student) user).chooseCourses();
    }

    public void displayStudentTranscript() throws SQLException {
        ((Student) user).displayUniTranscript();
    }

    public Activity addActivity(String name, String date, int startTime, int endTime) throws SQLException {
        return ((Student) user).addActivity(name, date, startTime, endTime);
    }

    public int getGradeFromStudent(String courseName) throws SQLException {
        Course course = CoursesManager.findCourseByName(courseName);

        if(course != null)
            return ((Student) user).getGrade(course);
        else
            return -1;
    }

    public float getStudentAvg() throws SQLException {
        return ((Student) user).getAverage();
    }

    public void displayExamsGraph() throws SQLException {
        ((Student) user).displayExamsGraph();
    }

    public void insertStudyTime() throws SQLException {
        StudyTimeManager.compileForm(((Student) user));
    }

    public void getStudyInfo() throws SQLException {
        ((Student) user).getStudyInfo();
    }

    private User user;
    private final LoginManager loginManager = new LoginManager();
}
