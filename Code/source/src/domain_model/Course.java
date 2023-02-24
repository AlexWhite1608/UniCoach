package domain_model;

public class Course {
    public Course(String name, int CFU) {
        this.name = name;
        this.CFU = CFU;
    }

    public String getName() {
        return name;
    }

    public int getCFU() {
        return CFU;
    }

    private String name;
    private int CFU;
}
