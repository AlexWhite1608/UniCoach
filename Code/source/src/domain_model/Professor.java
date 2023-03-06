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

    public int getGrade(Student student){
        return 0;
    }

    public void setExamDate(){

    }

    private List<Observer> observers;
}
