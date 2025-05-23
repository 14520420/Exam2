package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestRegistAction extends Action {

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

        // DAO の初期化
        ClassNumDao classNumDao = new ClassNumDao();
        SubjectDao subjectDao = new SubjectDao();
        StudentDao studentDao = new StudentDao();
        TestDao testDao = new TestDao();

        // 入学年度リストを作成（2015年から現在年まで）
        int currentYear = LocalDate.now().getYear();
        List<Integer> entYearList = new ArrayList<>();
        for (int i = 2015; i <= currentYear; i++) {
            entYearList.add(i);
        }

        // クラス番号リストを取得
        List<String> classNumList = classNumDao.filter(school);

        // 科目一覧を取得
        List<Subject> subjectList = subjectDao.filter(school);

        // 回数リスト（1～10）
        List<Integer> noList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            noList.add(i);
        }

        // リクエスト属性に設定
        req.setAttribute("entYearList", entYearList);
        req.setAttribute("classNumList", classNumList);
        req.setAttribute("subjectList", subjectList);
        req.setAttribute("noList", noList);

        // リクエストパラメータの取得
        String entYear = req.getParameter("ent_year");
        String classNum = req.getParameter("class_num");
        String subjectCd = req.getParameter("subject_cd");
        String no = req.getParameter("no");

        // 検索条件が指定されている場合
        if (entYear != null && !entYear.isEmpty() &&
            classNum != null && !classNum.isEmpty() &&
            subjectCd != null && !subjectCd.isEmpty() &&
            no != null && !no.isEmpty()) {

            try {
                // 入力値の検証
                int entYearInt = Integer.parseInt(entYear);
                int noInt = Integer.parseInt(no);

                // 科目情報を取得
                Subject subject = subjectDao.get(subjectCd, school);
                if (subject == null) {
                    req.setAttribute("error", "指定された科目が見つかりません");
                    req.getRequestDispatcher("test_regist.jsp").forward(req, res);
                    return;
                }

                // 学生一覧を取得
                List<Student> studentList = studentDao.filter(school, entYearInt, classNum, true);

                if (studentList.isEmpty()) {
                    req.setAttribute("error", "指定された条件に該当する学生が見つかりません");
                } else {
                    // 既存の成績を取得
                    Map<String, Integer> existingPoints = new HashMap<>();
                    for (Student student : studentList) {
                        Test existingTest = testDao.get(student, subject, school, noInt);
                        if (existingTest != null) {
                            existingPoints.put(student.getNo(), existingTest.getPoint());
                        }
                    }

                    req.setAttribute("studentList", studentList);
                    req.setAttribute("existingPoints", existingPoints);
                    req.setAttribute("selectedSubjectName", subject.getName());
                }

                // 選択した条件を保持
                req.setAttribute("selectedEntYear", entYear);
                req.setAttribute("selectedClassNum", classNum);
                req.setAttribute("selectedSubjectCd", subjectCd);
                req.setAttribute("selectedNo", no);

            } catch (NumberFormatException e) {
                req.setAttribute("error", "入学年度または回数の値が正しくありません");
            } catch (Exception e) {
                req.setAttribute("error", "データ取得中にエラーが発生しました: " + e.getMessage());
            }
        }

        // JSPへフォワード
        req.getRequestDispatcher("test_regist.jsp").forward(req, res);
    }
}