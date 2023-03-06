package domain_model;

import data_access.Gateway;
import data_access.ProfessorGateway;

import java.util.List;
import java.util.ArrayList;

public class Professor extends User implements Subject, ProfessorGateway {
    public Professor(int id, String name, String surname) {
        super(id, name, surname);

        observers = new ArrayList<>();
    }

    public void setGrade(Student student){

    }

    @Override
    public void notifyObservers(String msg) {

    }

    @Override
    public void subscribe(Observer o) {
        observers.add(o);
    }

    @Override
    public void unsubscribe(Observer o) {
        observers.remove(o);
    }

    @Override
    public void getGrade(Student student){}

    @Override
    public void getGrade(List<Student> students){}

    @Override
    public void getGrade(Course course){}

    @Override
    public void setExamDate(Exam exam, String date){}

    @Override
    public void getAverage(Student student){}

    @Override
    public void getAverage(List<Student> students){}

    @Override
    public void getAverage(Course course){}

    public void setExamDate(){

    }

    private List<Observer> observers;
}
