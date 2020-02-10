package chat.server;

import java.util.List;

public interface AuthManager {
    String getNicknameByLoginAndPassword(String login, String password);
    List<String> getNicknameList();
    boolean replaceNickname(String oldNickname, String newNickname);
}
