package Base;

public interface MessageType {
      String MESSAGE_LOGIN_SUSSED="1";//Indicates successful login
      String MESSAGE_LOGIN_FAIL="2";//failed to login
      String MESSAGE_ONLINE_USER_LIST="3";//online user list
      String MESSAGE_ROOM="4";//general information
      String MESSAGE_CLIENT_EXIT="5";//The client exits the system
      String MESSAGE_GROUP="6";//group news
      String MESSAGE_FILE="7";//FILE
      String MESSAGE_LOGIN_FAIL_REPEATEDLY="8";//Too many login failures
      String MESSAGE_Online_User="9";//Online users, used to judge whether they are online before sending a message
      String MESSAGE_IpAndPort="10";// ip and port

      String MESSAGE_UDP_FILE_NAME="11";
}
