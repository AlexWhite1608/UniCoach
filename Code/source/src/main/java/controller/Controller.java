package controller;

import domain_model.User;
import manager_implementation.StudyTimeManager;
import user_login.LoginManager;

import java.sql.SQLException;

public class Controller {

    public void addUser(User user) throws SQLException {
        loginManager.addUser(user);
    }

    public boolean login(User user) throws SQLException {
        return loginManager.login(user);
    }

    public void logout(User user) {
        loginManager.logout(user);
    }



    final LoginManager loginManager = new LoginManager();
}
