// 2. StudentCreateAction.java の修正版

package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import tool.Action;


public class StudentCreateAction extends Action{
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        //セッションのユーザデータを取得
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        // セッションチェック
        if (teacher == null || !teacher.isAuthenticated()) {
            res.sendRedirect("../Login.action");
            return;
        }

        String entYearStr = "";//入力された入学年度
        String classNum = "";//入力されたクラス番号
        String isAttendStr = "";
        int entYear = 0;//入学年度
        boolean isAttend = false;
        List<Student> students = null;//学生リスト

        LocalDate todaysDate = LocalDate.now();//LcalDateインスタンスを取得
        int currentYear = todaysDate.getYear();//現在の年を取得

        ClassNumDao cNumDao = new ClassNumDao();//クラス番号Dao

        //リクエストパラメータ―の取得
        entYearStr = req.getParameter("f1");
        classNum = req.getParameter("f2");
        isAttendStr = req.getParameter("f3");

        //ビジネスロジック
        if (entYearStr != null && !entYearStr.isEmpty()) {
            //数値に変換
            try {
                entYear = Integer.parseInt(entYearStr);
            } catch (NumberFormatException e) {
                entYear = 0; // エラー時はデフォルト値
            }
        }

        //list初期化 - 年度リストを降順に設定（現在から過去へ）
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = currentYear; i >= currentYear - 10; i--) {
            entYearSet.add(i);
        }

        //DBからデータ取得
        List<String> list = cNumDao.filter(teacher.getSchool());

        // クラス番号のソート
        list.sort((a, b) -> {
            try {
                int aNum = Integer.parseInt(a);
                int bNum = Integer.parseInt(b);
                return Integer.compare(aNum, bNum);
            } catch (NumberFormatException e) {
                // 数値変換できない場合は文字列比較
                return a.compareTo(b);
            }
        });

        //レスポンス値をセット 6
        //リクエストに入学年度をセット
        req.setAttribute("f1", entYear);

        //リクエストにクラス番号をセット
        req.setAttribute("f2", classNum);

        if (isAttendStr != null) {
            isAttend = true;
            req.setAttribute("f3", isAttendStr);
        }

        //リクエストに学生をセット
        req.setAttribute("students", students);

        //リクエストにデータをセット
        req.setAttribute("class_num_set", list);
        req.setAttribute("ent_year_set", entYearSet);

        //フォワード
        req.getRequestDispatcher("student_create.jsp").forward(req, res);
    }
}