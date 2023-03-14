package domain_model;

import data_access.Gateway;
import data_access.ProfessorGateway;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Professor extends User implements Subject{
    public Professor(int id, String name, String surname) throws SQLException {
        super(id, name, surname);

        observers = new ArrayList<>();

        professorGateway.addProfessor(this);

    }

    public void setGrade(Student student){

    }

    public void getGrade(Student student) throws SQLException {
        professorGateway.getGrade(student, this);
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

    private List<Observer> observers;
    private ProfessorGateway professorGateway = new ProfessorGateway();
}
