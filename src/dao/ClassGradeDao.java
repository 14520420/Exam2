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

        String sql = "SELECT sub.name, AVG(t.point) AS avg_point FROM test t JOIN subject sub ON t.subject_cd = sub.cd WHERE t.class_num = ? GROUP BY sub.name ORDER BY sub.name";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, classNum);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            ClassGrade g = new ClassGrade();
            g.setSubjectName(rs.getString("name"));
            g.setAvgPoint(rs.getDouble("avg_point"));
            list.add(g);
        }
        rs.close(); stmt.close(); conn.close();
        return list;
    }
}
