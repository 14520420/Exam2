package scoremanager.main;

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
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestListBySubjectAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン情報取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../Login.action");
            return;
        }

        School school = teacher.getSchool();

        // パラメータ取得
        String entYearStr = req.getParameter("ent_year");
        String classNum = req.getParameter("class_num");
        String subjectCd = req.getParameter("subject_cd");

        // パラメータチェック
        if (entYearStr == null || classNum == null || subjectCd == null ||
            entYearStr.trim().isEmpty() || classNum.trim().isEmpty() || subjectCd.trim().isEmpty()) {
            req.setAttribute("error", "入学年度、クラス、科目をすべて選択してください");
            req.getRequestDispatcher("TestList.action").forward(req, res);
            return;
        }

        int entYear = Integer.parseInt(entYearStr);

        // 科目情報を取得
        SubjectDao subjectDao = new SubjectDao();
        Subject subject = subjectDao.get(subjectCd, school);

        if (subject == null) {
            req.setAttribute("error", "指定された科目が見つかりません");
            req.getRequestDispatcher("TestList.action").forward(req, res);
            return;
        }

        // 学生リストを取得
        StudentDao studentDao = new StudentDao();
        List<Student> students = studentDao.filter(school, entYear, classNum, true);

        // テスト情報を取得
        TestDao testDao = new TestDao();
        Map<String, String> cond = new HashMap<>();
        cond.put("entYear", entYearStr);
        cond.put("classNum", classNum);
        cond.put("subjectCd", subjectCd);
        List<Test> tests = testDao.selectByConditions(cond, school);

        // テスト結果をマップに整理 (学生番号をキーに)
        Map<String, Map<Integer, Integer>> testResults = new HashMap<>();
        for (Test test : tests) {
            String studentNo = test.getStudent().getNo();
            int testNo = test.getNo();
            int point = test.getPoint();

            if (!testResults.containsKey(studentNo)) {
                testResults.put(studentNo, new HashMap<>());
            }
            testResults.get(studentNo).put(testNo, point);
        }

        // リクエスト属性に設定
        req.setAttribute("entYear", entYear);
        req.setAttribute("classNum", classNum);
        req.setAttribute("subject", subject);
        req.setAttribute("students", students);
        req.setAttribute("testResults", testResults);

        // JSPへフォワード
        req.getRequestDispatcher("test_list_by_subject.jsp").forward(req, res);
    }
}