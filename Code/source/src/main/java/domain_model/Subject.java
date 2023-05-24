package domain_model;

import manager.Activity;

import javax.mail.MessagingException;
import java.sql.SQLException;

public interface Subject {
    void notifyObservers(String msg, String subject, Activity activity) throws MessagingException, SQLException;

    void notifyObservers(Activity activity) throws SQLException;

    void subscribe(Observer o);

    void unsubscribe(Observer o);
}
