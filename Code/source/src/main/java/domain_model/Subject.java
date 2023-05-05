package domain_model;

import javax.mail.MessagingException;

public interface Subject {
    void notifyObservers(String msg, String subject) throws MessagingException;

    void subscribe(Observer o);

    void unsubscribe(Observer o);
}
