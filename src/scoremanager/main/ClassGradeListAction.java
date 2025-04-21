//クラス
package scoremanager.main;

import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bean.ClassGrade;
import dao.ClassGradeDao;
import tool.Action;

public class ClassGradeListAction extends Action {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String classNum = request.getParameter("class_num");

        ClassGradeDao dao = new ClassGradeDao();
        List<ClassGrade> list = dao.findByClass(classNum);

        request.setAttribute("grades", list);
        request.setAttribute("class_num", classNum);

        RequestDispatcher dispatcher = request.getRequestDispatcher("scoremanager/main/class_student_grade_list.jsp");
        dispatcher.forward(request, response);
    }
}


