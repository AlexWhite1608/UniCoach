package domain_model;

public interface Observer {
    void update();

    void attach(Course course);

    void detach(Course course);

}
