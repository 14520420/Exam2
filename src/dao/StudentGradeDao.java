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

        String sql = "SELECT sub.name, t.point FROM test t JOIN subject sub ON t.subject_cd = sub.cd WHERE t.student_no = ? ORDER BY sub.cd";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, studentNo);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            StudentGrade g = new StudentGrade();
            g.setSubjectName(rs.getString("name"));
            g.setPoint(rs.getInt("point"));
            list.add(g);
        }
        rs.close(); stmt.close(); conn.close();
        return list;
    }
}

