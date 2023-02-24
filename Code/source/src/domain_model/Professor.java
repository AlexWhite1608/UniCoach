package domain_model;

import data_access.Gateway;
import data_access.ProfessorGateway;

public class Professor extends User implements Subject, ProfessorGateway {
    public Professor(int id, String name, String surname) {
        super(id, name, surname);
    }

    public void setGrade(Student student){

    }

    @Override
    public void notifyObservers() {

    }

    @Override
    public void subscribe(Observer o) {

    }

    @Override
    public void unsubscribe(Observer o) {

    }

    public int getGrade(Student student){
        return 0;
    }

    public void setExamDate(){

    }


}
