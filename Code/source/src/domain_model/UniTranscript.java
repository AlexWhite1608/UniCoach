package domain_model;

import java.util.ArrayList;
import java.util.List;

public class UniTranscript {

    public void addExam(Exam e){
        if(!examList.contains(e))
            examList.add(e);
        else
            System.out.println("L'esame è già presente nel libretto");
    }

    public void removeExam(Exam e){
        if(examList.contains(e))
            examList.remove(e);
        else
            System.out.println("L'esame non è presente nel libretto");
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

}
