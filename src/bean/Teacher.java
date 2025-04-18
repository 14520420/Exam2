// クラス定義：このクラスは教員情報を管理するためのエンティティです
package bean;

import java.io.Serializable;

public class Teacher extends User implements Serializable {
	/**
	 * 教員ID:String
	 */
	// id：String型のフィールド
	private String id;

	/**
	 * パスワード:String
	 */
	// password：String型のフィールド
	private String password;

	/**
	 * 教員名:String
	 */
	// name：String型のフィールド
	private String name;

	/**
	 * 所属校:School
	 */
	// school：School型のフィールド
	private School school;

	/**
	 * ゲッター・セッター
	 */
	// getIdメソッド：値の取得を行う
	public String getId() {
		return id;
	}

	// setIdメソッド：値の設定を行う
	public void setId(String id) {
		this.id = id;
	}

	// getPasswordメソッド：値の取得を行う
	public String getPassword() {
		return password;
	}

	// setPasswordメソッド：値の設定を行う
	public void setPassword(String password) {
		this.password = password;
	}

	// getNameメソッド：値の取得を行う
	public String getName() {
		return name;
	}

	// setNameメソッド：値の設定を行う
	public void setName(String name) {
		this.name = name;
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
