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
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import tool.Action;

public class TestListAction extends Action {

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

    // プルダウン用データの準備
    // 入学年度リストを作成（現在年から20年前まで）
    int currentYear = LocalDate.now().getYear();
    List<Integer> entYearSet = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      entYearSet.add(currentYear - i);
    }
    req.setAttribute("ent_year_set", entYearSet);

    // クラス番号リストを取得（全クラス）
    ClassNumDao cDao = new ClassNumDao();
    List<String> classNumSet = cDao.filter(school);
    req.setAttribute("class_num_set", classNumSet);

    // 科目一覧を取得（全科目）
    SubjectDao sDao = new SubjectDao();
    List<Subject> subjects = sDao.filter(school);
    req.setAttribute("subjects", subjects);

    // 回数リスト（1～10）
    List<Integer> noSet = new ArrayList<>();
    for (int i = 1; i <= 10; i++) {
      noSet.add(i);
    }
    req.setAttribute("no_set", noSet);

    // リクエストパラメータを取得
    String entYear   = nv(req.getParameter("f1"));
    String classNum  = nv(req.getParameter("f2"));
    String subjectCd = nv(req.getParameter("f3"));
    String testNo    = nv(req.getParameter("f4"));

    // 検索条件マップの作成
    Map<String, String> cond = new HashMap<>();
    if (!entYear.isEmpty()) cond.put("entYear", entYear);
    if (!classNum.isEmpty()) cond.put("classNum", classNum);
    if (!subjectCd.isEmpty()) cond.put("subjectCd", subjectCd);
    if (!testNo.isEmpty()) cond.put("no", testNo);

    // 検索実行
    TestDao testDao = new TestDao();
    List<Test> tests = null;

    // 検索ボタンが押されたかどうか
    boolean isSearchSubmitted = req.getParameterMap().containsKey("f1");

    // 初期表示時またはすべての条件が未選択の場合は全データを表示
    if (!isSearchSubmitted || cond.isEmpty()) {
      tests = testDao.getAllTests(school);
    } else {
      // 条件付き検索
      tests = testDao.selectByConditions(cond, school);
    }

    // リクエスト属性に設定
    req.setAttribute("conditions", cond);
    req.setAttribute("tests", tests);

    // JSPへフォワード
    req.getRequestDispatcher("test_list.jsp").forward(req, res);
  }

  // null または 空文字 を空文字に変換
  private String nv(String s) {
    return s == null ? "" : s.trim();
  }
}