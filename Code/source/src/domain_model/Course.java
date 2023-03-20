package domain_model;

public class Course {
    public Course(String name, int CFU, Professor professor, ExamType examType) {
        this.id = RandomStringGenerator.generateRandomString(8);
        this.name = name;
        this.CFU = CFU;
        this.professor = professor;
        this.examType = examType;
    }

    public ExamType getExamType() {
        return examType;
    }

    public String getName() {
        return name;
    }

    public int getCFU() {
        return CFU;
    }

    public Professor getProfessor() {
        return professor;
    }

    public String getId() {
        return id;
    }

    private final String id;
    private String name;
    private int CFU;
    private Professor professor;
    private ExamType examType;
}
