package domain_model;

import manager_implementation.Activity;

import javax.mail.MessagingException;
import java.sql.SQLException;

public interface Subject {
    void notifyObservers(String msg, String subject, Activity activity) throws MessagingException, SQLException;

    void subscribe(Observer o);

    void unsubscribe(Observer o);
}
