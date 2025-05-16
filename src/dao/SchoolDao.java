package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;

public class SchoolDao extends Dao {
    /**
     * getメソッド 学校コードを指定して学校インスタンスを１件取得する
     *
     * @param cd:String 学校コード
     * @return 学校クラスのインスタンス 存在しない場合はnull
     * @throws Exception
     */
    public School get(String cd) throws Exception {
        // 学校インスタンスを初期化
        School school = new School();
        // データベースへのコネクションを確立
        Connection connection = getConnection();
        // プリペアードステートメント
        PreparedStatement statement = null;

        try {
            // プリペアードステートメントにSQL文をセット
            statement = connection.prepareStatement("select * from school where cd = ?");
            // プリペアードステートメントに学校コードをバインド
            statement.setString(1, cd);
            // プリペアードステートメントを実行
            ResultSet rSet = statement.executeQuery();

            if (rSet.next()) {
                // リザルトセットが存在する場合：学校情報をセット
                school.setCd(rSet.getString("cd"));
                school.setName(rSet.getString("name"));
            } else {
                // 存在しない場合：nullをセット
                school = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // プリペアードステートメントを閉じる
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
            // コネクションを閉じる
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    throw sqle;
                }
            }
        }
        return school;
    }

    /**
     * 全学校の一覧を取得する
     *
     * @return 学校のリスト
     * @throws Exception
     */
    public List<School> getAllSchools() throws Exception {
        List<School> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = getConnection();
            statement = connection.prepareStatement("SELECT * FROM school ORDER BY cd");
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                School school = new School();
                school.setCd(resultSet.getString("cd"));
                school.setName(resultSet.getString("name"));
                list.add(school);
            }
        } catch (Exception e) {
            throw new Exception("学校一覧の取得中にエラーが発生しました: " + e.getMessage(), e);
        } finally {
            if (resultSet != null) try { resultSet.close(); } catch (SQLException e) {}
            if (statement != null) try { statement.close(); } catch (SQLException e) {}
            if (connection != null) try { connection.close(); } catch (SQLException e) {}
        }

        return list;
    }
}