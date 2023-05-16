package manager_implementation;

import domain_model.Course;
import domain_model.Student;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

//TODO: la classe deve gestire i corsi dello studente, quindi farglieli scegliere
public class CoursesManager {

    public static void addCourse(Course course) {
        courses.add(course);
    }

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

                if (course != null) {
                    selectedCourses.add(course);
                } else {
                    System.out.println("Codice del corso non valido");
                }

                input = scanner.nextLine();
            }

            student.getStudentGateway().linkStudentToCourse(selectedCourses, student);

        } catch (SQLException e) {
            System.err.println("Errore durante l'accesso al database: " + e.getMessage());
        }
    }

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
