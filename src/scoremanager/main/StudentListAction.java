// StudentListAction.java の修正版 - 過去の年度も未来の年度も全て表示

package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import tool.Action;

public class StudentListAction extends Action {
    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        HttpSession session = req.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        String entYearStr="";
        String classNum="";
        String isAttendStr="";
        int entYear=0;
        boolean isAttend = false;
        List<Student> students = null;

        LocalDate todaysDate = LocalDate.now();
        int currentYear = todaysDate.getYear();

        StudentDao sDao = new StudentDao();
        ClassNumDao cNumDao = new ClassNumDao();
        Map<String, String> errors = new HashMap<>();

        //リクエストパラメータ―の取得
        entYearStr = req.getParameter("f1");
        classNum = req.getParameter("f2");
        isAttendStr = req.getParameter("f3");
        if (entYearStr != null && !entYearStr.isEmpty()) {
            try {
                entYear = Integer.parseInt(entYearStr);
            } catch (NumberFormatException e) {
                errors.put("f1", "入学年度は数値で指定してください");
            }
        }

        // 年度のリストを作成 - 過去と未来も含めた広い範囲を表示
        List<Integer> entYearSet = new ArrayList<>();

        // 例：現在から過去15年、未来5年までを表示（年度範囲を広げる）
        for (int i = currentYear + 5; i >= currentYear - 15; i--) {
            entYearSet.add(i);
        }

        if (isAttendStr != null) {
            isAttend = true;
            req.setAttribute("f3", isAttendStr);
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
                return a.compareTo(b);
            }
        });

        if (entYear != 0 && classNum != null && !classNum.equals("0")){
            students = sDao.filter(teacher.getSchool(), entYear, classNum, isAttend);
        } else if (entYear != 0 && (classNum == null || classNum.equals("0"))) {
            students = sDao.filter(teacher.getSchool(), entYear, isAttend);
        } else if (entYear == 0 && (classNum == null || classNum.equals("0"))) {
            students = sDao.filter(teacher.getSchool(), isAttend);
        } else {
            errors.put("f1", "クラスを指定する場合は入学年度も指定してください");
            req.setAttribute("errors", errors);
            students = sDao.filter(teacher.getSchool(), isAttend);
        }

        req.setAttribute("f1", entYear);
        req.setAttribute("f2", classNum);
        req.setAttribute("students", students != null ? students : new ArrayList<>());
        req.setAttribute("class_num_set", list);
        req.setAttribute("ent_year_set", entYearSet);
        req.setAttribute("errors", errors);

        req.getRequestDispatcher("student_list.jsp").forward(req, res);
    }
}