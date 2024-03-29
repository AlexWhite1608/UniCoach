package data_access;

import domain_model.*;
import manager.Activity;

import javax.mail.MessagingException;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class Gateway {

    abstract void addActivity(Activity activity, User user) throws SQLException;

    abstract void removeActivity(Activity activity, User user) throws SQLException, MessagingException;

    static Connection connection = DBConnection.connect();

}
