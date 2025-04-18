// クラス定義：このクラスはユーザーの認証状態を管理します
package bean;

public class User {
	/**
	 * 認証済みフラグ:boolean true:認証済み
	 */
	// isAuthenticated：boolean型のフィールド
	private boolean isAuthenticated;

	/**
	 * ゲッター、セッター
	 */
	// isAuthenticatedメソッド：値の取得を行う
	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	// setAuthenticatedメソッド：値の設定を行う
	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
	}
}
