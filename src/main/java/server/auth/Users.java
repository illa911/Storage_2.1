package server.auth;

public class Users {

    private String nickname;
    private String password;
    private String serverStorage;
    private String localStorage;

    public Users(String nickname, String password, String serverStorage, String localStorage) {
        this.nickname = nickname;
        this.password = password;
        this.serverStorage = serverStorage;
        this.localStorage = localStorage;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public String getServerStorage() {
        return serverStorage;
    }

    public String getLocalStorage() {
        return localStorage;
    }
}
