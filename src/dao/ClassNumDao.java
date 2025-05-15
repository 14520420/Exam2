// ClassNumDao.java の完全修正版

package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.ClassNum;
import bean.School;

public class ClassNumDao extends Dao {

    /**
     * クラス番号と学校コードでクラス情報を取得するメソッド
     * @param class_num クラス番号
     * @param school 学校情報
     * @return 該当するクラス情報、存在しない場合はnull
     * @throws Exception
     */
    public ClassNum get(String class_num, School school) throws Exception {
        // クラス番号インスタンス初期化
        ClassNum classNum = new ClassNum();
        // データベースコネクション確立
        Connection connection = null;
        // プリペアードステートメントにSQLセット
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            // コネクション取得
            connection = getConnection();

            // DB処理
            statement = connection
                    .prepareStatement("SELECT * FROM class_num WHERE class_num = ? AND school_cd = ?");
            statement.setString(1, class_num);
            statement.setString(2, school.getCd());
            rSet = statement.executeQuery();
            SchoolDao sDao = new SchoolDao();

            if (rSet.next()) {
                // リザルトが存在する場合クラス番号インスタンスに結果をセット
                classNum.setClass_num(rSet.getString("class_num"));
                classNum.setSchool(sDao.get(rSet.getString("school_cd")));
            } else {
                // リザルトが存在しない場合nullセット
                classNum = null;
            }
        } catch (Exception e) {
            throw e;
        } finally {
            // リソース解放（逆順）
            if (rSet != null) {
                try {
                    rSet.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
        }
        return classNum;
    }

    /**
     * 新規登録処理
     * @param classNum 登録するクラス番号
     * @return 成功した場合true、失敗した場合false
     * @throws Exception
     */
    public boolean save(ClassNum classNum) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        int count = 0;

        try {
            connection = getConnection();

            // 既存のクラスを確認
            ClassNum existing = get(classNum.getClass_num(), classNum.getSchool());

            if (existing == null) {
                // 新規登録
                statement = connection.prepareStatement(
                        "INSERT INTO class_num(school_cd, class_num) VALUES(?, ?)");
                statement.setString(1, classNum.getSchool().getCd());
                statement.setString(2, classNum.getClass_num());
            } else {
                // 更新（このケースはほぼないはずだが念のため）
                statement = connection.prepareStatement(
                        "UPDATE class_num SET school_cd = ? WHERE class_num = ?");
                statement.setString(1, classNum.getSchool().getCd());
                statement.setString(2, classNum.getClass_num());
            }

            count = statement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            // リソース解放
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
        }
        return count > 0;
    }

