import domain_model.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");

        Student student = new Student(7033449, "Alessandro", "Bianco");
        Professor professor = new Professor(456321, "Enrico", "Vicario");

        student.displayAttributes();
        professor.displayAttributes();

        UniTranscript transcript = student.getUniTranscript();
        transcript.addExam(new Exam("Prova"));

        student.displayUniTranscript();
    }

}