package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;
import bean.Student;

public class StudentDao extends Dao{

	String baseSql = "select * from student where school_cd=?";
	public static List<Student> filter(String schoolCd, String entYear, String classNum) throws Exception {
	    return new StudentDao().filter(schoolCd, entYear, classNum);
	}


	// 個人データ取得
	public Student get(String no) throws Exception {
		// インスタンス初期化
		Student student = new Student();
		Connection connection = getConnection();
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("SELECT * FROM student WHERE no = ? ORDER BY class_num, ent_year asc");
			statement.setString(1, no);
			ResultSet rSet = statement.executeQuery();

			SchoolDao schoolDao = new SchoolDao(); // 学校Dao初期化

			if (rSet.next()) {
				// リザルトセットが存在時、検索結果セット
				student.setNo(rSet.getString("no"));
				student.setName(rSet.getString("name"));
				student.setEntYear(rSet.getInt("ent_year"));
				student.setClassNum(rSet.getString("class_num"));
				student.setAttend(rSet.getBoolean("is_attend"));
				student.setSchool(schoolDao.get(rSet.getString("school_cd")));

			} else {
				student = null;
			}
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
		return student;
	}

//	filter設定　studentbeanへの登録処理のまとめ
	private List<Student> postFilter(ResultSet rSet, School school) throws Exception{
		List<Student> list = new ArrayList<>();
		try{
			while (rSet.next()){
				Student student = new Student();
				student.setNo(rSet.getString("no"));
				student.setName(rSet.getString("name"));
				student.setEntYear(rSet.getInt("ent_year"));
				student.setClassNum(rSet.getString("class_num"));
				student.setAttend(rSet.getBoolean("is_attend"));
				student.setSchool(school);
				list.add(student);
			}
		} catch (SQLException | NullPointerException e){ e.printStackTrace();}
		return list;
	}

	// 入学年度、クラスナンバー一致
	public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception {
	    List<Student> list = new ArrayList<>();
	    Connection connection = getConnection();
	    PreparedStatement statement = null;
	    ResultSet set = null;
	    String condition = " and ent_year=? and class_num=?";
	    String order = " order by class_num, ent_year asc";
	    String conditionIsAttend = "";
	    if (isAttend) {
	        conditionIsAttend = " and is_attend=true";
	    }
	    try {
	        statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order);
	        statement.setString(1, school.getCd());
	        statement.setInt(2, entYear);
	        statement.setString(3, classNum);
	        set = statement.executeQuery();
	        list = postFilter(set, school);

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

	// 入学年度一致
	public List<Student> filter(School school, int entYear,boolean isAttend)throws Exception{
		List<Student> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet rSet = null;
		String condition = " and ent_year=?";
		String order = " order by class_num, ent_year asc";
		String conditionIsAttend = "";
		if (isAttend) {
		    conditionIsAttend = " and is_attend=true";
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

	// 在学中のみ
	public List<Student> filter(School school,boolean isAttend) throws Exception{
	    List<Student> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet rSet = null;
		String order = " order by class_num, ent_year asc";
		String conditionIsAttend = "";
		if (isAttend) {
		    conditionIsAttend = " and is_attend=true";
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

	// クラス番号、学校番号
	public List<Student> filter(String classnum, School school)throws Exception{
		List<Student> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		ResultSet rSet = null;
		String condition = " and class_num = ?";
		String order = " order by ent_year asc, name";
		String conditionIsAttend = " and is_attend=true";
		try {
		    statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order);
		    statement.setString(1, school.getCd());
		    statement.setString(2, classnum);
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

	// 更新、新規追加
	public boolean save(Student student) throws Exception {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		// 実行件数
		int count = 0;

		try {
			Student old = get(student.getNo());
			if (old == null) {
				statement = connection.prepareStatement(
						"INSERT INTO student(no, name, ent_year, class_num, is_attend, school_cd) values(?, ?, ?, ?, ?, ?)");
				statement.setString(1, student.getNo());
				statement.setString(2, student.getName());
				statement.setInt(3, student.getEntYear());
				statement.setString(4, student.getClassNum());
				statement.setBoolean(5, student.isAttend());
				statement.setString(6, student.getSchool().getCd());
			} else {
				statement = connection
						.prepareStatement("UPDATE student SET name = ?, ent_year = ?, class_num = ?, is_attend = ? WHERE no = ?");
				statement.setString(1, student.getName());
				statement.setInt(2, student.getEntYear());
				statement.setString(3, student.getClassNum());
				statement.setBoolean(4, student.isAttend());
				statement.setString(5, student.getNo());
			}
			count = statement.executeUpdate();

		} catch (Exception e) {
			throw e;
		}  finally {
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

		if (count > 0) {
			return true;
		} else {
			return false;
		}

	}

	// postFilterのclassnumバージョン
	private List<ClassNum> cFilter(ResultSet rSet) throws Exception{
		List<ClassNum> list = new ArrayList<>();
		try{
			while (rSet.next()){
				ClassNum classNum = new ClassNum();
				classNum.setClass_num(rSet.getString("class_num"));
				classNum.setC_count(rSet.getInt("c_count"));
				list.add(classNum);
			}
		} catch (SQLException | NullPointerException e){ e.printStackTrace();}
		return list;
	}

	// クラスごとの学生数付き取得
	public List<ClassNum> class_count(School school)  throws Exception {
		// インスタンス初期化
		List<ClassNum> c_list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("SELECT class_num.class_num, Count(student.no) AS c_count FROM class_num LEFT JOIN student "
													+ "ON class_num.class_num = student.class_num AND class_num.school_cd = student.school_cd "
													+ "WHERE class_num.school_cd=? GROUP BY class_num.class_num ORDER BY class_num.class_num");
			statement.setString(1,school.getCd() );
			ResultSet rSet = statement.executeQuery();
			// セット
			c_list = cFilter(rSet);
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
		return c_list;
	}}



