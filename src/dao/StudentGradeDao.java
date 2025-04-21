package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.StudentGrade;

public class StudentGradeDao extends Dao {

    public List<StudentGrade> findByStudent(String studentNo) throws Exception {
        List<StudentGrade> list = new ArrayList<>();

        Connection conn = getConnection();

        String sql = "SELECT s.no, s.name, sub.name AS subject_name, t.point " +
                     "FROM student s " +
                     "JOIN test t ON s.no = t.student_no " +
                     "JOIN subject sub ON t.subject_cd = sub.cd " +
                     "WHERE s.no = ?";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, studentNo);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            StudentGrade sg = new StudentGrade(
                rs.getString("no"),
                rs.getString("name"),
                rs.getString("subject_name"),
                rs.getInt("point")
            );
            list.add(sg);
        }

        rs.close();
        stmt.close();
        conn.close();
        return list;
    }
}



