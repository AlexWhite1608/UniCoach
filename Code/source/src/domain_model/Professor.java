package domain_model;

public class Professor extends User implements Subject{
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
