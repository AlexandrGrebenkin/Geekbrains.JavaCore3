package chat.server;

import java.util.List;

public interface AuthManager {
    void start();
    void stop();
    String getNicknameByLoginAndPassword(String login, String password);
    boolean isNickBusy(String nickname);
    boolean replaceNickname(String oldNickname, String newNickname);
}
