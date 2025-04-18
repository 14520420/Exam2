package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;


// クラス定義：このクラスはデータ構造やロジックの単位として使用されます
public class StudentDao extends Dao{

	// baseSql="select：String型のフィールド
	private String baseSql="select * from student where school_cd=?";

	private List<Student> postFilter(ResultSet rSet, School school) throws Exception{
		//リストの初期化
		List<Student> list = new ArrayList<>();
		try{
			//リザルトセットを全件走査
			while(rSet.next()){
				//学生インスタンスを初期化
				Student student = new Student();
				//学生インスタンスに検索結果をセット
				student.setNo(rSet.getString("no"));
				student.setName(rSet.getString("name"));
				student.setEntYear(rSet.getInt("ent_year"));
				student.setClassNum(rSet.getString("class_num"));
				student.setAttend(rSet.getBoolean("is_attend"));
				student.setSchool(school);
				//リストに追加
				list.add(student);
			}
		}catch (SQLException | NullPointerException e){
			e.printStackTrace();
		}
		return list;
	}

	// getメソッド：学生番号で1人の学生情報を取得する
	public Student get(String no) throws Exception{
		//学生インスタンスを初期化
		Student student = new Student();
		//データベースへのコネクションを確立
		Connection connection = getConnection();
		//プリペアードステートメント
		PreparedStatement statement = null;

		try {
			//SQL文をセット
			statement = connection.prepareStatement("select * from student where no=?");
			// 学生番号をバインド
			statement.setString(1, no);
			//SQL実行
			ResultSet rSet = statement.executeQuery();

			// 学校情報取得用DAO
			SchoolDao schoolDao = new SchoolDao();

			if (rSet.next()) {
				//リザルトセットが存在する場合
				student.setNo(rSet.getString("no"));
				student.setName(rSet.getString("name"));
				student.setEntYear(rSet.getInt("ent_year"));
				student.setClassNum(rSet.getString("class_num"));
				student.setAttend(rSet.getBoolean("is_attend"));
				student.setSchool(schoolDao.get(rSet.getString("school_cd")));
			} else {
				// 存在しない場合
				student = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			// クローズ処理
			if(statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle){
					throw sqle;
				}
			}
			if (connection != null){
				try {
					connection.close();
				} catch (SQLException sqle){
					throw sqle;
				}
			}
		}

		return student;
	}
	// filterメソッド：学年・クラス・在学状態を指定して絞り込む
	public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception {
		List<Student> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet rSet = null;
		String condition = "and ent_year=? and class_num=?";
		String order = " order by no asc";
		String conditionIsAttend = "";

		if (isAttend) {
			conditionIsAttend = "and is_attend = true";
		}

		try {
			statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order);
			statement.setString(1, school.getCd());
			statement.setInt(2, entYear);
			statement.setString(3, classNum);
			rSet = statement.executeQuery();
			list = postFilter(rSet, school);
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
		return list;
	}

	// filterメソッド：学年と在学状態で絞り込む
	public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception {
		List<Student> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet rSet = null;
		String condition = "and ent_year=?";
		String order = "order by no asc";
		String conditionIsAttend = "";

		if (isAttend) {
			conditionIsAttend = "and is_attend = true";
		}

		try {
			statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order);
			statement.setString(1, school.getCd());
			statement.setInt(2, entYear);
			rSet = statement.executeQuery();
			list = postFilter(rSet, school);
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
		return list;
	}

	// filterメソッド：学校と在学状態のみで絞り込む
	public List<Student> filter(School school, boolean isAttend) throws Exception {
		List<Student> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet rSet = null;
		String order = " order by no asc";
		String conditionIsAttend = "";

		if (isAttend) {
			conditionIsAttend = "and is_attend = true";
		}

		try {
			statement = connection.prepareStatement(baseSql + conditionIsAttend + order);
			statement.setString(1, school.getCd());
			rSet = statement.executeQuery();
			list = postFilter(rSet, school);
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
		return list;
	}

	// saveメソッド：学生情報を挿入または更新
	public boolean save(Student student) throws Exception {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		int count = 0;

		try {
			Student old = get(student.getNo());
			if (old == null) {
				// 存在しない場合：INSERT
				statement = connection.prepareStatement(
					"insert into student(no, name, ent_year, class_num, is_attend, school_cd) values(?,?,?,?,?,?)"
				);
				statement.setString(1, student.getNo());
				statement.setString(2, student.getName());
				statement.setInt(3, student.getEntYear());
				statement.setString(4, student.getClassNum());
				statement.setBoolean(5, student.isAttend());
				statement.setString(6, student.getSchool().getCd());
			} else {
				// 存在する場合：UPDATE
				statement = connection.prepareStatement(
					"update student set name=?, ent_year=?, class_num=?, is_attend=? where no=?"
				);
				statement.setString(1, student.getName());
				statement.setInt(2, student.getEntYear());
				statement.setString(3, student.getClassNum());
				statement.setBoolean(4, student.isAttend());
				statement.setString(5, student.getNo());
			}
			count = statement.executeUpdate();
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException sqle) {
					throw sqle;
				}
			}
		}
		return count > 0;
	}
}

