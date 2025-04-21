//学生
package scoremanager.main;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.StudentGrade;
import dao.StudentGradeDao;
import tool.Action;

public class StudentGradeListAction extends Action {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String studentNo = request.getParameter("student_no");

        StudentGradeDao dao = new StudentGradeDao();
        List<StudentGrade> list = dao.findByStudent(studentNo);

        request.setAttribute("grades", list);
        request.setAttribute("student_no", studentNo);

        RequestDispatcher dispatcher = request.getRequestDispatcher("scoremanager/main/student_grade_list.jspp");
        dispatcher.forward(request, response);
    }
}


