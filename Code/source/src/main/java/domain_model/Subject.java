package domain_model;

import data_access.StudentGateway;
import manager.Activity;

import javax.mail.MessagingException;
import java.sql.SQLException;

public interface Subject {
    void notifyObservers(String msg, String subject, Activity activity) throws MessagingException, SQLException;

    void notifyObservers(Activity activity) throws SQLException;

    void notifyObservers(String msg, String subject, Activity activity, StudentGateway studentGateway) throws MessagingException, SQLException;

    void notifyObservers(Activity activity, StudentGateway studentGateway) throws SQLException;

    void notifyObservers(String msg, String subject) throws MessagingException;

    void subscribe(Observer o);

    void unsubscribe(Observer o);
}
