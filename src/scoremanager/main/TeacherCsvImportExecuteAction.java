package scoremanager.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import bean.School;
import bean.Teacher;
import dao.SchoolDao;
import dao.TeacherDao;
import tool.Action;

public class TeacherCsvImportExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
        // セッションからログイン教師情報を取得
        HttpSession session = req.getSession();
        Teacher loginTeacher = (Teacher) session.getAttribute("user");

        // 未ログインの場合はログイン画面へリダイレクト
        if (loginTeacher == null || !loginTeacher.isAuthenticated()) {
            res.sendRedirect("../Login.action");
            return;
        }

        // 管理者権限のチェック
        if (!loginTeacher.isAdmin()) {
            req.setAttribute("error", "この機能を使用する権限がありません");
            req.getRequestDispatcher("error.jsp").forward(req, res);
            return;
        }

        try {
            // CSVファイルのアップロード処理
            Part filePart = req.getPart("csvFile");
            if (filePart == null || filePart.getSize() == 0) {
                req.setAttribute("error", "CSVファイルが選択されていません");
                req.getRequestDispatcher("teacher_csv_import.jsp").forward(req, res);
                return;
            }

            // DAOの準備
            TeacherDao teacherDao = new TeacherDao();
            SchoolDao schoolDao = new SchoolDao();

            // 学校のマップを準備（コードをキーに）
            Map<String, School> schoolMap = new HashMap<>();
            List<School> schools = schoolDao.getAllSchools();
            for (School school : schools) {
                schoolMap.put(school.getCd(), school);
            }

            // 既存教員を更新するかどうか
            boolean updateExisting = "1".equals(req.getParameter("updateExisting"));

            // 結果カウンター
            int newCount = 0;
            int updateCount = 0;
            int errorCount = 0;
            List<String> errors = new ArrayList<>();

            // CSVファイルの読み込み
            try (BufferedReader br = new BufferedReader(
                     new InputStreamReader(filePart.getInputStream(), StandardCharsets.UTF_8))) {

                String line;
                int lineNo = 0;

                while ((line = br.readLine()) != null) {
                    lineNo++;
                    if (line.trim().isEmpty()) {
                        continue; // 空行はスキップ
                    }

                    // CSVの1行をカンマで分割
                    String[] values = line.split(",");

                    // 項目数チェック
                    if (values.length < 5) {
                        errors.add("行 " + lineNo + ": 項目数が不足しています");
                        errorCount++;
                        continue;
                    }

                    String id = values[0].trim();
                    String password = values[1].trim();
                    String name = values[2].trim();
                    String schoolCd = values[3].trim();
                    String adminFlag = values[4].trim();

                    // 入力チェック
                    boolean hasError = false;
                    if (id.isEmpty()) {
                        errors.add("行 " + lineNo + ": 教員IDが空です");
                        hasError = true;
                    }
                    if (password.isEmpty()) {
                        errors.add("行 " + lineNo + ": パスワードが空です");
                        hasError = true;
                    }
                    if (name.isEmpty()) {
                        errors.add("行 " + lineNo + ": 氏名が空です");
                        hasError = true;
                    }
                    if (schoolCd.isEmpty() || !schoolMap.containsKey(schoolCd)) {
                        errors.add("行 " + lineNo + ": 学校コード " + schoolCd + " は無効です");
                        hasError = true;
                    }
                    if (!adminFlag.equals("0") && !adminFlag.equals("1")) {
                        errors.add("行 " + lineNo + ": 管理者フラグは 0 または 1 で指定してください");
                        hasError = true;
                    }

                    if (hasError) {
                        errorCount++;
                        continue;
                    }

                    // 既存教員のチェック
                    Teacher existingTeacher = teacherDao.get(id);
                    if (existingTeacher != null && !updateExisting) {
                        errors.add("行 " + lineNo + ": 教員ID " + id + " は既に登録されています");
                        errorCount++;
                        continue;
                    }

                    // 教員情報のセット
                    Teacher teacher = new Teacher();
                    teacher.setId(id);
                    teacher.setPassword(password);
                    teacher.setName(name);
                    teacher.setSchool(schoolMap.get(schoolCd));
                    teacher.setAdmin(adminFlag.equals("1"));

                    // 保存処理
                    boolean result = teacherDao.save(teacher);
                    if (result) {
                        if (existingTeacher != null) {
                            updateCount++;
                        } else {
                            newCount++;
                        }
                    } else {
                        errors.add("行 " + lineNo + ": 保存処理に失敗しました");
                        errorCount++;
                    }
                }
            }

            // 結果を設定
            req.setAttribute("newCount", newCount);
            req.setAttribute("updateCount", updateCount);
            req.setAttribute("errorCount", errorCount);
            req.setAttribute("errors", errors);

            // 完了画面へ
            req.getRequestDispatcher("teacher_csv_import_done.jsp").forward(req, res);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "CSVインポート処理中にエラーが発生しました: " + e.getMessage());
            req.getRequestDispatcher("teacher_csv_import.jsp").forward(req, res);
        }
    }
}