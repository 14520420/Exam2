// クラス定義：このクラスはデータ構造やロジックの単位として使用されます
package bean;

public class ClassNum {
	// class_num：String型のフィールド
	private String class_num;
	// school：School型のフィールド
	private School school;

	// getClass_numメソッド：値の取得を行う
	public String getClass_num() {
		return class_num;
	}

	// setClass_numメソッド：値の設定を行う
	public void setClass_num(String class_num) {
		this.class_num = class_num;
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