    /**
     * クラス番号更新処理
     * @param classNum 更新するクラス情報（新しいクラス番号と古いクラス番号を含む）
     * @return 成功した場合true、失敗した場合false
     * @throws Exception
     */
    public boolean update(ClassNum classNum) throws Exception {
        Connection connection = null;
        PreparedStatement statement1 = null;
        PreparedStatement statement2 = null;
        int count = 0;

        try {
            // コネクション取得とトランザクション開始
            connection = getConnection();
            connection.setAutoCommit(false); // トランザクション開始

            // 1. クラス番号がnullでないことを確認
            if (classNum.getClass_num() == null || classNum.getSchool() == null || classNum.getSchool().getCd() == null) {
                throw new Exception("クラス番号または学校コードがnullです");
            }

            // 2. 学生テーブルのclass_num更新
            String updateStudentSql = "UPDATE student SET class_num = ? WHERE school_cd = ? AND class_num = ?";
            statement1 = connection.prepareStatement(updateStudentSql);
            statement1.setString(1, classNum.getClass_num());
            statement1.setString(2, classNum.getSchool().getCd());
            statement1.setString(3, classNum.getOld_class_num());

            int studentUpdateCount = statement1.executeUpdate();
            count += studentUpdateCount;

            // 3. クラステーブルのclass_num更新
            String updateClassNumSql = "UPDATE class_num SET class_num = ? WHERE school_cd = ? AND class_num = ?";
            statement2 = connection.prepareStatement(updateClassNumSql);
            statement2.setString(1, classNum.getClass_num());
            statement2.setString(2, classNum.getSchool().getCd());
            statement2.setString(3, classNum.getOld_class_num());

            int classNumUpdateCount = statement2.executeUpdate();
            count += classNumUpdateCount;

            // 成功した場合のみコミット
            if (count >= 1) { // 少なくとも1つの更新があれば成功と見なす
                connection.commit();
                return true;
            } else {
                connection.rollback();
                return false;
            }

        } catch (SQLException e) {
            // SQL例外発生時はロールバック
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    // ロールバック失敗時のエラーは無視
                }
            }
            throw new Exception("データベースエラー: " + e.getMessage(), e);
        } catch (Exception e) {
            // その他の例外発生時もロールバック
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    // ロールバック失敗時のエラーは無視
                }
            }
            throw new Exception("エラー: " + e.getMessage(), e);
        } finally {
            // 資源解放とトランザクション終了
            if (statement1 != null) {
                try {
                    statement1.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
            if (statement2 != null) {
                try {
                    statement2.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
            if (connection != null) {
                try {
                    connection.setAutoCommit(true);  // 自動コミットに戻す
                    connection.close();  // finallyブロックでのみコネクションをクローズ
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
        }
    }

    /**
     * 学校コード一致のクラスコード取得
     * @param school 学校情報
     * @return クラス番号のリスト
     * @throws Exception
     */
    public List<String> filter(School school) throws Exception {
        List<String> list = new ArrayList<>(); // リスト初期化
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            // コネクション取得
            connection = getConnection();

            // DB処理
            statement = connection
                    .prepareStatement("SELECT class_num FROM class_num WHERE school_cd = ? ORDER BY class_num");
            statement.setString(1, school.getCd());
            rSet = statement.executeQuery();

            while (rSet.next()) {
                list.add(rSet.getString("class_num"));
            }

            // クラス番号を数値としてソート (数値でない場合は文字列として扱う)
            list.sort((a, b) -> {
                try {
                    int aNum = Integer.parseInt(a);
                    int bNum = Integer.parseInt(b);
                    return Integer.compare(aNum, bNum);
                } catch (NumberFormatException e) {
                    // 数値変換できない場合は文字列比較
                    return a.compareTo(b);
                }
            });
        } catch (Exception e) {
            throw e;
        } finally {
            // リソース解放（逆順）
            if (rSet != null) {
                try {
                    rSet.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
        }
        return list;
    }

    /**
     * クラス情報リストを取得
     * @param school 学校情報
     * @return クラス情報のリスト
     * @throws Exception
     */
    public List<ClassNum> filterObjects(School school) throws Exception {
        List<ClassNum> list = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            // コネクション取得
            connection = getConnection();

            // DB処理
            statement = connection
                .prepareStatement("SELECT * FROM class_num WHERE school_cd = ? ORDER BY class_num");
            statement.setString(1, school.getCd());
            rSet = statement.executeQuery();

            while (rSet.next()) {
                ClassNum classNum = new ClassNum();
                classNum.setClass_num(rSet.getString("class_num"));
                classNum.setSchool(school);
                list.add(classNum);
            }

            // クラス番号を数値としてソート
            list.sort((a, b) -> {
                try {
                    int aNum = Integer.parseInt(a.getClass_num());
                    int bNum = Integer.parseInt(b.getClass_num());
                    return Integer.compare(aNum, bNum);
                } catch (NumberFormatException e) {
                    // 数値変換できない場合は文字列比較
                    return a.getClass_num().compareTo(b.getClass_num());
                }
            });
        } catch (Exception e) {
            throw e;
        } finally {
            // リソース解放（逆順）
            if (rSet != null) {
                try {
                    rSet.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
        }
        return list;
    }

    /**
     * クラス情報の削除
     * @param classNum 削除するクラス情報
     * @return 成功した場合true、失敗した場合false
     * @throws Exception
     */
    public boolean delete(ClassNum classNum) throws Exception {
        Connection connection = null;
        PreparedStatement statement = null;
        int count = 0;

        try {
            // コネクション取得
            connection = getConnection();

            // DB処理
            statement = connection.prepareStatement(
                    "DELETE FROM class_num WHERE school_cd = ? AND class_num = ?");
            statement.setString(1, classNum.getSchool().getCd());
            statement.setString(2, classNum.getClass_num());
            count = statement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            // リソース解放
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException sqle) {
                    // クローズエラーは無視
                }
            }
        }
        return count > 0;
    }
}