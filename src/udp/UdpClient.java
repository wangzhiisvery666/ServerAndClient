package udp;

import Base.UdpPort;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import static Base.UdpPort.UDP_send_port;

/**
 * UDP client
 */
public class UdpClient {

    public  void udpFile(int getterPort) {
        long startTime = System.currentTimeMillis();

        byte[] buf = new byte[UDPUtils.BUFFER_SIZE];
        byte[] receiveBuf = new byte[1];

        RandomAccessFile accessFile = null;
        DatagramPacket dpk = null;
        DatagramSocket dsk = null;
        int readSize = -1;
        try {
            accessFile = new RandomAccessFile(UDP_send_port,"r");
            //设置接受者的ip 和 端口
            dpk = new DatagramPacket(buf, buf.length,new InetSocketAddress(InetAddress.getByName("localhost"), getterPort));
            //发送者用自己的 ip 和 端口
            dsk = new DatagramSocket(UdpPort.client_udp_Port, InetAddress.getByName("localhost"));
            int sendCount = 0;
            while((readSize = accessFile.read(buf,0,buf.length)) != -1){
                dpk.setData(buf, 0, readSize);
                dsk.send(dpk);
//                 wait server response
                {
                    while(true){
                        dpk.setData(receiveBuf, 0, receiveBuf.length);
                        dsk.receive(dpk);

                        // confirm server receive
                        if(!UDPUtils.isEqualsByteArray(UDPUtils.successData,receiveBuf,dpk.getLength())){
                            System.out.println("resend ...");
                            dpk.setData(buf, 0, readSize);
                            dsk.send(dpk);
                        }else
                            break;
                    }
                }
            }
            // send exit wait server response
            while(true){
                System.out.println("client send exit message ....");
                dpk.setData(UDPUtils.exitData,0,UDPUtils.exitData.length);
                dsk.send(dpk);

                dpk.setData(receiveBuf,0,receiveBuf.length);
                dsk.receive(dpk);
                // byte[] receiveData = dpk.getData();
                if(!UDPUtils.isEqualsByteArray(UDPUtils.exitData, receiveBuf, dpk.getLength())){
                    System.out.println("client Resend exit message ....");
                    dsk.send(dpk);
                }else
                    break;
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                if(accessFile != null)
                    accessFile.close();
                if(dsk != null)
                    dsk.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        long endTime = System.currentTimeMillis();
        System.out.println("send success!  finish time:"+(endTime - startTime));
    }


}
