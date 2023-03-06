package domain_model;

public interface Subject {
    void notifyObservers(String msg);

    void subscribe(Observer o);

    void unsubscribe(Observer o);
}
