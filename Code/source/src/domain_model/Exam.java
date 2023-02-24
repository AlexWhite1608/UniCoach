package domain_model;

enum ExamType {
    PROJECT,
    ORAL_EXAMINATION,
    WRITTEN_TEST,
    WRITTEN_AND_ORAL_TEST
}

public class Exam{

    public Exam(String n) {
        this.name = n;
    }

    public Exam(String name, int grade, boolean praise, String date, boolean isPassed, ExamType examType) {
        this.name = name;
        this.grade = grade;
        this.praise = praise;
        this.date = date;
        this.isPassed = isPassed;
        this.examType = examType;
    }

    public String getName() {
        return name;
    }

    public int getGrade() {
        return grade;
    }

    public boolean isPraise() {
        return praise;
    }

    public String getDate() {
        return date;
    }

    public boolean isPassed() {
        return isPassed;
    }

    private String name;
    private int grade;
    private boolean praise;
    private String date;
    private boolean isPassed;
    private ExamType examType;
    private Course course;  //FIXME: siamo sicuri che sia Exam che deve avere un riferimento a Course?

}
