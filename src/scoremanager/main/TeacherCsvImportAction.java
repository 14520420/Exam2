package scoremanager.main;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Teacher;
import dao.SchoolDao;
import tool.Action;

public class TeacherCsvImportAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン教師情報を取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // 未ログインの場合はログイン画面へリダイレクト
        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../Login.action");
            return;
        }

        // 管理者権限のチェック
        if (!teacher.isAdmin()) {
            req.setAttribute("error", "この機能を使用する権限がありません");
            req.getRequestDispatcher("error.jsp").forward(req, res);
            return;
        }

        // 学校一覧を取得して画面に渡す (インポート時に学校コードの参照用)
        SchoolDao schoolDao = new SchoolDao();
        List<School> schools = schoolDao.getAllSchools();
        req.setAttribute("schoolList", schools);

        // JSPへフォワード
        req.getRequestDispatcher("teacher_csv_import.jsp").forward(req, res);
    }
}