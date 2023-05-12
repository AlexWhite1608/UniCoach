package manager_implementation;

import domain_model.Course;
import domain_model.Exam;
import domain_model.Student;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import java.awt.*;
import java.sql.SQLException;
import java.util.List;


public class GradesManager {

    //Grafica gli esami svolti da quello studente con la media
    public static void displayExamsGraph(Student student) throws SQLException {

        JFreeChart chart = ChartFactory.createBarChart(
                "Esami di " + student.getName() + " " + student.getSurname(),
                "Nome esame",           // Etichetta asse x
                "Voti",                               // Etichetta asse y
                getStudentDataset(student)            // Dataset
        );

        // Crea una finestra per mostrare il grafico
        ChartFrame frame = new ChartFrame("Grafico voti " + student.getName() + " " + student.getSurname(), chart);
        frame.setPreferredSize(new Dimension(500, 400));
        frame.pack();
        frame.setVisible(true);
    }

    //Grafica i voti di tutti gli studenti iscritti a quel corso (specifico per il professore)
    public static void displayExamsGraph(Course course){

    }

    //Grafica la media di ogni corso presente
    public static void displayExamsGraph(){
    }

    private static DefaultCategoryDataset getStudentDataset(Student student) throws SQLException {
        DefaultCategoryDataset studentDataset = new DefaultCategoryDataset();
        List<Exam> examListStudent = student.getExams(student);

        for (Exam exam : examListStudent) {
            String examName = exam.getName();
            int grade = exam.getGrade();
            studentDataset.addValue(grade, "Esami " + student.getId(), examName);
        }

        return studentDataset;

    }
}
