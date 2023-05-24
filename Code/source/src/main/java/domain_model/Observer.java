package domain_model;

import manager.Activity;

import java.sql.SQLException;

public interface Observer {
    void update(Activity activity) throws SQLException;

    void attach(Course course);

    void detach(Course course);

}
