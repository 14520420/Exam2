package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.StudentGrade;
import bean.Teacher;
import dao.StudentDao;
import dao.StudentGradeDao;
import tool.Action;

public class TestListByStudentAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン情報取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../Login.action");
            return;
        }

        // 学生番号を取得
        String studentNo = req.getParameter("student_no");
        if (studentNo == null || studentNo.trim().isEmpty()) {
            // 学生番号がない場合は成績参照画面に戻る
            req.setAttribute("error", "学生番号を入力してください");
            req.getRequestDispatcher("TestList.action").forward(req, res);
            return;
        }

        // 学生情報を取得
        StudentDao studentDao = new StudentDao();
        Student student = studentDao.get(studentNo);

        if (student == null) {
            // 学生が見つからない場合
            req.setAttribute("error", "指定された学生番号の学生が見つかりません");
            req.getRequestDispatcher("TestList.action").forward(req, res);
            return;
        }

        // 成績データを取得
        StudentGradeDao gradeDao = new StudentGradeDao();
        List<StudentGrade> grades = gradeDao.findByStudent(studentNo);

        // リクエスト属性に設定
        req.setAttribute("student", student);
        req.setAttribute("grades", grades);

        // JSPへフォワード
        req.getRequestDispatcher("test_list_by_student.jsp").forward(req, res);
    }
}