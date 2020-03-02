package chat.server;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DataBaseAuthManager implements AuthManager {
    private static final Logger LOGGER = LogManager.getLogger(DataBaseAuthManager.class);

    private Connection connection;
    private Statement stmt;
    private PreparedStatement psGetNicknameByLoginAndPassword;
    private PreparedStatement psReplaceNickname;
    private PreparedStatement psIsNickBusy;

    @Override
    public void start() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:chat.db");
            stmt = connection.createStatement();
            psGetNicknameByLoginAndPassword = connection.prepareStatement("SELECT nickname FROM users WHERE login = ? AND password = ?");
            psReplaceNickname = connection.prepareStatement("UPDATE users SET nickname = ? WHERE nickname = ?");
            psIsNickBusy = connection.prepareStatement("SELECT nickname FROM users WHERE nickname = ?");
        } catch (ClassNotFoundException | SQLException e){
            LOGGER.throwing(Level.ERROR, e);
            throw new RuntimeException("Unable to connect to DB");
        }
    }

    @Override
    public void stop() {
        tryClose(psIsNickBusy);
        tryClose(psReplaceNickname);
        tryClose(psGetNicknameByLoginAndPassword);
        tryClose(stmt);
        tryClose(connection);
    }

    @Override
    public boolean isNickBusy(String nickname) {
        try{
            psIsNickBusy.setString(1, nickname);
            ResultSet rs = psIsNickBusy.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.throwing(Level.ERROR, e);
        }
        return false;
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        try {
            psGetNicknameByLoginAndPassword.setString(1, login);
            psGetNicknameByLoginAndPassword.setString(2, password);
            return psGetNicknameByLoginAndPassword.executeQuery().getString("nickname");
        } catch (SQLException e) {
            LOGGER.throwing(Level.ERROR, e);
        }
        return null;
    }

    @Override
    public boolean replaceNickname(String oldNickname, String newNickname) {
        try{
            psReplaceNickname.setString(1, newNickname);
            psReplaceNickname.setString(2, oldNickname);
            return psReplaceNickname.executeUpdate() != 0;
        } catch (SQLException e) {
            LOGGER.throwing(Level.ERROR, e);
        }
        return false;
    }

    private void tryClose(AutoCloseable closeable){
        if (closeable != null){
            try {
                closeable.close();
            } catch (Exception e) {
                LOGGER.throwing(Level.ERROR, e);
            }
        }
    }


}
