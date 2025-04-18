// クラス定義：このクラスはデータ構造やロジックの単位として使用されます
package bean;

public class Student {
	// no：String型のフィールド
	private String no;
	// name：String型のフィールド
	private String name;
	// entYear：int型のフィールド
	private int entYear;
	// classNum：String型のフィールド
	private String classNum;
	// isAttend：boolean型のフィールド
	private boolean isAttend;
	// school：School型のフィールド
	private School school;

	// getNoメソッド：値の取得を行う
	public String getNo() {
		return no;
	}

	// setNoメソッド：値の設定を行う
	public void setNo(String no) {
		this.no = no;
	}

	// getNameメソッド：値の取得を行う
	public String getName() {
		return name;
	}

	// setNameメソッド：値の設定を行う
	public void setName(String name) {
		this.name = name;
	}

	// getEntYearメソッド：値の取得を行う
	public int getEntYear() {
		return entYear;
	}

	// setEntYearメソッド：値の設定を行う
	public void setEntYear(int entYear) {
		this.entYear = entYear;
	}

	// getClassNumメソッド：値の取得を行う
	public String getClassNum() {
		return classNum;
	}

	// setClassNumメソッド：値の設定を行う
	public void setClassNum(String classNum) {
		this.classNum = classNum;
	}

	// isAttendメソッド：値の取得を行う
	public boolean isAttend() {
		return isAttend;
	}

	// setAttendメソッド：値の設定を行う
	public void setAttend(boolean isAttend) {
		this.isAttend = isAttend;
	}

	// getSchoolメソッド：値の取得を行う
	public School getSchool() {
		return school;
	}

	// setSchoolメソッド：値の設定を行う
	public void setSchool(School school) {
		this.school = school;
	}
}
