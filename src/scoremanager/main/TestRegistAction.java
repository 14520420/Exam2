
package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import tool.Action;

public class TestRegistAction extends Action {
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            // ログインユーザー情報取得
            HttpSession session = request.getSession();
            Teacher teacher = (Teacher) session.getAttribute("user");

            if (teacher == null || !teacher.isAuthenticated()) {
                response.sendRedirect("../Login.action");
                return;
            }

            School school = teacher.getSchool();

            // 入学年度リストを作成（現在年から過去30年、未来10年までを表示）
            int currentYear = LocalDate.now().getYear();
            List<Integer> entYearList = new ArrayList<>();
            for (int i = currentYear + 10; i >= currentYear - 30; i--) {
                entYearList.add(i);
            }
            request.setAttribute("entYearList", entYearList);

            // クラス番号リストを取得（全クラス）
            ClassNumDao classNumDao = new ClassNumDao();
            List<String> classNumStrList = classNumDao.filter(school);
            request.setAttribute("classNumList", classNumStrList);

            // 科目一覧を取得（全科目）
            SubjectDao subjectDao = new SubjectDao();
            List<Subject> subjectList = subjectDao.filter(school);
            request.setAttribute("subjectList", subjectList);

            // 回数リスト（1～10）
            List<Integer> noList = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                noList.add(i);
            }
            request.setAttribute("noList", noList);

            // 検索条件の取得
            String entYear = request.getParameter("ent_year");
            String classNum = request.getParameter("class_num");
            String subjectCd = request.getParameter("subject_cd");
            String no = request.getParameter("no");

            // 検索条件のチェック
            boolean hasSearchParams = request.getParameterMap().containsKey("ent_year");

            if (hasSearchParams) {
                // 必須項目のチェック
                if (isEmpty(entYear) || isEmpty(classNum) || isEmpty(subjectCd) || isEmpty(no)) {
                    request.setAttribute("error", "入学年度とクラスと科目と回数を選択してください");

                    // 入力値の保持
                    request.setAttribute("selectedEntYear", entYear);
                    request.setAttribute("selectedClassNum", classNum);
                    request.setAttribute("selectedSubjectCd", subjectCd);
                    request.setAttribute("selectedNo", no);

                    request.getRequestDispatcher("test_regist.jsp").forward(request, response);
                    return;
                }

                // 学生一覧取得と結果表示
                try {
                    // 科目名を取得
                    Subject subject = subjectDao.get(subjectCd, school);
                    if (subject != null) {
                        request.setAttribute("subjectName", subject.getName());
                    }

                    // 学生一覧取得処理
                    StudentDao studentDao = new StudentDao();
                    List<Student> studentList = studentDao.filter(school,
                                                                Integer.parseInt(entYear),
                                                                classNum,
                                                                true);
                    request.setAttribute("studentList", studentList);

                    // 条件の保持（hidden再表示用）
                    request.setAttribute("selectedEntYear", entYear);
                    request.setAttribute("selectedClassNum", classNum);
                    request.setAttribute("selectedSubjectCd", subjectCd);
                    request.setAttribute("selectedNo", no);
                    request.setAttribute("subjectName", subject != null ? subject.getName() : "");

                    request.getRequestDispatcher("test_regist_result.jsp").forward(request, response);
                } catch (Exception e) {
                    // エラー処理
                    request.setAttribute("error", "データ取得中にエラーが発生しました: " + e.getMessage());
                    request.getRequestDispatcher("test_regist.jsp").forward(request, response);
                }
            } else {
                // 初期表示
                request.getRequestDispatcher("test_regist.jsp").forward(request, response);
            }
        } catch (Exception e) {
            // エラー処理
            request.setAttribute("error", "データ取得中にエラーが発生しました: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    // 空チェック
    private boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }
}
