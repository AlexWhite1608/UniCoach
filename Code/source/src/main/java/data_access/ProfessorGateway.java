package data_access;

import domain_model.*;
import manager_implementation.Activity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;



public class ProfessorGateway implements Gateway{

    public ProfessorGateway(Professor professor) throws SQLException {
        connection = DBConnection.connect("../database/unicoachdb.db");
    }

    //FIXME: ora che abbiamo messo l'attributo corso nel professore ci serve ancora setCourseID?
    public void setCourseId(Professor professor) throws SQLException {
        connection = DBConnection.connect("../database/unicoachdb.db");

        String docSql = "SELECT Codice FROM Corso WHERE Docente = ?";
        PreparedStatement docStatement = connection.prepareStatement(docSql);
        docStatement.setString(1, professor.getId());
        ResultSet docRs = docStatement.executeQuery();

        if (docRs.next()) {
            this.courseID = docRs.getString("Codice");
        } else {
            throw new SQLException("Nessun corso trovato per il professore " + professor.getId());
        }

        docRs.close();
        docStatement.close();
    }

    public void addProfessor(Professor professor) throws SQLException {
        String sql = "INSERT OR IGNORE INTO Docente (Matricola, Nome, Cognome, Email) VALUES (?, ?, ?, ?)";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, professor.getId());
        statement.setString(2, professor.getName());
        statement.setString(3, professor.getSurname());
        statement.setString(4, professor.getEmail());

        statement.executeUpdate();
        statement.close();
    }

    public int getGrade(Student student) throws SQLException {

        int grade;

        //SQL per ottenere il voto dello studente fornito
        String gradeSQL = """
                SELECT Voto
                FROM Esame
                WHERE Corso = ? AND Studente = ?""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement gradeStatement = connection.prepareStatement(gradeSQL);
        gradeStatement.setString(1, this.courseID);
        gradeStatement.setString(2, student.getId());
        ResultSet gradeRs = gradeStatement.executeQuery();

        if (gradeRs.next()) {
            grade = gradeRs.getInt("Voto");
        } else {
            grade = -1; //valore di default per indicare che non c'è un voto per questo studente
        }

        gradeRs.close();
        gradeStatement.close();

        return grade;
    }

    public void setGrade(Student student, Exam exam, int grade) throws SQLException{
        String sql = "INSERT INTO Esame (Codice, Nome, Studente, Data, CFU, Voto, Corso, TipoEsame) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, exam.getId());
        statement.setString(2, exam.getName());
        statement.setString(3, student.getId());
        statement.setString(4, exam.getDate());
        statement.setInt(5, exam.getCFU());
        statement.setInt(6, grade);
        statement.setString(7, exam.getCourse().getId());
        statement.setString(8, exam.getExamType());

        statement.executeUpdate();
        statement.close();
    }

    // Ritorna la media su tutti gli esami dati dallo studente
    public float getAverage(Student student) throws SQLException{
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

    //FIXME: ma ci serve davvero sto metodo?
    public void getAverage(List<Student> students) throws SQLException {

    }

    // Ritorna la media del corso (tutti i voti degli studenti iscritti / numeri iscritti)
    public float getAverage() throws SQLException {
        String average = """
            SELECT CAST(SUM(CAST(Esame.Voto AS FLOAT) * CAST(Corso.CFU AS FLOAT)) / SUM(CAST(Corso.CFU AS FLOAT)) AS FLOAT)
            FROM Esame
            JOIN Corso ON Esame.Corso = Corso.Codice
            WHERE Corso.Codice = ?;""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement averageStatement = connection.prepareStatement(average);
        averageStatement.setString(1, this.courseID);

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
    public void addActivity(Activity activity, User user) throws SQLException{
        String sql = """
                INSERT INTO CalendarioDocenti(Attività, Data, OraInizio, OraFine, Matricola) VALUES (?, ?, ?, ?, ?)""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1, activity.getName());
        statement.setString(2, activity.getDate());
        statement.setInt(3, activity.getStartTime());
        statement.setInt(4, activity.getEndTime());
        statement.setString(5, user.getId());

        statement.executeUpdate();
        statement.close();

        String select = """
                SELECT Id FROM CalendarioDocenti ORDER BY Id DESC LIMIT 1""";

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
        String deleteLesson = "DELETE FROM CalendarioDocenti WHERE Data = ? AND Attività = ? AND Matricola = ? ";
        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement deleteStatement = connection.prepareStatement(deleteLesson);
        deleteStatement.setString(1, activity.getDate());
        deleteStatement.setString(2, activity.getName());
        deleteStatement.setString(3, user.getId());
        deleteStatement.executeUpdate();
    }

    public void removeLesson(int giorno, int mese, int anno, Professor professor) throws SQLException {

        //Ricerco la lezione nel database
        String findCourseSql = "SELECT * FROM CalendarioDocenti WHERE Data = ? AND Matricola = ?";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement selectStatement = connection.prepareStatement(findCourseSql);
        String data = String.valueOf(giorno) + "/" + String.valueOf(mese) + "/" + String.valueOf(anno);

        selectStatement.setString(1, data);
        selectStatement.setString(2, professor.getId());
        ResultSet resultSet = selectStatement.executeQuery();

        if(resultSet.next()){
            //Elimina la lezione dal calendario del professore
            Activity removeLesson = new Activity();
            removeLesson.setName("Lezione " + professor.getCourse().getName());
            removeLesson.setDate(data);

            removeActivity(removeLesson, professor);

            //Elimina anche la lezione visualizzata nel calendario dello studente
            for(Observer student : professor.getObservers()){
                ((Student)student).getStudentGateway().removeActivity(removeLesson, (Student)student);
            }

        } else throw new SQLException("La lezione inserita non è presente");

        resultSet.close();
        selectStatement.close();
    }

    public void displayActivities(Professor professor) throws SQLException {
        String sql = """
                SELECT Attività, Data, OraInizio, OraFine
                FROM CalendarioStudenti
                WHERE Matricola = ?
                ORDER BY CAST(Data as date) ASC""";

        connection = DBConnection.connect("../database/unicoachdb.db");

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, professor.getId());

        ResultSet resultSet = statement.executeQuery();

        if (resultSet.next()) {
            String activity = resultSet.getString(1);
            String date = resultSet.getString(2);
            int startTime = resultSet.getInt(3);
            int endTime = resultSet.getInt(4);

            System.out.println("\nProssime attività di " + professor.getName() + " " + professor.getSurname());
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

    private Connection connection = null;
    private String courseID;

}
