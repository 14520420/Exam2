package scoremanager.main;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Teacher;
import dao.TeacherDao;
import tool.Action;

public class TeacherCsvExportAction extends Action {

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

        // エクスポート処理を実行
        try {
            // 教員一覧を取得
            TeacherDao dao = new TeacherDao();
            List<Teacher> teacherList = dao.getAllTeachers();

            // 現在日時を取得してファイル名に使用
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
            String timestamp = format.format(new Date());
            String filename = "teachers_" + timestamp + ".csv";

            // レスポンスの設定
            res.setContentType("text/csv; charset=UTF-8");
            res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");

            // CSVを書き出し
            try (PrintWriter out = res.getWriter()) {
                for (Teacher t : teacherList) {
                    StringBuilder line = new StringBuilder();
                    line.append(escapeCSV(t.getId())).append(",");
                    line.append(escapeCSV(t.getPassword())).append(",");
                    line.append(escapeCSV(t.getName())).append(",");
                    line.append(escapeCSV(t.getSchool().getCd())).append(",");
                    line.append(t.isAdmin() ? "1" : "0");
                    out.println(line.toString());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            req.setAttribute("error", "CSVエクスポート処理中にエラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("TeacherList.action").forward(req, res);
        }
    }

    /**
     * CSV用にテキストをエスケープする
     * @param str エスケープ対象文字列
     * @return エスケープ済み文字列
     */
    private String escapeCSV(String str) {
        if (str == null) {
            return "";
        }

        // カンマやダブルクォートを含む場合はダブルクォートで囲む
        if (str.contains(",") || str.contains("\"") || str.contains("\n")) {
            return "\"" + str.replace("\"", "\"\"") + "\"";
        }
        return str;
    }
}