package domain_model;

import data_access.StudentGateway;
import manager.Activity;

import java.sql.SQLException;

public interface Observer {
    void update(Activity activity, StudentGateway studentGateway) throws SQLException;

    void attach(Course course);

    void detach(Course course);

}
