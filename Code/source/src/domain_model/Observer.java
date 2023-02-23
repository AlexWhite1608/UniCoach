package domain_model;

public interface Observer {
    void update();

    void attach();

    void detach();

}
