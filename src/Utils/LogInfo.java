package Utils;

import java.io.BufferedWriter;
        import java.io.File;
        import java.io.FileNotFoundException;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.OutputStreamWriter;
        import java.text.SimpleDateFormat;
        import java.util.Calendar;
import java.util.Date;

/**
 * A class that provides logging methods
 */
public class LogInfo {
    //file path to save logs
    private static String loginLogPath= "src\\resource\\loginLog";
    private static String messagePath= "src\\resource\\messageLog";
    private static String SRIdMessageLog= "src\\resource\\SrMessageLog";
    private static Calendar cal;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * log
     * @param username ：username
     * @param ip ：ip address
     * @param port ：listening port
     */
    public static void saveLoginLog(String username,String ip,int port,Date date){
        File file=new File(loginLogPath);
        BufferedWriter bw = null;
        try {
            cal = Calendar.getInstance();
            Date time = cal.getTime();
            String format = sdf.format(time);
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            bw.write("\n"+"time:  "+format+";  user:  "+username+";  ip:  "+ip+";  listening port:  "+port+" active time: "+date);
            bw.flush();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if(null!=bw){
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("Stream operation exception");
                }
            }
        }
    }

    /**
     *  BCM
     * @param username ：username
     * @param content ：Message content
     */
    public static void saveMessageLog(String username,String content){
        File file=new File(messagePath);
        BufferedWriter bw = null;
        try {
            cal = Calendar.getInstance();
            Date time = cal.getTime();
            String format = sdf.format(time);
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            bw.write("\n"+"time:  "+format+";  user:  "+username+";  content:  "+content+";");
            bw.flush();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if(null!=bw){
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("Stream operation exception");
                }
            }
        }
    }

    /**
     * BCM
     * @param content ：消息内容
     */
    public static void saveSRIdMessageLog(String username1,String username2,String content){
        File file=new File(SRIdMessageLog);
        BufferedWriter bw = null;
        try {
            cal = Calendar.getInstance();
            Date time = cal.getTime();
            String format = sdf.format(time);
            bw = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file, true)));
            bw.write("\n"+"time:  "+format+";  room member:  "+username1+","+username2+";  content:  "+content+";");
            bw.flush();
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally{
            if(null!=bw){
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("Stream operation exception");
                }
            }
        }
    }
    public static void main(String[] args) {

    }
}