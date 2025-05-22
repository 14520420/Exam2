package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.Student;
import bean.StudentGrade;
import dao.StudentDao;
import dao.StudentGradeDao;
import tool.Action;

public class StudentGradeListAction extends Action {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String studentNo = request.getParameter("student_no");

        // 学生情報も取得
        StudentDao studentDao = new StudentDao();
        Student student = studentDao.get(studentNo);

        if (student != null) {
            request.setAttribute("studentName", student.getName());
            request.setAttribute("studentNo", student.getNo());
        }

        StudentGradeDao dao = new StudentGradeDao();
        List<StudentGrade> grades = dao.findByStudent(studentNo);

        request.setAttribute("grades", grades);
        request.getRequestDispatcher("student_grade_list.jsp").forward(request, response);
    }
}