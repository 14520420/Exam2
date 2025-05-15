package bean;

public class User {
    private boolean isAuthenticated; // 認証済みフラグ

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
}