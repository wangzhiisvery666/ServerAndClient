import java.util.HashMap;
import java.util.Map;

/**
 * Manage client thread threads
 */
public class ManageClientThread {
    private  final static  Map<String, ClientServiceThread>  map = new HashMap<>();

    /**
     * Add threads to the map collection
     * @param UserId ：UserId
     * @param clientConnectServerThread ：Client thread
     */
    public static void  addConnectServerThread(String UserId, ClientServiceThread clientConnectServerThread){
        map.put(UserId, clientConnectServerThread);
    }

    /**
     * Get the thread with id
     * @param UserId ：The key used to get the thread
     * @return ：The target thread
     */
    public static ClientServiceThread getMap(String UserId) {
        return map.get(UserId);
    }

}
