package scoremanager.main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Subject;
import bean.Teacher;
import bean.TestListSubject;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestListSubjectDao;
import tool.Action;

public class TestListSubjectExecuteAction extends Action {
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		//メソッドとスタブ
		HttpSession session = req.getSession();
		Teacher teacher = (Teacher)session.getAttribute("user");
		LocalDate todaysDate = LocalDate.now();
		int year = todaysDate.getYear();

		//使用するDAOを定義
		TestListSubjectDao tesSubDao =new TestListSubjectDao();
		SubjectDao subDao =new SubjectDao();
		ClassNumDao cNumDao =new ClassNumDao();


		// JSPから送られてくるデータを定義
		int entYear = 0 ;
		String classNum = null ;
		String subjectCd = null ;

		// JSPから送られてきたデータを取得
		entYear =Integer.parseInt(req.getParameter("f1"));
		classNum =req.getParameter("f2");
		subjectCd =req.getParameter("f3");

		String error1 = "点数を入力してください";
        String error2 = "0～100の範囲で入力してください";

        int errorCount = 0 ;

		if (entYear==0 || classNum.equals("0") || subjectCd.equals("0")){
            req.setAttribute("error1",error1);
            errorCount +=1 ;
		}


		if (errorCount >= 1){
			String url ="TestList.action";
			req.getRequestDispatcher(url).forward(req, res);
		} else{
			Subject subject =subDao.get(subjectCd, teacher.getSchool());

			List<TestListSubject> testListSubject = tesSubDao.filter(entYear, classNum, subject, teacher.getSchool());

			// 表示用の年度リストを作成
			List<Integer> entYearSet = new ArrayList<>();
			for (int i = year - 10; i < year + 1; i++) {
				entYearSet.add(i);
			}

			// ログインユーザーの学校コードをもとにクラス番号の一覧を取得
			List<String> classList = cNumDao.filter(teacher.getSchool());

			// ログインユーザーの学校コードをもとに科目の一覧を取得
			List<Subject> subList = subDao.filter(teacher.getSchool());


			req.setAttribute("ent_year_set",entYearSet);
			req.setAttribute("class_num_set",classList);
			req.setAttribute("subject_set", subList);
			req.setAttribute("test_list",testListSubject);
			req.setAttribute("subjectCd",subjectCd);


			// フォワード
		    req.getRequestDispatcher("test_list_subject.jsp").forward(req, res);
		}
	}

}
