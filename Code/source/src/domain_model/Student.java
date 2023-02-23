package domain_model;

public class Student extends User implements Observer{
    public Student(int id, String name, String surname) {
        super(id, name, surname);
        uniTranscript = new UniTranscript();
    }

    public void displayUniTranscript(){
        uniTranscript.displayExams();
    }

    public UniTranscript getUniTranscript() {
        return uniTranscript;
    }

    @Override
    public void update() {

    }

    @Override
    public void attach() {

    }

    @Override
    public void detach() {

    }

    private UniTranscript uniTranscript;

}
