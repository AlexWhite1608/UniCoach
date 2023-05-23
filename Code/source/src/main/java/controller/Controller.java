package controller;

import domain_model.Professor;
import domain_model.User;
import manager_implementation.StudyTimeManager;
import user_login.LoginManager;

import javax.mail.MessagingException;
import javax.naming.directory.InvalidAttributesException;
import javax.swing.*;
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

    final LoginManager loginManager = new LoginManager();
}
