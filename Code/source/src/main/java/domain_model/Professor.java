package domain_model;

import data_access.ProfessorGateway;
import manager.Activity;
import manager.ChartManager;
import manager.StudyTimeManager;
import utility.MailNotifier;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

import javax.mail.*;
import javax.naming.directory.InvalidAttributesException;

public class Professor extends User implements Subject{

    public Professor(String id, String name, String surname, String email) throws SQLException {
        super(id, name, surname, email);

        observers = new ArrayList<>();

        professorGateway = new ProfessorGateway(this);
        professorGateway.addProfessor(this);

        course = null;
    }

    public Professor(String id, String name, String surname) throws SQLException {
        super(id, name, surname);

        observers = new ArrayList<>();

        professorGateway = new ProfessorGateway(this);
        professorGateway.addProfessor(this);

        course = null;
    }

    @Override
    public void displayActivities() throws SQLException {
        professorGateway.displayActivities(this);
    }

    //Aggiunge l'esame al libretto dello studente
    public void setGrade(Student student, int grade, String data, boolean sendEmail) throws SQLException, MessagingException {
        Exam exam = student.getUniTranscript().findExam(this.course);
        if (grade >= 1 && grade <= 30) {

            if (exam != null) {
                professorGateway.setGrade(exam, grade, data);

                if (sendEmail) {
                    //Manda email allo studente
                    String msg = "Gentile " + student.getName() + " " + student.getSurname() + " ti comunichiamo che l'esito della prova" +
                            " di esame relativa all'attività didattica " + exam.getName() + " da te sostenuta il " + data + " è " + grade + "/30";

                    MailNotifier.sendEmail(student, msg, "Pubblicazione voto appello " + exam.getName(), this);
                }
            } else {
                System.out.println("Lo studente selezionato non è iscritto al corso");
            }
        } else {
            System.out.println("Il voto inserito non è valido");
        }
    }

    //Imposta il corso al professore
    public void setCourse(Course course) {
        this.course = course;
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

    //Aggiunge la nuova data dell'esame
    public Activity addExamDate(String date, int startTime, int endTime) throws MessagingException, SQLException {
        String subject = "Nuova data esame professor " + course.getName() + " " + this.getSurname();
        String msg = "Di seguito la data del prossimo esame: \n" + date;

        Activity addExamActivity = new Activity();
        addExamActivity.setName("Data esame " + course.getName());
        addExamActivity.setDate(date);
        addExamActivity.setStartTime(startTime);
        addExamActivity.setEndTime(endTime);

        this.addActivity(addExamActivity);

        //Notifica gli studenti del nuovo esame
        notifyObservers(msg, subject, new Activity(addExamActivity));

        return addExamActivity;
    }

    //Aggiunge le eventuali note/homework della lezione
    public Activity addLectureNotes(String date, String msg) throws MessagingException, SQLException {
        String subject = "Resoconto lezione di "+ course.getName()+ " del " + date;

        Activity addLectureNotesActivity = new Activity();
        addLectureNotesActivity.setName("Resoconto lezione di " + course.getName());
        addLectureNotesActivity.setDate(date);
        addLectureNotesActivity.setStartTime(0);    // FIXME: negli orari ci possiamo mettere gli orari della lezione svolta
        addLectureNotesActivity.setEndTime(0);

        this.addActivity(addLectureNotesActivity);

        notifyObservers(msg, subject, addLectureNotesActivity);

        return addLectureNotesActivity;
    }

    //Grafica gli esami svolti da quello studente con la media
    public void displayExamsGraph(Student student) throws SQLException {
        ChartManager.displayExamsGraph(student);
    }

    //Grafica i voti di tutti gli studenti iscritti a quel corso (specifico per il professore)
    public void displayExamsGraph(Course course) throws SQLException {
        ChartManager.displayExamsGraph(course);
    }

    //Grafica la media di ogni corso presente
    public void displayExamsGraph() throws SQLException {
        ChartManager.displayExamsGraph();
    }

    //Visualizza le informazioni di studio degli studenti iscritti al corso; numero di ore spese confrontato con il voto ottenuto
    public void getCourseStudyInfo(Course course) throws SQLException {
        StudyTimeManager.getCourseStudyInfo(course);
    }

    //Vede le informazioni sullo studio dello studente fornito
    public void getStudentStudyInfo(Student student) throws SQLException {
        StudyTimeManager.getStudentStudyInfo(student);
    }

    //Programma le lezioni del professore per il trimestre
    public List<Activity> scheduleLessons(int giorno, int mese, int anno, int oraInizio, int oraFine) throws SQLException, InvalidAttributesException, MessagingException {

        if(giorno < 1 || giorno > 31 || mese < 1 || mese > 12)
            throw new InvalidAttributesException("Data inserita errata");

        String name = "Lezione " + course.getName();
        int tmpGiorno = giorno;
        int tmpMese = mese;
        int tmpAnno = anno;

        List<Activity> activityList= new ArrayList<>();

        while(tmpMese - mese <= 3 || tmpMese - mese + 12 <= 3){
            String date = tmpGiorno + "/" + tmpMese + "/" + tmpAnno;

            Activity activity = new Activity(name, date, oraInizio, oraFine);
            activityList.add(activity);

            this.addActivity(activity);
            this.notifyObservers(new Activity(activity));

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

        for(Observer obs : observers){
            MailNotifier.sendEmail(obs, "Inserite le date delle lezioni per la sessione", "Date lezioni di" + course.getName(), this);
        }

        return activityList;
    }

    //Rimuove la lezione del giorno fornito
    public void removeLesson(int giorno, int mese, int anno) throws SQLException {    //TODO: deve rimuovere la lezione anche dal calendario dello studente!!
        professorGateway.removeLesson(giorno, mese, anno, this);
    }

    //Aggiunge un'attività
    private void addActivity(Activity activity) throws SQLException {
        professorGateway.addActivity(activity, this);
    }

    @Override
    public void notifyObservers(String msg, String subject, Activity activity) throws MessagingException, SQLException {

        for(Observer observer : observers){
            MailNotifier.sendEmail(observer, msg, subject, this);
            observer.update(activity);
        }
    }

    @Override
    public void notifyObservers(Activity activity) throws SQLException {
        for(Observer observer : observers) {
            observer.update(activity);
        }
    }

    @Override
    public void subscribe(Observer o) {
        observers.add(o);
    }

    @Override
    public void unsubscribe(Observer o) {
        observers.remove(o);
    }

    public Student getStudentFromId(String id){
        for(Observer observer : observers){
            if(Objects.equals(id, ((Student) observer).getId()))
                return (Student) observer;
        }
        return null;
    }

    public ProfessorGateway getProfessorGateway() {
        return professorGateway;
    }

    public List<Observer> getObservers() {
        return observers;
    }

    public Course getCourse() {
        return course;
    }

    private List<Observer> observers;
    private ProfessorGateway professorGateway;
    private Course course;
}
