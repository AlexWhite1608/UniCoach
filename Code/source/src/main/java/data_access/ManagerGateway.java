package data_access;

import domain_model.*;
import manager.Activity;
import manager.StudyType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManagerGateway extends Gateway {

    public static void insertInfoInDb(Map<Exam, List<Map<StudyType, Integer>>> studyInfo) throws SQLException {
        String sql = "INSERT OR IGNORE INTO OreStudio (Codice, TipoStudio, Ore)" +
                "VALUES (?, ?, ?)" +
                "ON CONFLICT (Codice, TipoStudio) DO UPDATE SET Ore = Ore + excluded.Ore";

        connection = DBConnection.connect("../database/unicoachdb.db");
        PreparedStatement statement = connection.prepareStatement(sql);

        for(Map.Entry<Exam, List<Map<StudyType, Integer>>> entry : studyInfo.entrySet()) {
            Exam exam = entry.getKey();
            for (Map<StudyType, Integer> it : entry.getValue()){
                for(Map.Entry<StudyType, Integer> value : it.entrySet()){
                    statement.setString(1, exam.getId());
                    statement.setString(2, value.getKey().getDisplayName());
                    statement.setInt(3, value.getValue());
                    statement.executeUpdate();
                }
            }
        }

        statement.close();
        DBConnection.disconnect();
    }

    public static Map<StudyType, Integer> getCourseStudyInfo(Course course) throws SQLException {
        String sql = """
                SELECT TipoStudio, SUM(Ore) AS TotaleOre
                FROM OreStudio
                WHERE Codice = (SELECT Codice
                                FROM Esame
                                WHERE Corso = ?)
                GROUP BY TipoStudio""";

        connection = DBConnection.connect("../database/unicoachdb.db");
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, course.getId());
        ResultSet resultSet = statement.executeQuery();
        Map<StudyType, Integer> studyHoursByType = new HashMap<>();

        while (resultSet.next()) {
            String studyTypeString = resultSet.getString("TipoStudio");
            int totalHours = resultSet.getInt("TotaleOre");

            StudyType studyType = StudyType.getStudyTypeFromString(studyTypeString);
            studyHoursByType.put(studyType, totalHours);
        }

        resultSet.close();
        statement.close();
        DBConnection.disconnect();

        return studyHoursByType;
    }

    public static Map<Exam, List<Map<StudyType, Integer>>> getStudentStudyInfo(Student student) throws SQLException {
        String sql = """
            SELECT TipoStudio, Ore
            FROM OreStudio
            WHERE Codice = ?""";

        Map<Exam, List<Map<StudyType, Integer>>> info = new HashMap<>();
        connection = DBConnection.connect("../database/unicoachdb.db");
        PreparedStatement statement = connection.prepareStatement(sql);
        UniTranscript uniTranscript = student.getUniTranscript();

        for (Exam exam : uniTranscript.getExamList()) {
            List<Map<StudyType, Integer>> studyTypeList = new ArrayList<>();
            Map<StudyType, Integer> studyTypeMap = new HashMap<>();

            statement.setString(1, exam.getId());
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                StudyType studyType = StudyType.getStudyTypeFromString(resultSet.getString("TipoStudio"));
                int hours = resultSet.getInt("Ore");
                studyTypeMap.put(studyType, hours);
            }

            studyTypeList.add(studyTypeMap);
            info.put(exam, studyTypeList);
        }

        return info;
    }

    @Override
    public void addActivity(Activity activity, User user) throws SQLException {}

    @Override
    public void removeActivity(Activity activity, User user) throws SQLException {}
}
