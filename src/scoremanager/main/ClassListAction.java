// 7. ClassListAction.java の修正版

package scoremanager.main;

import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.ClassNum;
import bean.School;
import bean.Teacher;
import dao.StudentDao;
import tool.Action;

public class ClassListAction extends Action {

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

        // 学校情報を取得
        School school = teacher.getSchool();

        // クラス情報を取得（学生数を含む）
        StudentDao studentDao = new StudentDao();
        List<ClassNum> classList = studentDao.class_count(school);

        // クラス番号をソート（数値として）
        classList.sort(new Comparator<ClassNum>() {
            @Override
            public int compare(ClassNum a, ClassNum b) {
                try {
                    int aNum = Integer.parseInt(a.getClass_num());
                    int bNum = Integer.parseInt(b.getClass_num());
                    return Integer.compare(aNum, bNum);
                } catch (NumberFormatException e) {
                    // 数値変換できない場合は文字列比較
                    return a.getClass_num().compareTo(b.getClass_num());
                }
            }
        });

        // リクエスト属性に設定
        req.setAttribute("classList", classList);
        req.setAttribute("school", school);

        // JSPへフォワード
        req.getRequestDispatcher("class_list.jsp").forward(req, res);
    }
}