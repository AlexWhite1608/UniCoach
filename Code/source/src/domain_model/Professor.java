package domain_model;

import data_access.ProfessorGateway;

import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class Professor extends User implements Subject{
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

    //TODO: il professore notifica gli studenti con gli homework e le date degli esami (tramite observer)

    private void sendEmail(Observer dest, String msg) {

        // Imposta le proprietà per la connessione SMTP
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // il server SMTP
        props.put("mail.smtp.socketFactory.port", "465"); // porta per la connessione SSL
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // tipo di socket
        props.put("mail.smtp.auth", "true"); // abilita l'autenticazione
        props.put("mail.smtp.port", "465"); // porta per la connessione SMTP

        // Autenticazione
        String senderEmail = this.getEmail();
        String senderPassword = this.getPassword();

        Authenticator auth = new Authenticator() {
            // sostituire email e password con le proprie credenziali
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        };

        // Crea la sessione per l'invio dell'email
        Session session = Session.getDefaultInstance(props, auth);

        try {
            // Crea il messaggio email
            Message message = new MimeMessage(session);

            // Imposta il mittente dell'email
            message.setFrom(new InternetAddress(senderEmail));

            // Imposta i destinatari dell'email
            try {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(((Student) dest).getEmail()));
            } catch (ClassCastException e) {
                System.out.println(e.getMessage());
            }

            // Imposta l'oggetto dell'email
            message.setSubject("Oggetto dell'email");

            // Imposta il testo dell'email
            message.setText(msg);    //TODO: inserire la roba da comunicare

            // Invia l'email
            Transport.send(message);

            System.out.println("Email inviata correttamente.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void notifyObservers(String msg) {

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
