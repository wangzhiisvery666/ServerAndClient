import Base.Message;
import Base.MessageType;
import Base.User;
import Utils.LogInfo;
import Utils.Utility;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * server main thread
 */
public class Server implements Runnable {
    //Configuration class for storing users and passwords
    private static Properties properties = new Properties();
    //When more than 4 times, block all users for 10 seconds
    public static int loginFrequency = 4;
    public static int ServerPort;

    //Static code blocks load configuration files
    static {
        try {
            properties.load(new FileReader("src\\resource\\credentials.properties"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Authenticate user method
     *
     * @param UserId   ：id entered by the user
     * @param Password ：User entered password
     * @return ：Return if the identity is correct
     */
    public static boolean judge(String UserId, String Password) {
        //One less chance per called
        loginFrequency--;
        String correctPassword = properties.getProperty(UserId);
        if (correctPassword == null) {
            return false;
        } else {
            return correctPassword.equals(Password);
        }
    }

    /**
     * The main method is used to implement
     *
     */
    public static void main(String[] args) {
        loginFrequency=Integer.parseInt(args[1]);
        ServerPort=Integer.parseInt(args[0]);
        //Start the message push thread
        new Thread(new Server()).start();
        ServerSocket serverSocket = null;
        System.out.println("The server is listening");
        try {
            serverSocket = new ServerSocket(ServerPort);
            while (true) {

                //If you can't read the information, it will be blocked here until a client connects to the port
                Socket socket = serverSocket.accept();
                InetAddress inetAddress = socket.getInetAddress();

                //Read a user object with an object stream
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                User user = (User) objectInputStream.readObject();


                //to get the date and time
                Date date = new Date();
                String SendTime = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss").format(date);

                //output stream
                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);

                Message message = new Message();
                //Returned when the remaining number of logins is less than or equal to 0    LOGIN_FAIL_REPEATEDLY
                if (loginFrequency <= 0) {
                    message.setMessageType(MessageType.MESSAGE_LOGIN_FAIL_REPEATEDLY);
                    objectOutputStream.writeObject(message);
                    //sleep for 10 seconds
                    Thread.sleep(10000);
                    loginFrequency = 4;
                }
                if (judge(user.getUserId(), user.getPassword())) {
                    //If the login is successful, the remaining number of logins will be restored.
                    loginFrequency = 4;
                    //save log
                    LogInfo.saveLoginLog(user.getUserId(), user.getIp(), user.getPort(),user.getData());
                    System.out.println(user.getUserId() + " : Successful login channel has been established");

                    //If successful, send the success message back
                    message.setMessageType(MessageType.MESSAGE_LOGIN_SUSSED);
                    objectOutputStream.writeObject(message);
                    //Successfully create a thread to maintain communication
                    ServerConnectThread serverConnectThread = new ServerConnectThread(socket, user.getUserId());
                    serverConnectThread.start();

                    //and load it into a collection for easy management
                    ManageServerConnectClientThread.addManageServerConnectClientThread(serverConnectThread, user.getUserId());
                    user.setPassword("");
                    //add users
                    ManageServerConnectClientThread.users.add(user);

                } else {
                    System.out.println("failed to login~");
                    //If the login fails, a failure message is returned directly
                    message.setMessageType(MessageType.MESSAGE_LOGIN_FAIL);
                    message.setContent("you fail again" + loginFrequency + "will wait 10 seconds after");
                    objectOutputStream.writeObject(message);

                    socket.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            //If you exit the while, it means that the program exits and finally closes the resource
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("Please enter the message you want to push");
            Message message = new Message();

            String content = Utility.readString(10);
            message.setMessageType(MessageType.MESSAGE_ROOM);
            message.setContent(content);
            message.setSender("[system]");
            message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            Set<String> keySet = ManageServerConnectClientThread.getKeySet();
            Map<String, ServerConnectThread> map = ManageServerConnectClientThread.getMap();

            for (String user : keySet) {
                ServerConnectThread serverConnectThread = map.get(user);
                try {
                    OutputStream outputStream = serverConnectThread.getSocket().getOutputStream();
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                    objectOutputStream.writeObject(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
