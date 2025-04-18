//テスト
// クラス定義：このクラスはDAOの基底クラスであり、DB接続を管理します
package dao;

import java.sql.Connection;

import javax.naming.InitialContext;
import javax.sql.DataSource;

public class Dao {
	/**
	 * データソース:DataSource:クラスフィールド
	 */
	// ds：DataSource型の静的フィールド
	static DataSource ds;

	/**
	 * getConnectionメソッド データベースへのコネクションを返す
	 *
	 * @return データベースへのコネクション:Connection
	 * @throws Exception
	 */
	// getConnectionメソッド：DB接続を取得する
	public Connection getConnection() throws Exception {
		// データソースがnullの場合
		if (ds == null) {
			// InitialContextを初期化
			InitialContext ic = new InitialContext();
			// データベースへ接続
			ds = (DataSource) ic.lookup("java:/comp/env/jdbc/exam");
		}
		// データベースへのコネクションを返却
		return ds.getConnection();
	}
}
