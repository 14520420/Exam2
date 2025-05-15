package bean;

import java.io.Serializable;

public class Teacher extends User implements Serializable {
    private String id;        // 教員ID
    private String password;  // パスワード
    private String name;      // 教員名
    private School school;    // 所属校
    private boolean isAdmin;  // 管理者フラグ

    // ゲッター・セッター
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}