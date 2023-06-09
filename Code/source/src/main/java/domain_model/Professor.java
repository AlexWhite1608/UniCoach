package domain_model;

import data_access.ProfessorGateway;
import manager.Activity;
import manager.ChartManager;
import manager.StudyTimeManager;
import utility.MailNotifier;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import javax.mail.*;
import javax.naming.directory.InvalidAttributesException;

public class Professor extends User implements Subject{

    public Professor(String id, String name, String surname, String email) throws SQLException {
        super(id, name, surname, email);

        observers = new ArrayList<>();
        course = null;
    }

    public Professor(String id, String name, String surname) throws SQLException {
        super(id, name, surname);

        observers = new ArrayList<>();
        course = null;
    }



    //Imposta il corso al professore
    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public void notifyObservers(String msg, String subject, Activity activity) throws MessagingException, SQLException {
        for(Observer observer : observers){
            MailNotifier.sendEmail(observer, msg, subject, this);
            observer.update(activity);
        }
    }

    @Override
    public void notifyObservers(Activity activity) throws SQLException {
        for(Observer observer : observers) {
            observer.update(activity);
        }
    }

    @Override
    public  void notifyObservers(String msg, String subject) throws MessagingException{
        for(Observer observer : observers){
            MailNotifier.sendEmail(observer, msg, subject, this);
        }
    }

    @Override
    public void subscribe(Observer o) {
        observers.add(o);
    }

    @Override
    public void unsubscribe(Observer o) {
        observers.remove(o);
    }

    public Student getStudentFromId(String id){
        for(Observer observer : observers){
            if(Objects.equals(id, ((Student) observer).getId()))
                return (Student) observer;
        }
        return null;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public Course getCourse() {
        return course;
    }

    private List<Observer> observers;
    private Course course;
}
