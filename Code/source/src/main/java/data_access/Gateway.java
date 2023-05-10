package data_access;

import domain_model.*;
import manager_implementation.Activity;

import java.sql.SQLException;

public interface Gateway {

    void addActivity(Activity activity, User user) throws SQLException;

    void removeActivity(Activity activity, User user) throws SQLException;

}
