// クラス定義：このクラスはデータ構造やロジックの単位として使用されます
package bean;

import java.io.Serializable;

public class School implements Serializable {

	/**
	 * 学校コード:String
	 */
	// cd：String型のフィールド
	private String cd;

	/**
	 * 学校名:String
	 */
	// name：String型のフィールド
	private String name;

	/**
	 * ゲッター・セッター
	 */
	// getCdメソッド：値の取得を行う
	public String getCd() {
		return cd;
	}

	// setCdメソッド：値の設定を行う
	public void setCd(String cd) {
		this.cd = cd;
	}

	// getNameメソッド：値の取得を行う
	public String getName() {
		return name;
	}

	// setNameメソッド：値の設定を行う
	public void setName(String name) {
		this.name = name;
	}
}
