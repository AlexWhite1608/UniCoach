package data_access;

import domain_model.*;
import manager_implementation.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentGateway implements Gateway {

    public StudentGateway() {
        connection = DBConnection.connect("../database/unicoachdb.db");
    }

    public void addStudent(Student student) throws SQLException {
        String sql = "INSERT INTO Studente (Matricola, Nome, Cognome, Email) VALUES (?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getId());
        statement.setString(2, student.getName());
        statement.setString(3, student.getSurname());
        statement.setString(4, student.getEmail());

        statement.executeUpdate();
        statement.close();
    }

    // Ritorna il voto dello studente all'esame del corso fornito
    public int getGrade(Course course, Student student) throws SQLException{
        int grade;

        String gradeSQL = """
                SELECT Voto
                FROM Esame
                WHERE Corso = ? AND Studente = ?
                """;

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(gradeSQL);
        statement.setString(1, course.getId());
        statement.setString(2, student.getId());
        ResultSet gradeRs = statement.executeQuery();

        if (gradeRs.next()) {
            grade = gradeRs.getInt("Voto");
        } else {
            grade = -1; //valore di default per indicare che non c'è un voto per questo studente
        }

        gradeRs.close();
        statement.close();

        return grade;
    }


    //Ritorna la media dello studente
    public float getAverage(Student student) throws SQLException {
        String average = """
            SELECT CAST(SUM(CAST(Esame.Voto AS FLOAT) * CAST(Corso.CFU AS FLOAT)) / SUM(CAST(Corso.CFU AS FLOAT)) AS FLOAT)
            FROM Esame
            JOIN Corso ON Esame.Corso = Corso.Codice
            WHERE Esame.Studente = ?;""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement averageStatement = connection.prepareStatement(average);
        averageStatement.setString(1, student.getId());

        ResultSet averageRs = averageStatement.executeQuery();

        float finalAverage;

        if (averageRs.next()) {
            finalAverage = averageRs.getFloat(1);
        } else {
            throw new SQLException("Non sono presenti esami su cui eseguire la media!");
        }

        averageRs.close();
        averageStatement.close();

        return finalAverage;
    }

    @Override
    public void addActivity(Activity activity, User student) throws SQLException {
        String sql = """
                INSERT INTO CalendarioStudenti(Attività, Data, OraInizio, OraFine, Matricola) VALUES (?, ?, ?, ?, ?)""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, activity.getName());
        statement.setString(2, activity.getDate());
        statement.setInt(3, activity.getStartTime());
        statement.setInt(4, activity.getEndTime());
        statement.setString(5, student.getId());

        statement.executeUpdate();
        statement.close();

        String select = """
                SELECT Id FROM CalendarioDocenti ORDER BY Id DESC LIMIT 1""";   //FIXME: ho fatto che l'attività nella tabella CalendarioStudenti prende lo stesso id della stessa attività in CalendarioDocenti

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement selectStatement = connection.prepareStatement(select);
        ResultSet resultSet = selectStatement.executeQuery();

        if(resultSet.next()){
            String id = resultSet.getString(1);
            activity.setId(id);
        } else throw new SQLException("Non è riuscito a inserire l'evento nel database");

        resultSet.close();
        selectStatement.close();

    }

    @Override
    public void removeActivity(Activity activity, User user) throws SQLException {
        String deleteLesson = "DELETE FROM CalendarioStudenti WHERE Data = ? AND Attività = ? AND Matricola = ? ";
        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement deleteStatement = connection.prepareStatement(deleteLesson);
        deleteStatement.setString(1, activity.getDate());
        deleteStatement.setString(2, activity.getName());
        deleteStatement.setString(3, user.getId());
        deleteStatement.executeUpdate();
    }

    public void displayActivities(Student student) throws SQLException {
        String sql = """
                SELECT Attività, Data, OraInizio, OraFine
                FROM CalendarioStudenti
                WHERE Matricola = ?
                ORDER BY CAST(Data as date) ASC""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, student.getId());

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String activity = resultSet.getString(1);
            String date = resultSet.getDate(2).toString();
            int startTime = resultSet.getInt(3);
            int endTime = resultSet.getInt(4);

            System.out.println("\nProssime attività di " + student.getName() + " " + student.getSurname());
            System.out.println("Nome attività: " + activity);
            System.out.println("Data: " + date);
            System.out.println("Ora inizio: " + startTime);
            System.out.println("Ora fine: " + endTime);
            System.out.println("---------\n");
        } else {
            throw new SQLException("Non sono presenti attività da mostrare");
        }

        resultSet.close();
        statement.close();
    }

    public void displayCourse () throws SQLException{
        String sql = "SELECT * FROM Corso";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);



    }

    private Connection connection = null;
}
