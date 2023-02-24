package domain_model;

public class User {

    public User(int id, String name, String surname, String email) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
    }

    public User(int ID, String n, String s) {
        this.id = ID;
        this.name = n;
        this.surname = s;

        this.email = n + "." + s + "@stud.unifi.it"; //FIXME: gestire omonimi
    }

    public void displayAttributes() {
        System.out.println("\nID: " + id);
        System.out.println("Name: " + name);
        System.out.println("Surname: " + surname);
        System.out.println("Email: " + email);
    }

    private int id = 0;
    private String name = "";
    private String surname = "";
    private String email = "";

}