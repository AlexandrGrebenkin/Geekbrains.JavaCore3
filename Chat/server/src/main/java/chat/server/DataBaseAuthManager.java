package chat.server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataBaseAuthManager implements AuthManager {
    private Connection connection;
    private Statement stmt;

    public DataBaseAuthManager() {

    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            connect();
            try(PreparedStatement ps = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ?")){
                ps.setString(1, login);
                ps.setString(2, password);
                return ps.executeQuery().getString("nickname");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return null;
    }

    @Override
    public List<String> getNicknameList() {
        List<String> nicknameList = new ArrayList<>();
        try {
            connect();
            try(ResultSet rs = stmt.executeQuery("SELECT nickname FROM users")){
                while (rs.next()){
                    nicknameList.add(rs.getString("nickname"));
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return nicknameList;
    }

    @Override
    public boolean replaceNickname(String oldNickname, String newNickname) {
        try {
            connect();
            try(PreparedStatement ps = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?")){
                ps.setString(1, newNickname);
                ps.setString(2, oldNickname);
                return ps.executeUpdate() != 0;
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            disconnect();
        }
        return false;
    }

    private void connect() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:chat.db");
        stmt = connection.createStatement();
    }

    private void disconnect(){
        try {
            if(stmt != null) {
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if(connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
