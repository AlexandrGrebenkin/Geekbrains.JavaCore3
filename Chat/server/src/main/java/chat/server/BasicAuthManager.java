package chat.server;

import java.util.ArrayList;
import java.util.List;

public class BasicAuthManager implements AuthManager{
    private class Entry{
        private String login;
        private String password;
        private String nickname;

        public Entry(String login,String password, String nickname){
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    private List<Entry> users;

    @Override
    public void start() {
        users = new ArrayList<>();
        users.add(new Entry("login1", "pass1", "user1"));
        users.add(new Entry("login2", "pass2", "user2"));
        users.add(new Entry("login3", "pass3", "user3"));
    }

    @Override
    public void stop() {
        users = null;
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (Entry u : users){
            if(u.login.equals(login) && u.password.equals(password)){
                return u.nickname;
            }
        }
        return null;
    }

    @Override
    public boolean isNickBusy(String nickname) {
        for (Entry u : users){
            if(u.getNickname().equals(nickname)){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean replaceNickname(String oldNickname, String newNickname) {
        for (Entry u : users){
            if(u.getNickname().equals(oldNickname)){
                u.setNickname(newNickname);
                return true;
            }
        }
        return false;
    }
}
