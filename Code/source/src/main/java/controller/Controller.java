package controller;

import data_access.ProfessorGateway;
import data_access.StudentGateway;
import domain_model.*;
import manager.*;
import utility.MailNotifier;

import javax.mail.MessagingException;
import javax.naming.directory.InvalidAttributesException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    public Controller(Professor professor) throws SQLException {
        this.user = professor;

        professorGateway = new ProfessorGateway((Professor) this.user);
        professorGateway.addProfessor((Professor) this.user);
    }

    public Controller(Student student) throws SQLException{
        this.user = student;

        studentGateway = new StudentGateway((Student) this.user);
        studentGateway.addStudent((Student) this.user);
    }

    public void addUser(User user) throws SQLException {
        loginManager.addUser(user);
    }

    public boolean login(User user) throws SQLException {
        return loginManager.login(user);
    }

    public void logout(User user) {
        loginManager.logout(user);
    }

    //Metodi di user
    public void displayActivities(User user) throws SQLException {
        if(user instanceof Professor){
            professorGateway.displayActivities((Professor) user);
        } else if(user instanceof Student){
            studentGateway.displayActivities((Student) user);
        }

    }

    //Metodi di professor
    public Activity addExamDate(String date, int startTime, int endTime) throws MessagingException, SQLException {
        String subject = "Nuova data esame professor " + ((Professor) user).getCourse().getName() + " " + ((Professor) user).getSurname();
        String msg = "Di seguito la data del prossimo esame: \n" + date;

        Activity addExamActivity = new Activity();
        addExamActivity.setName("Data esame " + ((Professor) user).getCourse().getName());
        addExamActivity.setDate(date);
        addExamActivity.setStartTime(startTime);
        addExamActivity.setEndTime(endTime);

        professorGateway.addActivity(addExamActivity, ((Professor) user));


        //Notifica gli studenti del nuovo esame
        ((Professor) user).notifyObservers(msg, subject, new Activity(addExamActivity));

        return addExamActivity;
    }

    //Programma le lezioni del professore per il trimestre
    public List<Activity> scheduleLessons(int giorno, int mese, int anno, int oraInizio, int oraFine) throws SQLException, InvalidAttributesException, MessagingException {

        if(giorno < 1 || giorno > 31 || mese < 1 || mese > 12)
            throw new InvalidAttributesException("Data inserita errata");

        String name = "Lezione " + ((Professor) user).getCourse().getName();
        int tmpGiorno = giorno;
        int tmpMese = mese;
        int tmpAnno = anno;

        List<Activity> activityList= new ArrayList<>();

        while(tmpMese - mese <= 3 || tmpMese - mese + 12 <= 3){
            String date = tmpGiorno + "/" + tmpMese + "/" + tmpAnno;

            Activity activity = new Activity(name, date, oraInizio, oraFine);
            activityList.add(activity);

            professorGateway.addActivity(activity, (Professor) user);
            ((Professor) user).notifyObservers(new Activity(activity));

            tmpGiorno += 7;

            if(tmpGiorno >30 && ((tmpMese == 4) || (tmpMese == 6) || (tmpMese == 9) || (tmpMese == 11))){
                tmpGiorno -=  30;
                tmpMese += 1;
            }

            if(tmpGiorno >31 && ((tmpMese == 1) || (tmpMese == 3) || (tmpMese == 5) || (tmpMese == 7) || (tmpMese == 8) || (tmpMese == 10) || (tmpMese ==12) )){
                tmpGiorno -=  31;
                tmpMese += 1;

                if(tmpMese > 12) {
                    tmpMese -= 12;
                    tmpAnno += 1;
                }
            }

            if(tmpGiorno >28 && (tmpMese == 2)){
                tmpGiorno -=  28;
                tmpMese += 1;
            }

        }

        ((Professor) user).notifyObservers( "Inserite le date delle lezioni per la sessione", "Date lezioni di" + ((Professor) user).getCourse().getName());

        return activityList;
    }

    //Rimuove la lezione del giorno fornito
    public void removeLesson(int giorno, int mese, int anno) throws SQLException, MessagingException {
        professorGateway.removeLesson(giorno, mese, anno, (Professor) user, this);
    }

    //Aggiunge le eventuali note/homework della lezione
    public Activity addLectureNotes(String date, String msg) throws MessagingException, SQLException {
        String subject = "Resoconto lezione di "+ ((Professor) user).getCourse().getName()+ " del " + date;

        Activity addLectureNotesActivity = new Activity();
        addLectureNotesActivity.setName("Resoconto lezione di " + ((Professor) user).getCourse().getName());
        addLectureNotesActivity.setDate(date);
        addLectureNotesActivity.setStartTime(0);
        addLectureNotesActivity.setEndTime(0);

        professorGateway.addActivity(addLectureNotesActivity, ((Professor) user));

        ((Professor) user).notifyObservers(msg, subject, addLectureNotesActivity);

        return addLectureNotesActivity;
    }

    //Aggiunge l'esame al libretto dello studente
    public void setGrade(Student student, int grade, String data, boolean sendEmail) throws SQLException, MessagingException {
        Exam exam = student.getUniTranscript().findExam(((Professor) user).getCourse());
        if (grade >= 1 && grade <= 30) {

            if (exam != null) {
                professorGateway.setGrade(exam, grade, data);

                if (sendEmail) {
                    //Manda email allo studente
                    String msg = "Gentile " + student.getName() + " " + student.getSurname() + " ti comunichiamo che l'esito della prova" +
                            " di esame relativa all'attività didattica " + exam.getName() + " da te sostenuta il " + data + " è " + grade + "/30";

                    MailNotifier.sendEmail(student, msg, "Pubblicazione voto appello " + exam.getName(), (Professor) user);
                }
            } else {
                System.out.println("Lo studente selezionato non è iscritto al corso");
            }
        } else {
            System.out.println("Il voto inserito non è valido");
        }
    }

    //Ottiene il voto dello studente fornito
    public int getGrade(Student student) throws SQLException {
        return professorGateway.getGrade(student);
    }

    //Ritorna la media dello studente fornito
    public float getAverage(Student student) throws SQLException {
        return professorGateway.getAverage(student);
    }

    //Ritorna la media del corso
    public float getAverage() throws SQLException {
        return professorGateway.getAverage();
    }

    //Grafica gli esami svolti da quello studente con la media
    public void displayExamsGraph(Student student) throws SQLException {
        ChartManager.displayExamsGraph(this);
    }

    //Grafica i voti di tutti gli studenti iscritti a quel corso (specifico per il professore)
    public void displayExamsGraph(Course course) throws SQLException {
        ChartManager.displayExamsGraph(course);
    }

    //Grafica la media di ogni corso presente
    public void displayAllExamsGraph() throws SQLException {
        ChartManager.displayExamsGraph();
    }

    //Vede le informazioni sullo studio dello studente fornito
    public void getStudentStudyInfo(Student student) throws SQLException {
        StudyTimeManager.getStudentStudyInfo(student);
    }

    //Visualizza le informazioni di studio degli studenti iscritti al corso; numero di ore spese confrontato con il voto ottenuto
    public void getCourseStudyInfo(Course course) throws SQLException {
        StudyTimeManager.getCourseStudyInfo(course);
    }

    //-----------------metodi dello studente-----------------

    //Lo studente sceglie i corsi ai quali iscriversi
    public void chooseCourses() {
        CoursesManager.chooseCourses(this);
    }

    public void displayUniTranscript() throws SQLException {
        studentGateway.displayTranscript((Student) user);
    }

    public Activity addActivity(String name, String date, int startTime, int endTime) throws SQLException {
        Activity activity = new Activity(name, date, startTime, endTime);
        studentGateway.addActivity(activity, (Student) user);
        return activity;
    }

    public List<Exam> getExams(Student student) throws SQLException {
        return studentGateway.getExams((Student) user);
    }

    public int getGrade(String courseName) throws SQLException {
        Course course = CoursesManager.findCourseByName(courseName);

        if(course != null)
            return studentGateway.getGrade(course, (Student) user);
        else
            return -1;
    }

    public float getStudentAvg() throws SQLException {
        return studentGateway.getAverage((Student) user);
    }

    //Mostra il grafico dei voti ottenuti dallo studente
    public void displayExamsGraph() throws SQLException {
        ChartManager.displayExamsGraph(this);
    }

    public void insertStudyTime() throws SQLException {
        StudyTimeManager.compileForm(((Student) user));
    }

    //Mostra le ore dedicate allo studio di uno studente
    public void getStudyInfo() throws SQLException {
        StudyTimeManager.getStudentStudyInfo((Student) user);
    }

    public ProfessorGateway getProfessorGateway() {
        return professorGateway;
    }

    public StudentGateway getStudentGateway() {
        return studentGateway;
    }

    public Student getStudent() {
        return (Student) user;
    }

    private User user;
    private ProfessorGateway professorGateway;
    private StudentGateway studentGateway;
    private final LoginManager loginManager = new LoginManager();

}
