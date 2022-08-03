package Base;

import java.io.Serializable;
import java.util.List;

/**
 * Encapsulates message classes for message delivery
 */
public class Message implements Serializable, MessageType {
    private static final long serialVersionUID = 1L;
    private String MessageType;
    private String getter;
    private String sender;
    private String sendTime;
    private String content;
    private byte[] videoFile;
    private List<User> userList;
    public Message() {
    }

    public byte[] getVideoFile() {
        return videoFile;
    }

    public void setVideoFile(byte[] videoFile) {
        this.videoFile = videoFile;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public Message(String messageType, String getter, String sender, String sendTime, String content) {
        MessageType = messageType;
        this.getter = getter;
        this.sender = sender;
        this.sendTime = sendTime;
        this.content = content;
    }

    public byte[] getvideoFile() {
        return videoFile;
    }

    public void setvideoFile(byte[] videoFile) {
        this.videoFile = videoFile;
    }

    public String getMessageType() {
        return MessageType;
    }

    public void setMessageType(String messageType) {
        MessageType = messageType;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
