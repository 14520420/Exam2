package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;

public class SubjectDao extends Dao {

    private String baseSql = "SELECT * FROM subject WHERE school_cd=?";

    private List<Subject> postFilter(ResultSet rSet, School school) throws Exception {
        List<Subject> list = new ArrayList<>();
        while (rSet.next()) {
            Subject subject = new Subject();
            subject.setCd(rSet.getString("cd"));
            subject.setName(rSet.getString("name"));
            subject.setSchool(school);
            list.add(subject);
        }
        return list;
    }

    // 科目1件取得（複合主キー：cd + school_cd）
    public Subject get(String cd, School school) throws Exception {
        Subject subject = null;
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT * FROM subject WHERE cd = ? AND school_cd = ?");
            statement.setString(1, cd);
            statement.setString(2, school.getCd());
            ResultSet rSet = statement.executeQuery();

            if (rSet.next()) {
                subject = new Subject();
                subject.setCd(rSet.getString("cd"));
                subject.setName(rSet.getString("name"));
                subject.setSchool(school);
            }
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }
        return subject;
    }

    // 学校ごとの全科目取得
    public List<Subject> filter(School school) throws Exception {
        List<Subject> list = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            statement = connection.prepareStatement(baseSql + " ORDER BY cd ASC");
            statement.setString(1, school.getCd());
            rSet = statement.executeQuery();
            list = postFilter(rSet, school);
        } finally {
            if (rSet != null) try { rSet.close(); } catch (SQLException e) { throw e; }
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }

        return list;
    }

    // 科目保存（INSERT or UPDATE）
    public boolean save(Subject subject) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            Subject old = get(subject.getCd(), subject.getSchool());

            if (old == null) {
                statement = connection.prepareStatement(
                    "INSERT INTO subject (cd, name, school_cd) VALUES (?, ?, ?)"
                );
                statement.setString(1, subject.getCd());
                statement.setString(2, subject.getName());
                statement.setString(3, subject.getSchool().getCd());
            } else {
                statement = connection.prepareStatement(
                    "UPDATE subject SET name = ? WHERE cd = ? AND school_cd = ?"
                );
                statement.setString(1, subject.getName());
                statement.setString(2, subject.getCd());
                statement.setString(3, subject.getSchool().getCd());
            }

            count = statement.executeUpdate();

        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }

        return count > 0;
    }

    // 削除
    public boolean delete(Subject subject) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            statement = connection.prepareStatement(
                "DELETE FROM subject WHERE cd = ? AND school_cd = ?"
            );
            statement.setString(1, subject.getCd());
            statement.setString(2, subject.getSchool().getCd());

            count = statement.executeUpdate();

        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) { throw e; }
            if (connection != null) try { connection.close(); } catch (SQLException e) { throw e; }
        }

        return count > 0;
    }
}