package data_access;

import domain_model.Course;
import domain_model.Exam;
import domain_model.Student;

import java.util.List;

public interface ProfessorGateway {
    void getGrade(Student student);

    void getGrade(List<Student> students);

    void getGrade(Course course);

    void setExamDate(Exam exam, String date);

    void getAverage(Student student);

    void getAverage(List<Student> students);

    void getAverage(Course course);

}
