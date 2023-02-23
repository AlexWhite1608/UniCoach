package domain_model;

public interface Subject {
    void notifyObservers();

    void subscribe(Observer o);

    void unsubscribe(Observer o);
}
