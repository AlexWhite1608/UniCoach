package domain_model;

import data_access.ProfessorGateway;
import manager_implementation.Activity;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import javax.mail.*;
import javax.mail.internet.*;
import javax.naming.directory.InvalidAttributesException;
import java.util.Properties;

public class Professor extends User implements Subject{

    public Professor(String id, String name, String surname, String email) throws SQLException {
        super(id, name, surname, email);

        observers = new ArrayList<>();

        professorGateway = new ProfessorGateway(this);
        professorGateway.addProfessor(this);
    }

    public Professor(String id, String name, String surname) throws SQLException {
        super(id, name, surname);

        observers = new ArrayList<>();

        professorGateway = new ProfessorGateway(this);
        professorGateway.addProfessor(this);

    }

    @Override
    public void displayActivities() throws SQLException {
        professorGateway.displayActivities(this);
    }

    public void setGrade(Student student, Exam exam, int grade) throws SQLException {
        professorGateway.setGrade(student, exam, grade);

        //Aggiunge l'esame al libretto dello studente
        student.getUniTranscript().addExam(exam);
    }

    public int getGrade(Student student) throws SQLException {
        return professorGateway.getGrade(student);
    }

    public float getAverage(Student student) throws SQLException {
        return professorGateway.getAverage(student);
    }

    public float getAverage() throws SQLException {
        return professorGateway.getAverage();
    }

    //FIXME: questo metodo andrebbe spostato dalla classe professor?
    private void sendEmail(Observer dest, String msg, String subject) throws MessagingException {
        Properties prop = new Properties();

        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", true);
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        String professorEmail = this.getEmail();
        String professorPassword = this.getPassword();

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(professorEmail, professorPassword);
            }
        });

        Message message = prepareMessage(session, professorEmail, ((Student) dest).getEmail(), msg, subject);   //FIXME: rendere modificabile l'oggetto della mail

        Transport.send(message);
        System.out.println("Email inviata correttamente");
    }

    //FIXME: questo metodo andrebbe spostato dalla classe professor?
    private Message prepareMessage(Session s, String email, String dest, String msg, String subject){
        try {
            Message message = new MimeMessage(s);
            message.setFrom(new InternetAddress(email));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(dest));
            message.setSubject(subject);
            message.setText(msg);
            return message;
        }catch(MessagingException e) {
            e.printStackTrace();
            return null;
        }
    }

    //FIXME: ora questo metodo ritorna l'oggetto Activity ma non è troppo corretto
    public Activity addExamDate(String date, int startTime, int endTime) throws MessagingException, SQLException {

        String subject = "Nuova data esame professor " + this.getName() + " " + this.getSurname();
        String msg = "Di seguito la data del prossimo esame: \n" + date;

        Activity addExamActivity = new Activity();
        addExamActivity.setName("Data esame");   //FIXME: trovare il modo di passare il nome del corso del professore!
        addExamActivity.setDate(date);
        addExamActivity.setStartTime(startTime);
        addExamActivity.setEndTime(endTime);

        this.addActivity(addExamActivity);

        //Notifica gli studenti del nuovo esame
        notifyObservers(msg, subject, addExamActivity);

        return addExamActivity;
    }

    public Activity addLectureNotes(String date, String msg) throws MessagingException, SQLException {
        String subject = "Resoconto lezione " + date;

        Activity addLectureNotesActivity = new Activity();
        addLectureNotesActivity.setName("Resoconto lezione");   //FIXME: trovare il modo di passare il nome del corso del professore!
        addLectureNotesActivity.setDate(date);
        addLectureNotesActivity.setStartTime(0);    // FIXME: negli orari ci possiamo mettere gli orari della lezione svolta
        addLectureNotesActivity.setEndTime(0);

        this.addActivity(addLectureNotesActivity);

        notifyObservers(msg, subject, addLectureNotesActivity);

        return addLectureNotesActivity;
    }

    public Activity scheduleLesson(int giorno, int mese, int anno, int oraInizio, int oraFine) throws SQLException, InvalidAttributesException, MessagingException {

        if(giorno < 1 || giorno > 31 || mese < 1 || mese > 12)
            throw new InvalidAttributesException("Data inserita errata");

        String name = "Lezione";    //FIXME: anche qui trovare il modo di passare il corso
        int tmpGiorno = giorno;
        int tmpMese = mese;
        int tmpAnno = anno;

        Activity activity = null;

        while(tmpMese - mese <= 3 || tmpMese - mese + 12 <= 3){
            String date = tmpGiorno + "/" + tmpMese + "/" + tmpAnno;

            activity = new Activity(name, date, oraInizio, oraFine);

            this.addActivity(activity);
            this.notifyObservers(activity);

            tmpGiorno += 7;

            if(tmpGiorno >=30 && ((tmpMese == 4) || (tmpMese == 6) || (tmpMese == 9) || (tmpMese == 11))){
                tmpGiorno -=  30;
                tmpMese += 1;
            }

            if(tmpGiorno >=31 && ((tmpMese == 1) || (tmpMese == 3) || (tmpMese == 5) || (tmpMese == 7) || (tmpMese == 8) || (tmpMese == 10) || (tmpMese ==12) )){
                tmpGiorno -=  31;
                tmpMese += 1;

                if(tmpMese > 12) {
                    tmpMese -= 12;
                    tmpAnno += 1;
                }
            }

            if(tmpGiorno >=28 && (tmpMese == 2)){
                tmpGiorno -=  28;
                tmpMese += 1;
            }

        }

        for(Observer obs : observers){
            this.sendEmail(obs, "Inserite le date delle lezioni per la sessione", "Date lezioni professor" + this.getName() + " " + this.getSurname());
        }

        return activity;
    }

    private void addActivity(Activity activity) throws SQLException {
        professorGateway.addActivity(activity, this);
    }

    @Override
    public void notifyObservers(String msg, String subject, Activity activity) throws MessagingException, SQLException {

        for(Observer observer : observers){
            sendEmail(observer, msg, subject);
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

    public ProfessorGateway getProfessorGateway() {
        return professorGateway;
    }

    private List<Observer> observers;
    private ProfessorGateway professorGateway;
}