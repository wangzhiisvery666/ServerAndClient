import Base.Message;
import Base.MessageType;
import Base.UdpPort;
import Base.User;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.List;

/**
 * Client service thread, the thread is always open, monitoring information
 */
public class ClientServiceThread extends Thread {

    public static StringBuffer onlineUser;
    private Socket socket;
    Message message;

    @Override

    /**
     * Main Thread
     */
    public void run() {
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (true) {

            try {
                ObjectInputStream obs = new ObjectInputStream(socket.getInputStream());

                //If no content is found it is blocked here
                message = (Message) obs.readObject();
                //     MESSAGE_Online_User
                if (message.getMessageType().equals(MessageType.MESSAGE_Online_User)) {
                    onlineUser = new StringBuffer();
                    String content = message.getContent();
                    if (content != null) {
                        String[] split = content.split(",");
                        for (String s : split) {
                            onlineUser.append(s);
                        }
                    }
                    //     MESSAGE_ONLINE_USER_LIST
                } else if (message.getMessageType().equals(MessageType.MESSAGE_ONLINE_USER_LIST)) {
                    List<User> userList = message.getUserList();
                    for (User user: userList) {
                        System.out.println("username: "+user.getUserId()+" port: "+user.getPort()+" ip: "+user.getIp()+" active time: "+user.getData());
                    }
                    //     MESSAGE_ROOM
                } else if (message.getMessageType().equals(MessageType.MESSAGE_ROOM)) {
                    String mes = message.getSendTime() + "\t" + message.getSender() + " : \t" + message.getContent();
                    StoreMessage.RSRMessage.add(mes);
                    //     MESSAGE_GROUP
                } else if (message.getMessageType().equals(MessageType.MESSAGE_GROUP)) {
                    String mes = message.getGetter() + "\t" + message.getSendTime() + "\n" + message.getSender() + " : \t" + message.getContent();
                    StoreMessage.BCMMessage.add(mes);
                    //     MESSAGE_FILE
                } else if (message.getMessageType().equals(MessageType.MESSAGE_FILE)) {
                    String path = "D:\\" + message.getContent();
                    FileOutputStream fileOutputStream = new FileOutputStream(path);
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                    bufferedOutputStream.write(message.getvideoFile());
                    bufferedOutputStream.close();
                    System.out.println(message.getSendTime() + "\n" + "[" + message.getContent() + "]" + "In the path" + path);
                }else if(message.getMessageType().equals(MessageType.MESSAGE_IpAndPort)){
                    String Port = message.getContent();
                    UdpPort.sender_Port=Integer.parseInt(Port);
                } else if(message.getMessageType().equals(MessageType.MESSAGE_UDP_FILE_NAME)){

                    String[] split = message.getContent().split("/");
                    UdpPort.UDP_FILENAME= split[split.length-1];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Message getMessage() {
        return message;
    }

    public ClientServiceThread(Socket socket) {
        this.socket = socket;
    }

    public ClientServiceThread() {
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
