package manager;

import domain_model.Course;
import domain_model.Student;
import domain_model.Exam;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class CoursesManager {

    public static void addCourse(Course course) {
        courses.add(course);
    }

    //Permette allo studente di scegliere i corsi da seguire tra tutti i corsi possibili
    public static void chooseCourses(Student student) {
        try {
            System.out.println("Tutti i corsi disponibili sono i seguenti: ");
            student.getStudentGateway().displayCourses();
            Scanner scanner = new Scanner(System.in);
            System.out.println("Digita il codice del corso che vuoi seguire (premi 0 per uscire)");
            String input = scanner.nextLine();
            List<Course> selectedCourses = new ArrayList<>();

            while (!input.equals("0")) {
                System.out.println("Digita il codice del corso successivo o premi 0 per uscire");
                Course course = findCourseById(input);
                Exam exam = student.getUniTranscript().findExam(course);

                if (course != null) {
                    selectedCourses.add(course);
                } else {
                    System.out.println("Codice del corso non valido");
                }
                input = scanner.nextLine();
            }
            studentController.getStudentGateway().linkStudentToCourse(selectedCourses, studentController.getStudent());

        } catch (SQLException e) {
            System.err.println("Errore durante l'accesso al database: " + e.getMessage());
        }

    }

    //Restituisce l'oggetto corso a partire dal suo id
    private static Course findCourseById(String courseId) {
        for(Course course : courses){
            if(Objects.equals(course.getId(), courseId))
                return course;
        }
        return null;
    }

    public static Course findCourseByName(String courseName) {
        for(Course course : courses){
            if(Objects.equals(course.getName(), courseName))
                return course;
        }
        return null;
    }

    public static List<Course> getCourses() {
        return courses;
    }

    private static List<Course> courses = new ArrayList<>();
}