package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.Teacher;

public class TeacherDao extends Dao {

    /**
     * 教員IDを指定して教員インスタンスを取得する
     */
    public Teacher get(String id) throws Exception {
        Teacher teacher = new Teacher();
        Connection connection = getConnection();
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement("SELECT * FROM teacher WHERE id = ?");
            statement.setString(1, id);
            ResultSet resultSet = statement.executeQuery();
            SchoolDao schoolDao = new SchoolDao();

            if (resultSet.next()) {
                teacher.setId(resultSet.getString("id"));
                teacher.setPassword(resultSet.getString("password"));
                teacher.setName(resultSet.getString("name"));

                // 管理者フラグの取得（DB構造に応じて列名を調整）
                try {
                    teacher.setAdmin(resultSet.getBoolean("is_admin"));
                } catch (SQLException e) {
                    try {
                        teacher.setAdmin(resultSet.getBoolean("admin"));
                    } catch (SQLException e2) {
                        teacher.setAdmin(false);
                        System.err.println("警告: teacher テーブルに admin または is_admin 列が見つかりません");
                    }
                }

                teacher.setSchool(schoolDao.get(resultSet.getString("school_cd")));
            } else {
                teacher = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException sqle) { throw sqle; }
            if (connection != null) try { connection.close(); } catch (SQLException sqle) { throw sqle; }
        }

        return teacher;
    }

    /**
     * 全教員の一覧を取得
     */
    public List<Teacher> getAllTeachers() throws Exception {
        List<Teacher> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT * FROM teacher ORDER BY id");
            resultSet = statement.executeQuery();
            SchoolDao schoolDao = new SchoolDao();

            while (resultSet.next()) {
                Teacher teacher = new Teacher();
                teacher.setId(resultSet.getString("id"));
                teacher.setPassword(resultSet.getString("password"));
                teacher.setName(resultSet.getString("name"));

                // 管理者フラグの取得
                try {
                    teacher.setAdmin(resultSet.getBoolean("is_admin"));
                } catch (SQLException e) {
                    try {
                        teacher.setAdmin(resultSet.getBoolean("admin"));
                    } catch (SQLException e2) {
                        teacher.setAdmin(false);
                    }
                }

                teacher.setSchool(schoolDao.get(resultSet.getString("school_cd")));
                list.add(teacher);
            }
        } catch (Exception e) {
            throw new Exception("教員情報の取得中にエラーが発生しました: " + e.getMessage());
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }

        return list;
    }

    /**
     * 教員情報を保存（新規登録または更新）
     */
    public boolean save(Teacher teacher) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        int count = 0;

        try {
            connection = getConnection();
            Teacher existing = get(teacher.getId());

            if (existing == null) {
                // 新規登録
                try {
                    // is_admin 列を使用する場合
                    statement = connection.prepareStatement(
                        "INSERT INTO teacher (id, password, name, school_cd, is_admin) VALUES (?, ?, ?, ?, ?)"
                    );
                } catch (SQLException e) {
                    // admin 列を使用する場合
                    statement = connection.prepareStatement(
                        "INSERT INTO teacher (id, password, name, school_cd, admin) VALUES (?, ?, ?, ?, ?)"
                    );
                }

                statement.setString(1, teacher.getId());
                statement.setString(2, teacher.getPassword());
                statement.setString(3, teacher.getName());
                statement.setString(4, teacher.getSchool().getCd());
                statement.setBoolean(5, teacher.isAdmin());
            } else {
                // 更新
                try {
                    // is_admin 列を使用する場合
                    statement = connection.prepareStatement(
                        "UPDATE teacher SET password = ?, name = ?, school_cd = ?, is_admin = ? WHERE id = ?"
                    );
                } catch (SQLException e) {
                    // admin 列を使用する場合
                    statement = connection.prepareStatement(
                        "UPDATE teacher SET password = ?, name = ?, school_cd = ?, admin = ? WHERE id = ?"
                    );
                }

                statement.setString(1, teacher.getPassword());
                statement.setString(2, teacher.getName());
                statement.setString(3, teacher.getSchool().getCd());
                statement.setBoolean(4, teacher.isAdmin());
                statement.setString(5, teacher.getId());
            }

            count = statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("教員情報の保存中にエラーが発生しました: " + e.getMessage());
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }

        return count > 0;
    }

    /**
     * 教員情報を削除
     */
    public boolean delete(String id) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        int count = 0;

        try {
            connection = getConnection();
            statement = connection.prepareStatement("DELETE FROM teacher WHERE id = ?");
            statement.setString(1, id);
            count = statement.executeUpdate();
        } catch (Exception e) {
            throw new Exception("教員情報の削除中にエラーが発生しました: " + e.getMessage());
        } finally {
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }

        return count > 0;
    }

    /**
     * ログイン認証
     */
    public Teacher login(String id, String password) throws Exception {
        Teacher teacher = get(id);

        // 教員が存在しない、またはパスワードが一致しない場合
        if (teacher == null || !teacher.getPassword().equals(password)) {
            return null;
        }

        // 認証成功したら、認証済みフラグを設定
        teacher.setAuthenticated(true);
        return teacher;
    }
}