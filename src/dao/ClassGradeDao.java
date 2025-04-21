package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import bean.ClassGrade;

public class ClassGradeDao extends Dao {

    public List<ClassGrade> findByClass(String classNum) throws Exception {
        List<ClassGrade> list = new ArrayList<>();

        Connection conn = getConnection();

        String sql = "SELECT s.class_num, sub.name AS subject_name, AVG(t.point) AS avg_point " +
                     "FROM student s " +
                     "JOIN test t ON s.no = t.student_no " +
                     "JOIN subject sub ON t.subject_cd = sub.cd " +
                     "WHERE s.class_num = ? " +
                     "GROUP BY s.class_num, sub.name";

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, classNum);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            ClassGrade cg = new ClassGrade(
                rs.getString("class_num"),
                rs.getString("subject_name"),
                rs.getDouble("avg_point")
            );
            list.add(cg);
        }

        rs.close();
        stmt.close();
        conn.close();
        return list;
    }
}


