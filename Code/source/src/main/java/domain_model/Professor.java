package domain_model;

import data_access.ProfessorGateway;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import javax.mail.*;
import javax.mail.internet.*;
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


    //FIXME: questo metodo andrebbe spostato dalla classe professor
    private void sendEmail(Observer dest, String msg) throws MessagingException {
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

        Message message = prepareMessage(session, professorEmail, ((Student) dest).getEmail(), msg, "OGGETTO EMAIL");   //FIXME: rendere modificabile l'oggetto della mail

        Transport.send(message);
        System.out.println("email sent successfully");
    }

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

    @Override
    public void notifyObservers(String msg) throws MessagingException {

        for(Observer observer : observers){
            sendEmail(observer, msg);
            observer.update();
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

    public void addExamDate() throws MessagingException {
        //TODO Modifica il calendario del professore

        String string = " ";
        notifyObservers(string);
    }


    public ProfessorGateway getProfessorGateway() {
        return professorGateway;
    }

    private List<Observer> observers;
    private ProfessorGateway professorGateway;
}
