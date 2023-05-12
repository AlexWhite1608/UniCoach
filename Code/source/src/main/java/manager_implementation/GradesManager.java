package manager_implementation;

import domain_model.Course;
import domain_model.Exam;
import domain_model.Student;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class GradesManager {

    //Grafica gli esami svolti da quello studente con la media
    public static void displayExamsGraph(Student student) throws SQLException {

        JFreeChart chart = ChartFactory.createBarChart(
                "Esami di " + student.getName() + " " + student.getSurname(),
                "Esame",           // Etichetta asse x
                "Voti",                               // Etichetta asse y
                getStudentDataset(student)            // Dataset
        );

        // Crea una finestra per mostrare il grafico
        JFrame frame = new JFrame("Grafico voti " + student.getName() + " " + student.getSurname());
        frame.setPreferredSize(new Dimension(500, 400));
        frame.pack();
        frame.setVisible(true);

        //Modifica estetica grafico
        chart.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.decode("#1d3557"));

        // Specifica il percorso e il nome del file di output
        String outputPath = "../graph/"+ "grafico" + student.getId() + ".png";    //FIXME: cambia percorso!!

        // Specifica le dimensioni per l'immagine del grafico
        int width = 800;
        int height = 600;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        // Crea un oggetto ChartPanel per il grafico
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setSize(width, height);
        chartPanel.paint(graphics);

        // Salva l'immagine del grafico
        try {
            File outputFile = new File(outputPath);
            ImageIO.write(image, "png", outputFile);
            System.out.println("Grafico salvato come: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Errore durante il salvataggio dell'immagine del grafico: " + e.getMessage());
        }

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
            studentDataset.addValue(grade, "Studente " + student.getId(), examName);
        }

        return studentDataset;

    }
}
