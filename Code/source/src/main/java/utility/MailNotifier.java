package utility;

import domain_model.Observer;
import domain_model.Professor;
import domain_model.Student;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailNotifier {
    public static void sendEmail(Observer dest, String msg, String subject, Professor professor) throws MessagingException {
        Properties prop = new Properties();

        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", true);
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        String professorEmail = professor.getEmail();
        String professorPassword = professor.getPassword();

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(professorEmail, professorPassword);
            }
        });

        Message message = prepareMessage(session, professorEmail, ((Student) dest).getEmail(), msg, subject);

        Transport.send(message);
        System.out.println("Email inviata correttamente");
    }

    public static Message prepareMessage(Session s, String email, String dest, String msg, String subject){
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
}
