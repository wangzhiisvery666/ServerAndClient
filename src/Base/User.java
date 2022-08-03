package Base;

import java.io.Serializable;
import java.util.Date;

/**
 * The entity class corresponding to the user
 */
public class User implements Serializable {
    private String UserId;
    private String Password;
    private String ip;
    private int port;
    private Date data;
    private static final long serialVersionUID = 1L;

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public User(String userId, String password) {
        UserId = userId;
        Password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
