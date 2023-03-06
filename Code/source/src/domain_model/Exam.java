package domain_model;

public class Exam{

    public Exam(String n) {
        this.name = n;
    }

    public Exam(Course course) {
        this.course = course;
        this.name = course.getName();
        this.CFU = course.getCFU();
        this.examType = course.getExamType();
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
    private int grade = 0;
    private boolean praise = false;
    private String date = "";
    private boolean isPassed = false;
    private int CFU;
    private ExamType examType;
    private Course course;

}
