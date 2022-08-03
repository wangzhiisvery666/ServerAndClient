import Base.Message;
import Base.MessageType;
import Base.User;
import Utils.StreamUtils;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static Base.UdpPort.client_udp_Port;


/**
 * A class that provides methods for implementing functions
 */
public class ClientService {
    private User u;
    public static int port;
    public static String ip;

    /**
     * The method used to authenticate the user
     *
     * @param user     ：username
     * @param password ： password
     * @return ：Returns whether the login was successful
     */
    public boolean getLoginMessage(String user, String password) {
        boolean loop = false;
        Socket socket = null;
        try {
            //create an object
            u = new User(user, password);
            u.setData(new Date());
            u.setIp(ip);
            u.setPort(client_udp_Port);
            socket = new Socket(ip,port);
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            //Pass the object to the server
            objectOutputStream.writeObject(u);
            //Read the message object from the server
            InputStream inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Message message = (Message) objectInputStream.readObject();
            if (message.getMessageType().equals(Message.MESSAGE_LOGIN_FAIL_REPEATEDLY)) {
                System.out.println("Too many consecutive failures, please log in again after 10 seconds");
                Thread.sleep(10000);

            } else
                //If the login is successful
                if (message.getMessageType().equals(Message.MESSAGE_LOGIN_SUSSED)) {
                    //To start a thread that maintains the socket all the time, it is always started to maintain communication
                    ClientServiceThread cCST = new ClientServiceThread(socket);
                    cCST.start();
                    ManageClientThread.addConnectServerThread(user, cCST);
                    loop = true;
                    //If the login is not successful, close the created socket
                } else {
                    System.out.println(message.getContent());
                    socket.close();
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loop;
    }

    /**
     * How to get online users
     */
    public void getOnLineUserList() {
        //Edit message type
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_ONLINE_USER_LIST);
        message.setSender(u.getUserId());
        //Send a message
        sendMessage(message);
    }

    /**
     * Exit client method
     */
    public void closeSeverSocket() {
        //Edit message information
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(u.getUserId());
        //Send a message
        sendMessage(message);
        System.out.println(u.getUserId() + "   Retreat safely");
        //Retreat safely
        System.exit(0);

    }

    /**
     * Open room chat method
     *
     * @param content ：Message content
     * @param person  ：person receiving the message
     */
    public void privateChat(String content, String person) {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_ROOM);
        message.setContent(content);
        message.setSender(u.getUserId());
        message.setGetter(person);
        message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        sendMessage(message);
    }

    /**
     * public message
     * @param content ：content
     */
    public void commonChat(String content) {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_GROUP);
        //content
        message.setContent(content);
        //sender
        message.setSender(u.getUserId());
        //Receiver
        message.setGetter("BCM");
        //time
        message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //Send a message
        sendMessage(message);
    }

    /**
     * Send file method
     * @param fileName ：fileName
     * @param person   ：person
     */
    public void sendFile(String fileName, String person) {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_FILE);
        message.setContent(fileName);
        message.setSender(u.getUserId());
        message.setGetter(person);
        message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        try {
            FileInputStream fileInputStream = new FileInputStream("src\\" + fileName);
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            byte[] data = StreamUtils.streamToByteArray(bufferedInputStream);
            message.setvideoFile(data);
            //Send a message
            sendMessage(message);

            System.out.println("Sent successfully");
            fileInputStream.close();
        } catch (Exception e) {
            System.out.println("file not found");
        }
    }

    /**
     * method of sending a message
     *
     * @param message : message
     */
    public void sendMessage(Message message) {
        try {
            //Get the user's socket by username
            ClientServiceThread thread = ManageClientThread.getMap(u.getUserId());
            //The channel is already connected, use a specific socket to communicate with the socket connected to the server
            Socket socket = thread.getSocket();
            OutputStream outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Query message list
     *
     * @param key ：Select the type of message to view
     */
    public void queryMES(String key) {
        ArrayList<String> rsrMessage = StoreMessage.RSRMessage;
        ArrayList<String> bcmMessage = StoreMessage.BCMMessage;
        ArrayList<String> temp = null;
        if (key.equals("b")) {
            temp = bcmMessage;
        } else if (key.equals("s")) {
            temp = rsrMessage;
        }
        if (temp != null) {
            for (String item : temp) {
                System.out.println(item);
            }
            if (temp.size() <= 0) {
                System.out.println("no news");
            }
        } else {
            System.out.println("wrong option~");
        }
    }

    /**
     * The signal used to get the online list to send to the server
     */
    public void isOnline() {
        //Edit message type
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_Online_User);
        //Send a message
        sendMessage(message);
    }
    /**
     * send port
     * @param port ：port
     */
    public void  ipAndPort(int port,String getter) {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_IpAndPort);

        message.setContent(port+"");

        message.setGetter(getter);

        message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));

        sendMessage(message);
    }

    /**
     * send filename
     * @param filename
     * @param getter
     */
    public void  sendFilename(String filename,String getter) {
        Message message = new Message();
        message.setMessageType(MessageType.MESSAGE_UDP_FILE_NAME);

        message.setContent(filename);

        message.setGetter(getter);

        message.setSendTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format( new Date()));

        sendMessage(message);
    }
}














