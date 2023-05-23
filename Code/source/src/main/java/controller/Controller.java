package controller;

import domain_model.Course;
import domain_model.Professor;
import domain_model.Student;
import domain_model.User;
import manager_implementation.Activity;
import manager_implementation.CoursesManager;
import manager_implementation.StudyTimeManager;
import user_login.LoginManager;

import javax.mail.MessagingException;
import javax.naming.directory.InvalidAttributesException;
import java.sql.SQLException;

public class Controller {

    public void addUser(User user) throws SQLException {
        loginManager.addUser(user);
    }

    public boolean login(User user) throws SQLException {
        return loginManager.login(user);
    }

    public void logout(User user) {
        loginManager.logout(user);
    }

    public void displayActivities(Professor professor) throws SQLException {
        professor.displayActivities();
    }

    public void addExamDate(Professor professor, String examDate, int start, int end) throws MessagingException, SQLException {
        professor.addExamDate(examDate, start, end);
    }

    public void scheduleLessons(Professor professor, int day, int month, int year, int start, int end) throws SQLException, MessagingException, InvalidAttributesException {
        professor.scheduleLessons(day, month, year, start, end);
    }

    public void addLectureNotes(Professor professor, String date, String msg) throws MessagingException, SQLException {
        professor.addLectureNotes(date, msg);
    }

    public void setGrade(Professor professor, String studentId, String date, int grade) throws SQLException, MessagingException {
        professor.setGrade(professor.getStudentFromId(studentId), grade, date, true);
    }

    public int getGrade(Professor professor, String studentId) throws SQLException {
        return professor.getGrade(professor.getStudentFromId(studentId));
    }

    public float getAverageStudent(Professor professor, String studentId) throws SQLException {
        return professor.getAverage(professor.getStudentFromId(studentId));
    }

    public float getAverageCourse(Professor professor) throws SQLException {
        return professor.getAverage();
    }

    public void displayStudentExamsGraph(Professor professor, String studentId) throws SQLException {
        professor.displayExamsGraph(professor.getStudentFromId(studentId));
    }

    public void displayCourseExamsGraph(Professor professor) throws SQLException {
        professor.displayExamsGraph(professor.getCourse());
    }

    public void displayAllCoursesGraph(Professor professor) throws SQLException {
        professor.displayExamsGraph();
    }

    public void displayStudentStudyTime(Professor professor, String studentId) throws SQLException {
        professor.getStudentStudyInfo(professor.getStudentFromId(studentId));
    }

    public void displayCourseStudyInfo(Professor professor) throws SQLException {
        professor.getCourseStudyInfo(professor.getCourse());
    }

    public void chooseCourses(Student student) {
        student.chooseCourses();
    }

    public void displayStudentTranscript(Student student) throws SQLException {
        student.displayUniTranscript();
    }

    public void displayActivities(Student student) throws SQLException {
        student.displayActivities();
    }

    public Activity addActivity(Student student, String name, String date, int startTime, int endTime) throws SQLException {
        return student.addActivity(name, date, startTime, endTime);
    }

    public int getGrade(Student student, String courseName) throws SQLException {
        Course course = CoursesManager.findCourseByName(courseName);

        if(course != null)
            return student.getGrade(course);
        else
            return -1;
    }

    public float getStudentAvg(Student student) throws SQLException {
        return student.getAverage();
    }

    public void displayExamsGraph(Student student) throws SQLException {
        student.displayExamsGraph();
    }

    public void insertStudyTime(Student student) throws SQLException {
        StudyTimeManager.compileForm(student);
    }

    public void getStudyInfo(Student student) throws SQLException {
        student.getStudyInfo();
    }

    final LoginManager loginManager = new LoginManager();
}
