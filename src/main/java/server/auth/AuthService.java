package server.auth;

import java.util.List;
import java.util.Optional;

public class AuthService {

    private static final List<Users> users;

    static {
        users = List.of(
                new Users("user1", "1111", "src/main/java/server/fileServer/user1/", "src/main/java/client/test/"),
                new Users("user2", "2222", "src/main/java/server/fileServer/user2/", "src/main/java/client/test/"),
                new Users("user3", "3333", "src/main/java/server/fileServer/user3/", "src/main/java/client/test/")
        );
    }

    public Optional<Users> doAuth(String login, String password) {
        for (Users user : users) {
            if (user.getNickname().equals(login) && user.getPassword().equals(password)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }
}
