// クラス定義：このクラスは教員情報の取得・認証を行うDAOです
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.Teacher;

public class TeacherDao extends Dao {

	/**
	 * getメソッド 教員IDを指定して教員インスタンスを1件取得する
	 *
	 * @param id:String 教員ID
	 * @return 教員クラスのインスタンス（存在しない場合はnull）
	 * @throws Exception
	 */
	public Teacher get(String id) throws Exception {
		// 教員インスタンスを初期化
		Teacher teacher = new Teacher();
		// コネクションを確立
		Connection connection = getConnection();
		// プリペアードステートメント
		PreparedStatement statement = null;

		try {
			// SQL文をセット
			statement = connection.prepareStatement("select * from teacher where id=?");
			// 教員IDをバインド
			statement.setString(1, id);
			// SQLを実行
			ResultSet resultSet = statement.executeQuery();

			// 学校情報を取得するためのSchoolDao
			SchoolDao schoolDao = new SchoolDao();

			if (resultSet.next()) {
				// 結果がある場合、教員情報をセット
				teacher.setId(resultSet.getString("id"));
				teacher.setPassword(resultSet.getString("password"));
				teacher.setName(resultSet.getString("name"));
				// 学校情報をセット（学校コードから検索）
				teacher.setSchool(schoolDao.get(resultSet.getString("school_cd")));
			} else {
				// 結果がなければ null
				teacher = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			// statementを閉じる
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			// connectionを閉じる
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}

		return teacher;
	}

	/**
	 * loginメソッド 教員IDとパスワードで認証する
	 *
	 * @param id:String 教員ID
	 * @param password:String パスワード
	 * @return 認証成功：教員クラスのインスタンス、失敗：null
	 * @throws Exception
	 */
	public Teacher login(String id, String password) throws Exception {
		// 教員情報を取得
		Teacher teacher = get(id);
		// 教員が存在しない、またはパスワードが一致しない場合
		if (teacher == null || !teacher.getPassword().equals(password)) {
			return null;
		}
		// 認証成功
		return teacher;
	}
}
