// 6. StudentUpdateAction.java の修正版

package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentUpdateAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

        // 学生番号取得
        String no = req.getParameter("no");

        // 学生情報取得
        StudentDao studentDao = new StudentDao();
        Student student = studentDao.get(no);

        // 学生情報が存在する場合
        if (student != null) {
            // 入学年度選択肢の準備（現在から過去10年）
            int currentYear = LocalDate.now().getYear();
            List<Integer> entYearSet = new ArrayList<>();

            // 年度を降順で準備（現在→過去）
            for (int i = currentYear; i >= currentYear - 10; i--) {
                entYearSet.add(i);
            }

            // セッションから教員情報を取得し、学校コード取得
            HttpSession session = req.getSession();
            Teacher teacher = (Teacher) session.getAttribute("user");
            School school = teacher.getSchool();

            // クラス番号リストを取得
            ClassNumDao classNumDao = new ClassNumDao();
            List<String> sclassList = classNumDao.filter(school);

            // クラス番号をソート
            sclassList.sort((a, b) -> {
                try {
                    int aNum = Integer.parseInt(a);
                    int bNum = Integer.parseInt(b);
                    return Integer.compare(aNum, bNum);
                } catch (NumberFormatException e) {
                    // 数値変換できない場合は文字列比較
                    return a.compareTo(b);
                }
            });

            // リクエストスコープに属性をセット
            req.setAttribute("student", student);
            req.setAttribute("ent_year_set", entYearSet);
            req.setAttribute("sclassList", sclassList);

            // JSPへフォワード
            req.getRequestDispatcher("student_update.jsp").forward(req, res);
        } else {
            // エラー処理（学生が見つからない）
            req.setAttribute("error", "該当する学生が見つかりません。");
            req.getRequestDispatcher("/main/student_list.jsp").forward(req, res);
        }
    }
}