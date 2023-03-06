package domain_model;

enum ExamType {
    PROJECT,
    ORAL_EXAMINATION,
    WRITTEN_TEST,
    WRITTEN_AND_ORAL_TEST
}

public class Course {
    public Course(String name, int CFU, Professor professor, ExamType examType) {
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

    private String name;
    private int CFU;
    private Professor professor;
    private ExamType examType;
}
