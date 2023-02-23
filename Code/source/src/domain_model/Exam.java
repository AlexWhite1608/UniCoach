package domain_model;

public class Exam{

    public Exam(String n) {
        this.name = n;
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

}
