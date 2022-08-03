import Base.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * A class that manages user server connections to clients
 */
public class ManageServerConnectClientThread {
    private static  Map<String, ServerConnectThread> map=new HashMap<>();
    public static ArrayList<User> users=new ArrayList<>();

    public static  void addManageServerConnectClientThread(ServerConnectThread serverConnectThread,String user){
        map.put(user, serverConnectThread);
    }

    public static ServerConnectThread getMap(String user) {
        return map.get(user);
    }

    public static Map<String, ServerConnectThread> getMap() {
        return map;
    }

    //Returns a collection of online people
    public static Set<String>  getKeySet(){
        return getMap().keySet();
    }

    /**
     * How to get online users
     * @return ：Returns the string concatenated by online users
     */
    public static String getOnLineUser() {
        StringBuffer stringBuffer = new StringBuffer();
        Map<String, ServerConnectThread> map = ManageServerConnectClientThread.getMap();
        Set<String> strings = map.keySet();

        for (String S :
                strings) {
            stringBuffer.append(S).append(",");
        }
        if (strings.size()<=1){
            return null;
        }
        return new String(stringBuffer);
    }
}
