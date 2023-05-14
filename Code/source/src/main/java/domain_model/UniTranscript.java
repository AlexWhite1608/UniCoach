package domain_model;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    public Exam getExam(String examID){
        for (Exam exam : examList){
            if (Objects.equals(exam.getId(), examID)){
                return exam;
            }
        }

        return null;
    }

    public void displayExams(){
        for(Exam i : examList){
            System.out.println(i.getName());
        }
    }

    public Exam findExam(String examId) {
        for(Exam e : examList){
            if(Objects.equals(e.getId(), examId))
                return e;
        }
        return null;
    }

    public Exam findExam(Course course) {
        for(Exam e : examList){
            if(Objects.equals(e.getCourse(), course))
                return e;
        }
        return null;
    }

    public int getSize() {
        return this.examList.size();
    }

    public List<Exam> getExamList() {
        return examList;
    }

    private List<Exam> examList = new ArrayList<Exam>();
    private final String id;

}
