import Base.Message;
import Base.MessageType;
import Base.User;
import Utils.LogInfo;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Set;

/**
 * The server thread that implements the method, the thread corresponding to the client function
 */
public class ServerConnectThread extends Thread {
    private Socket socket;
    private String UserId;

    public ServerConnectThread(Socket socket, String UserId) {
        this.socket = socket;
        this.UserId = UserId;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {

        while (true) {
            try {

                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) objectInputStream.readObject();
                //MESSAGE_Online_User
                if (message.getMessageType().equals(MessageType.MESSAGE_Online_User)) {
                    Message mm = new Message();
                    String onLineUser = ManageServerConnectClientThread.getOnLineUser();
                    mm.setContent(onLineUser);
                    mm.setMessageType(MessageType.MESSAGE_Online_User);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(mm);
                //MESSAGE_ONLINE_USER_LIST
                } else if (message.getMessageType().equals(MessageType.MESSAGE_ONLINE_USER_LIST)) {
                    Message mm = new Message();
                    ArrayList<User> users = ManageServerConnectClientThread.users;
                    mm.setUserList(users);
                    mm.setMessageType(MessageType.MESSAGE_ONLINE_USER_LIST);
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject(mm);

                //  MESSAGE_CLIENT_EXIT
                } else if (message.getMessageType().equals(MessageType.MESSAGE_CLIENT_EXIT)) {
                    System.out.println(message.getSender() + "To log out of the system");
                    //close the thread
                    socket.close();
                    //and remove the thread from the collection
                    ManageServerConnectClientThread.getMap().remove(message.getSender());
                    System.out.println(message.getSender() + "has exited");
                    //exit the loop
                    break;
                //  MESSAGE_ROOM
                } else if (message.getMessageType().equals(MessageType.MESSAGE_ROOM)) {
                    ServerConnectThread serverConnectThread = ManageServerConnectClientThread.getMap().get(message.getGetter());
                    LogInfo.saveSRIdMessageLog(message.getSender(), message.getGetter(), message.getSender() + " : " + message.getContent());
                    //Then the user is online, if not, wait for him to send it online
                    if (serverConnectThread != null) {
                        OutputStream outputStream = serverConnectThread.socket.getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                        oos.writeObject(message);
                    }
                //    MESSAGE_GROUP
                } else if (message.getMessageType().equals(MessageType.MESSAGE_GROUP)) {
                    //record broadcast message log
                    LogInfo.saveMessageLog(message.getSender(), message.getContent());

                    Set<String> strings = ManageServerConnectClientThread.getKeySet();
                    for (String user : strings) {
                        if (message.getSender().equals(user)) {
                            continue;
                        }
                        ServerConnectThread serverConnectThread = ManageServerConnectClientThread.getMap().get(user);
                        OutputStream outputStream = serverConnectThread.socket.getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                        oos.writeObject(message);
                    }
                //     MESSAGE_FILE
                } else if (message.getMessageType().equals(MessageType.MESSAGE_FILE)) {

                    ServerConnectThread serverConnectThread = ManageServerConnectClientThread.getMap().get(message.getGetter());
                    //Then the user is online, if not, wait for him to send it online
                    if (serverConnectThread != null) {
                        OutputStream outputStream = serverConnectThread.socket.getOutputStream();
                        ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                        oos.writeObject(message);

                        FileOutputStream fileOutputStream = new FileOutputStream("src\\" + message.getContent());
                        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                        bufferedOutputStream.write(message.getvideoFile());
                        bufferedOutputStream.close();
                    }
                 // MESSAGE_IpAndPort
                }else if (message.getMessageType().equals(MessageType.MESSAGE_IpAndPort)){
                    //
                    ServerConnectThread serverConnectThread = ManageServerConnectClientThread.getMap().get(message.getGetter());
                    OutputStream outputStream = serverConnectThread.socket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    oos.writeObject(message);

                }
                else if (message.getMessageType().equals(MessageType.MESSAGE_UDP_FILE_NAME)){
                    ServerConnectThread serverConnectThread = ManageServerConnectClientThread.getMap().get(message.getGetter());
                    OutputStream outputStream = serverConnectThread.socket.getOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                    oos.writeObject(message);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
