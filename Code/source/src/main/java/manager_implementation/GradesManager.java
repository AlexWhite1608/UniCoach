package manager_implementation;

import data_access.DBConnection;
import domain_model.Course;
import domain_model.Exam;
import domain_model.Observer;
import domain_model.Student;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: ottimizza la classe mettendo come attributi chart, frame e così via
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
        ChartPanel  chartPanel = new ChartPanel(chart);
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
    public static void displayExamsGraph(Course course) throws SQLException {
        JFreeChart chart = ChartFactory.createBarChart(
                "Situazione corso " + course.getName(),
                "Studenti",           // Etichetta asse x
                "Voti",                               // Etichetta asse y
                getCourseDataset(course)            // Dataset
        );

        // Crea una finestra per mostrare il grafico
        JFrame frame = new JFrame("Grafico voti del corso" + course.getName());
        frame.setPreferredSize(new Dimension(500, 400));
        frame.pack();
        frame.setVisible(true);

        //Modifica estetica grafico
        chart.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.decode("#1d3557"));

        // Specifica il percorso e il nome del file di output
        String outputPath = "../graph/"+ "grafico_corso_" + course.getId() + ".png";    //FIXME: cambia percorso!!

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

    //Grafica la media di ogni corso presente
    public static void displayExamsGraph() throws SQLException {
        JFreeChart chart = ChartFactory.createBarChart(
                "Media esami svolti per ciascun corso",
                "Corso",           // Etichetta asse x
                "Media",                               // Etichetta asse y
                getAvgCoursesDataset()      // Dataset
        );

        // Crea una finestra per mostrare il grafico
        JFrame frame = new JFrame("Grafico media esami svolti per ciascun corso");
        frame.setPreferredSize(new Dimension(500, 400));
        frame.pack();
        frame.setVisible(true);

        //Modifica estetica grafico
        chart.getCategoryPlot().getRenderer().setSeriesPaint(0, Color.decode("#1d3557"));

        // Specifica il percorso e il nome del file di output
        String outputPath = "../graph/overview_corsi.png";    //FIXME: cambia percorso!!

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

    private static DefaultCategoryDataset getCourseDataset(Course course) {
        DefaultCategoryDataset studentDataset = new DefaultCategoryDataset();

        List<Observer> studentsLinkedToCourse = course.getProfessor().getObservers();

        for(Observer student : studentsLinkedToCourse){
            String studentId = ((Student) student).getId();
            int grade = ((Student) student).getUniTranscript().findExam(course).getGrade();
            studentDataset.addValue(grade, course.getName(), studentId);
        }

        return studentDataset;
    }

    private static DefaultCategoryDataset getAvgCoursesDataset() throws SQLException {

        DefaultCategoryDataset avgCourseDataset = new DefaultCategoryDataset();

        List<Course> courseList = CoursesManager.getCourses();

        //Mappa ciascun nome del corso con tutti i voti degli esami svolti per quel corso
        Map<String, List<Integer>> studentGrades = new HashMap<>();

        //Assegna a ciascun corso tutti i voti degli esami svolti dagli studenti iscritti
        getExamsGradesFromCourses(courseList, studentGrades);

        //Calcola la media dei voti per ciascun corso
        Map<String, Float> averageForEachCourse = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : studentGrades.entrySet()) {
            String courseName = entry.getKey();
            float average = (float) calculateAverage(entry.getValue());
            averageForEachCourse.put(courseName, average);
        }

        //Inserisce le informazioni nel dataset
        for(Map.Entry<String, Float> entry : averageForEachCourse.entrySet()){
            avgCourseDataset.addValue(entry.getValue(), "Corsi", entry.getKey());   //FIXME: fare che ogni corso ha un colore diverso
        }

        return avgCourseDataset;
    }

    //Ritorna la media degli esami svolti dagli studenti iscritti al corso fornito
    private static float getAvgCourse(Course course) throws SQLException {
        List<Course> courseList = CoursesManager.getCourses();

        //Mappa ciascun nome del corso con tutti i voti degli esami svolti per quel corso
        Map<String, List<Integer>> studentGrades = new HashMap<>();

        //Assegna a ciascun corso tutti i voti degli esami svolti dagli studenti iscritti
        getExamsGradesFromCourses(courseList, studentGrades);

        //Calcola la media dei voti per ciascun corso
        Map<String, Float> averageForEachCourse = new HashMap<>();
        for (Map.Entry<String, List<Integer>> entry : studentGrades.entrySet()) {
            String courseName = entry.getKey();
            float average = (float) calculateAverage(entry.getValue());
            averageForEachCourse.put(courseName, average);
        }

        return averageForEachCourse.get(course.getName());
    }

    //FIXME: sarebbe meglio non fare query in questa classe!
    private static void getExamsGradesFromCourses(List<Course> courseList, Map<String, List<Integer>> studentGrades) throws SQLException {

        String sql = "SELECT Voto FROM Esame WHERE Corso = ?";

        Connection connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);

        for(Course course : courseList){
            statement.setString(1, course.getId());
            ResultSet examsRs = statement.executeQuery();

            List<Integer> examsGrades = new ArrayList<>();

            while (examsRs.next()){
                examsGrades.add(examsRs.getInt("Voto"));
            }

            studentGrades.put(course.getName(), examsGrades);

            examsRs.close();
        }

        statement.close();
    }

    public static void visualizeStudyTypeData(DefaultPieDataset dataset, Course course) throws SQLException {

        JFreeChart chart = ChartFactory.createPieChart(
                "Grafico rapporto studio/media voti " + course.getName(),
                dataset,
                true,
                true,
                false
        );

        //TODO: bisogna inserire la media del corso in una label!
        float avgCourse = getAvgCourse(course);

        // Aggiungi una label aggiuntiva al grafico a torta
        TextTitle label = new TextTitle("Media del corso: " + avgCourse);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setPosition(RectangleEdge.BOTTOM);

        // Aggiunta della label al grafico
        chart.addSubtitle(label);

        // Crea una finestra per mostrare il grafico
        JFrame frame = new JFrame("Grafico rapporto studio/media voti");
        frame.setPreferredSize(new Dimension(500, 400));
        frame.pack();
        frame.setVisible(true);

        // Specifica il percorso e il nome del file di output
        String outputPath = "../graph/pie_chart_" + course.getId() + ".png";

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

    //Costruisce dataset per grafico a torta usato in StudyTimeManager.getCourseStudyInfo()
    public static DefaultPieDataset buildStudyTypeDataset(Map<StudyType, Integer> studyHoursByType) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (Map.Entry<StudyType, Integer> entry : studyHoursByType.entrySet()) {
            StudyType studyType = entry.getKey();
            int hours = entry.getValue();
            dataset.setValue(studyType.getDisplayName(), hours);
        }

        return dataset;
    }

    //TODO: spostalo in un package utility
    private static double calculateAverage(List<Integer> list) {
        int sum = 0;
        for (int value : list) {
            if(value != -1)
                sum += value;
        }
        return (float) sum / list.size();
    }


}
