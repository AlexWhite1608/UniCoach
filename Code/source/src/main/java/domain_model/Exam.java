package domain_model;

public class Exam{

    public Exam(Course course) {
        this.id = RandomStringGenerator.generateRandomString(8);
        this.course = course;
        this.name = course.getName();
        this.CFU = course.getCFU();
        this.examType = course.getExamType();
        this.grade = -1;
        this.date = null;
    }

    public Exam(Course course, String date) {
        this.id = RandomStringGenerator.generateRandomString(8);
        this.course = course;
        this.name = course.getName();
        this.CFU = course.getCFU();
        this.examType = course.getExamType();
        this.grade = -1;
        this.date = date;
    }


    public String getId() {
        return id;
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

    public int getCFU() {
        return CFU;
    }

    public String getExamType() {
        return examType.getDisplayName();
    }

    public Course getCourse() {
        return course;
    }

    private String id;
    private String name;
    private int grade = 0;
    private boolean praise = false;
    private String date = "";
    private boolean isPassed = false;
    private int CFU;
    private ExamType examType;
    private Course course;

}
