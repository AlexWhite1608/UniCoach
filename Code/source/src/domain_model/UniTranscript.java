package domain_model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class UniTranscript {

    public UniTranscript() {
        this.id = RandomStringGenerator.generateRandomString(8);
    }

    public void addExam(Exam e){
        if(!examList.contains(e))
            examList.add(e);
        else
            System.out.println("L'esame è già presente nel libretto");
    }

    public String getId() {
        return id;
    }

    public Exam getExam(String examName){
        return null;
    }

    public void displayExams(){
        for(Exam i : examList){
            System.out.println(i.getName());
        }
    }

    public List<Exam> getExamList() {
        return examList;
    }

    private List<Exam> examList = new ArrayList<Exam>();
    private String id;

}
