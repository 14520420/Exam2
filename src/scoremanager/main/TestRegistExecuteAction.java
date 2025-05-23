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

public class TestRegistExecuteAction extends Action {

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

        // リクエストパラメータの取得
        String entYear = req.getParameter("ent_year");
        String classNum = req.getParameter("class_num");
        String subjectCd = req.getParameter("subject_cd");
        String no = req.getParameter("no");
        String[] studentNos = req.getParameterValues("student_no");
        String[] points = req.getParameterValues("point");

        try {
            // 基本的なバリデーション
            if (entYear == null || classNum == null || subjectCd == null || no == null ||
                studentNos == null || points == null || studentNos.length != points.length) {
                throw new Exception("必要なパラメータが不足しています");
            }

            int entYearInt = Integer.parseInt(entYear);
            int noInt = Integer.parseInt(no);

            // DAO の初期化
            SubjectDao subjectDao = new SubjectDao();
            StudentDao studentDao = new StudentDao();
            TestDao testDao = new TestDao();

            // 科目情報を取得
            Subject subject = subjectDao.get(subjectCd, school);
            if (subject == null) {
                throw new Exception("指定された科目が見つかりません");
            }

            // バリデーション
            Map<String, String> pointErrors = new HashMap<>();
            boolean hasErrors = false;

            List<Test> testList = new ArrayList<>();
            for (int i = 0; i < studentNos.length; i++) {
                String studentNo = studentNos[i];
                String pointStr = points[i];

                // 点数の検証
                if (pointStr == null || pointStr.trim().isEmpty()) {
                    pointErrors.put(studentNo, "点数を入力してください");
                    hasErrors = true;
                    continue;
                }

                try {
                    int point = Integer.parseInt(pointStr.trim());
                    if (point < 0 || point > 100) {
                        pointErrors.put(studentNo, "0～100の範囲で入力してください");
                        hasErrors = true;
                        continue;
                    }

                    // 学生情報を取得
                    Student student = studentDao.get(studentNo);
                    if (student != null) {
                        // テストデータを作成
                        Test test = new Test();
                        test.setStudent(student);
                        test.setSubject(subject);
                        test.setSchool(school);
                        test.setNo(noInt);
                        test.setPoint(point);
                        test.setClassNum(classNum);
                        testList.add(test);
                    }
                } catch (NumberFormatException e) {
                    pointErrors.put(studentNo, "0～100の範囲で入力してください");
                    hasErrors = true;
                }
            }

            // エラーがある場合は元の画面に戻る
            if (hasErrors) {
                // 必要なデータを再取得して画面に戻る
                ClassNumDao classNumDao = new ClassNumDao();

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

                // 回数リスト
                List<Integer> noList = new ArrayList<>();
                for (int i = 1; i <= 10; i++) {
                    noList.add(i);
                }

                // 学生一覧を再取得
                List<Student> studentList = studentDao.filter(school, entYearInt, classNum, true);

                // 入力された値を保持（範囲外でも保持）
                Map<String, String> existingPointStrings = new HashMap<>();
                for (int i = 0; i < studentNos.length; i++) {
                    if (points[i] != null && !points[i].trim().isEmpty()) {
                        existingPointStrings.put(studentNos[i], points[i].trim());
                    }
                }

                // 画面に必要なデータを設定
                req.setAttribute("entYearList", entYearList);
                req.setAttribute("classNumList", classNumList);
                req.setAttribute("subjectList", subjectList);
                req.setAttribute("noList", noList);
                req.setAttribute("studentList", studentList);
                req.setAttribute("existingPoints", existingPointStrings);
                req.setAttribute("pointErrors", pointErrors);
                req.setAttribute("selectedEntYear", entYear);
                req.setAttribute("selectedClassNum", classNum);
                req.setAttribute("selectedSubjectCd", subjectCd);
                req.setAttribute("selectedNo", no);
                req.setAttribute("selectedSubjectName", subject.getName());

                req.getRequestDispatcher("test_regist.jsp").forward(req, res);
                return;
            }

            // 成績を保存
            boolean success = testDao.save(testList);

            if (success) {
                // 成功した場合は完了画面へ
                req.getRequestDispatcher("test_regist_done.jsp").forward(req, res);
            } else {
                throw new Exception("成績の登録に失敗しました");
            }

        } catch (Exception e) {
            // エラー情報をセット
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("TestRegist.action").forward(req, res);
        }
    }
}