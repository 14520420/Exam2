package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;

public class TestDao extends Dao {

    private String baseSql = "SELECT * FROM test WHERE school_cd = ?";

    private List<Test> postFilter(ResultSet rSet, School school) throws Exception {
        List<Test> list = new ArrayList<>();

        while (rSet.next()) {
            Test test = new Test();

            // student, subject はIDしか取れないので取得は別クラスに任せる
            Student student = new Student();
            student.setNo(rSet.getString("student_no"));
            test.setStudent(student);

            Subject subject = new Subject();
            subject.setCd(rSet.getString("subject_cd"));
            test.setSubject(subject);

            test.setSchool(school);
            test.setNo(rSet.getInt("no"));
            test.setPoint(rSet.getInt("point"));
            test.setClassNum(rSet.getString("class_num"));

            list.add(test);
        }

        return list;
    }

    public Test get(Student student, Subject subject, School school, int no) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet rSet = null;
        Test test = null;

        try {
            statement = connection.prepareStatement(
                "SELECT * FROM test WHERE student_no=? AND subject_cd=? AND school_cd=? AND no=?"
            );
            statement.setString(1, student.getNo());
            statement.setString(2, subject.getCd());
            statement.setString(3, school.getCd());
            statement.setInt(4, no);

            rSet = statement.executeQuery();

            if (rSet.next()) {
                test = new Test();
                test.setStudent(student);
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(no);
                test.setPoint(rSet.getInt("point"));
                test.setClassNum(rSet.getString("class_num"));
            }
        } finally {
            if (rSet != null) rSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return test;
    }

    public List<Test> filter(int entYear, String classNum, Subject subject, int num, School school) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet rSet = null;
        List<Test> list = new ArrayList<>();

        try {
            // 学生情報と結合して入学年度でフィルター
            String sql = baseSql + " AND class_num=? AND subject_cd=? AND no=?"
                       + " AND student_no IN (SELECT no FROM student WHERE ent_year=?)";

            statement = connection.prepareStatement(sql);
            statement.setString(1, classNum);
            statement.setString(2, subject.getCd());
            statement.setInt(3, num);
            statement.setInt(4, entYear);
            rSet = statement.executeQuery();

            list = postFilter(rSet, school);

        } finally {
            if (rSet != null) rSet.close();
            if (statement != null) statement.close();
            if (connection != null) connection.close();
        }

        return list;
    }

    public boolean save(List<Test> list) throws Exception {
        Connection connection = getConnection();
        boolean result = true;

        try {
            connection.setAutoCommit(false);

            for (Test test : list) {
                if (!save(test, connection)) {
                    result = false;
                    break;
                }
            }

            if (result) {
                connection.commit();
            } else {
                connection.rollback();
            }

        } finally {
            if (connection != null) connection.close();
        }

        return result;
    }

    public boolean save(Test test, Connection connection) throws Exception {
        PreparedStatement statement = null;
        boolean result = false;

        try {
            // 既存データ確認
            Test old = get(test.getStudent(), test.getSubject(), test.getSchool(), test.getNo());

            if (old == null) {
                statement = connection.prepareStatement(
                    "INSERT INTO test (student_no, subject_cd, school_cd, no, point, class_num) VALUES (?, ?, ?, ?, ?, ?)"
                );
            } else {
                statement = connection.prepareStatement(
                    "UPDATE test SET point=?, class_num=? WHERE student_no=? AND subject_cd=? AND school_cd=? AND no=?"
                );
            }

            if (old == null) {
                statement.setString(1, test.getStudent().getNo());
                statement.setString(2, test.getSubject().getCd());
                statement.setString(3, test.getSchool().getCd());
                statement.setInt(4, test.getNo());
                statement.setInt(5, test.getPoint());
                statement.setString(6, test.getClassNum());
            } else {
                statement.setInt(1, test.getPoint());
                statement.setString(2, test.getClassNum());
                statement.setString(3, test.getStudent().getNo());
                statement.setString(4, test.getSubject().getCd());
                statement.setString(5, test.getSchool().getCd());
                statement.setInt(6, test.getNo());
            }

            result = statement.executeUpdate() > 0;

        } finally {
            if (statement != null) statement.close();
        }

        return result;
    }
}
