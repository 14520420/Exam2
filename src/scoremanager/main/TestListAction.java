// 同様に、他のアクションクラスでも年度表示範囲の拡大
// TestListAction.java の修正版

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
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassGradeDao;
import dao.ClassNumDao;
import dao.StudentGradeDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestListAction extends Action {

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

    // リクエストパラメータを取得
    String entYear   = nv(req.getParameter("f1"));
    String classNum  = nv(req.getParameter("f2"));
    String subjectCd = nv(req.getParameter("f3"));
    String testNo    = nv(req.getParameter("f4"));
    String studentKW = nv(req.getParameter("studentInfo"));

    // プルダウン用データを準備 - 年度範囲を拡大
    int currentYear = LocalDate.now().getYear();
    List<Integer> entYearSet = new ArrayList<>();

    // 過去20年と未来5年を表示（より広い範囲）
    for (int y = currentYear + 5; y >= currentYear - 20; y--) {
        entYearSet.add(y);
    }

    ClassNumDao cDao = new ClassNumDao();
    SubjectDao  sDao = new SubjectDao();
    List<String> classNumSet = nvl(cDao.filter(school));

    // クラス番号のソート
    classNumSet.sort((a, b) -> {
        try {
            int aNum = Integer.parseInt(a);
            int bNum = Integer.parseInt(b);
            return Integer.compare(aNum, bNum);
        } catch (NumberFormatException e) {
            return a.compareTo(b);
        }
    });

    List<Subject> subjects = nvl(sDao.filter(school));

    req.setAttribute("ent_year_set", entYearSet);
    req.setAttribute("class_num_set", classNumSet);
    req.setAttribute("subjects", subjects);

    // 検索条件マップの作成
    Map<String, String> cond = new HashMap<>();
    if (!entYear.isEmpty()) cond.put("entYear", entYear);
    if (!classNum.isEmpty()) cond.put("classNum", classNum);
    if (!subjectCd.isEmpty()) cond.put("subjectCd", subjectCd);
    if (!testNo.isEmpty()) cond.put("no", testNo);
    if (!studentKW.isEmpty()) cond.put("studentInfo", studentKW);

    // テスト情報の取得
    TestDao tDao = new TestDao();
    List<Test> tests = tDao.selectByConditions(cond, school);

    // リクエスト属性に設定
    req.setAttribute("conditions", cond);
    req.setAttribute("tests", nvl(tests));

    // クラスおよび学生の成績一覧が必要な場合は取得
    if (!classNum.isEmpty()) {
        ClassGradeDao cgDao = new ClassGradeDao();
        req.setAttribute("classGrades", cgDao.findByClass(classNum));
    }

    if (!studentKW.isEmpty()) {
        StudentGradeDao sgDao = new StudentGradeDao();
        req.setAttribute("studentGrades", sgDao.findByStudent(studentKW));
    }

    // JSPへフォワード
    req.getRequestDispatcher("test_list.jsp").forward(req, res);
  }

  // null または 空文字 を空文字に変換
  private String nv(String s) {
      return s == null ? "" : s.trim();
  }

  // null のリストを空のリストに変換
  private <T> List<T> nvl(List<T> l) {
      return l == null ? new ArrayList<>() : l;
  }
}