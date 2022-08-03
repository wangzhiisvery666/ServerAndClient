import Base.UdpPort;
import Utils.Utility;
import udp.UdpServerThread;
import udp.UdpClient;


import java.text.SimpleDateFormat;
import java.util.Date;

import static Base.UdpPort.UDP_send_port;

/**
 * Client interface
 */
public class ClientView {
    private final ClientServiceThread clientServiceThread = new ClientServiceThread();
    private boolean loop = true;

    public static void main(String[] args) throws InterruptedException {
        ClientService.ip= args[0];
        ClientService.port= Integer.parseInt(args[1]);
        UdpPort.client_udp_Port=Integer.parseInt(args[2]);

        new ClientView().view();
    }

    public void view() throws InterruptedException {
        while (loop) {
            String user ="";
            System.out.println("1、login");
            System.out.println("9、exit");
            char c = Utility.readChar();
            switch (c) {
                case '1':
                    System.out.println("Please enter a user name");
                    user = Utility.readString(20);
                    System.out.println("Enter the password");
                    String password = Utility.readString(20);
                    ClientService client_receive = new ClientService();
                    if (client_receive.getLoginMessage(user, password)) {
                        System.out.println("==========welcome (" + user + ") Log in the system===========");
                        boolean loop1 = true;
                        while (loop1) {
                            System.out.println("1、ATU Show active users");
                            System.out.println("2、BCM Common message");
                            System.out.println("3、SRS Separate chat");
                            System.out.println("4、Video conference");
                            System.out.println("5、RDM");
                            System.out.println("6、UDP");
                            System.out.println("9、OUT");
                            System.out.println("Enter the business you want to select");
                            String key = Utility.readString(1);
                            switch (key) {
                                case "1":
                                    System.out.println("=======Active users are as follows========");
                                    client_receive.getOnLineUserList();
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    System.out.println("\n");
                                    break;
                                case "2":
                                    System.out.println("========( BCM Enter q to exit the BCM)=======");
                                    String content = "";
                                    String sendTime;
                                    while (true) {
                                        sendTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                                        System.out.println(sendTime);
                                        content = Utility.readString(20);
                                        if (content.equals("q")) {
                                            break;
                                        } else {
                                            client_receive.commonChat(content);
                                        }
                                    }
                                    break;
                                case "3":
                                    client_receive.isOnline();
                                    System.out.println("Please enter a contact to select");
                                    String person = Utility.readString(10);
                                    //发送获取在线信息的消息
                                    if (ClientServiceThread.onlineUser == null || !ClientServiceThread.onlineUser.toString().contains(person)) {
                                        System.out.println("The user is offline or does not exist");
                                        break;
                                    }
                                    System.out.println("========( " + person + "Enter q to exit )=======");
                                    while (true) {
                                        sendTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                                        System.out.println(sendTime);
                                        content = Utility.readString(30);
                                        if (content.equals("q")) {
                                            break;
                                        } else {
                                            client_receive.privateChat(content, person);
                                        }
                                    }
                                    System.out.println("Have withdrawn and " + person + " The chat");
                                    break;
                                case "4":
                                    System.out.println("Please enter your choice");
                                    System.out.println("a、The audience");
                                    System.out.println("b、The speaker");
                                    System.out.println("q、exit");
                                    key = Utility.readString(1);

                                    if ("a".equals(key)) {
                                        System.out.println("Waiting for the presenter to send a video....");
                                        System.out.println("Enter any key to exit");
                                        Utility.readString(1);
                                    } else if ("b".equals(key)) {
                                        client_receive.isOnline();
                                        System.out.println("Please enter a contact to select");
                                        person = Utility.readString(30);
                                        //发送获取在线信息的消息
                                        if (ClientServiceThread.onlineUser == null || !ClientServiceThread.onlineUser.toString().contains(person)) {
                                            System.out.println("The user is offline or does not exist");
                                            break;
                                        }
                                        System.out.println("========( " + person + " Enter q to exit )=======");
                                        while (true) {
                                            sendTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
                                            System.out.println(sendTime);
                                            System.out.println("Please enter the name of the video you want to send (Enter q to exit)");
                                            content = Utility.readString(30);
                                            if (content.equals("q")) {
                                                break;
                                            } else {
                                                client_receive.sendFile(content, person);
                                            }
                                        }
                                        System.out.println("Have withdrawn and " + person + " The chat");

                                    }
                                    if ("q".equals(key)) {
                                        break;
                                    }
                                    break;
                                case "5":
                                    System.out.println("Please enter your choice");
                                    System.out.println("b、BCMMessage");
                                    System.out.println("s、RSRMessage");
                                    System.out.println("q、exit");
                                    key = Utility.readString(1);
                                    client_receive.queryMES(key);
                                    System.out.println("Any key exit");
                                    key = Utility.readString(1);
                                    break;
                                case "6":
                                    System.out.println("1、you are the server");
                                    System.out.println("2、you are the client");
                                    String s = Utility.readString(1);
                                    if (s.equals("1")){
                                        UdpServerThread udp = new UdpServerThread();
                                        udp.start();
                                    }else {


                                        client_receive.isOnline();
                                        System.out.println("Please enter the contact you want to select");
                                        person = Utility.readString(10);
                                        //发送获取在线信息的消息
                                        if (ClientServiceThread.onlineUser == null || !ClientServiceThread.onlineUser.toString().contains(person)) {
                                            System.out.println("The user is offline or the user does not exist");
                                            break;
                                        }
                                        System.out.println("=======Active users are as follows========");
                                        client_receive.getOnLineUserList();
                                        client_receive.ipAndPort(UdpPort.client_udp_Port,person);
                                        System.out.println("Please enter the recipient's port number");
                                        int  getterPort=Utility.readAnyLongInt(8);
                                        System.out.println("Please enter the file path to send");
                                        UDP_send_port=Utility.readString(40);
                                        client_receive.sendFilename(UDP_send_port,person);
                                        new UdpClient().udpFile(getterPort);
                                        System.out.println("any key to exit");
                                        char c1 = Utility.readChar();
                                    }
                                    break;
                                case "9":
                                    System.out.println("goodbye  :"+user);
                                    client_receive.closeSeverSocket();
                                    try {
                                        Thread.sleep(50);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                        }
                    }
                    break;
                case '9':
                    System.out.println("goodbye  :"+user);
                    loop = false;
                    break;
            }
        }
    }
}
