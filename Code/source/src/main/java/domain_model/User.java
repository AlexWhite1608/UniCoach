package domain_model;

import utility.RandomStringGenerator;

import java.sql.SQLException;

public abstract class User {

    public User(String id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public User(String ID, String n, String s) {
        this.id = ID;
        this.name = n;
        this.surname = s;

        this.email = n + "." + s + "@gmail.com";
    }



    public abstract void displayActivities() throws SQLException;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String id;
    private String name = "";
    private String surname = "";
    private String email = "";
    private String password = "";

}
