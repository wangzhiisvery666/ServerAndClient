package udp;

import Base.UdpPort;
import udp.UDPUtils;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;

import static Base.UdpPort.UDP_FILENAME;


/**
 * UDP server
 */
public class UdpServerThread extends Thread{

    @Override
    public void run() {


            byte[] buf = new byte[ UDPUtils.BUFFER_SIZE];

            DatagramPacket dpk = null;
            DatagramSocket dsk = null;
            BufferedOutputStream bos = null;
            try {
                //存储数据
                dpk = new DatagramPacket(buf, buf.length);
                //监听端口
                dsk = new DatagramSocket(UdpPort.client_udp_Port);
                bos = new BufferedOutputStream(Files.newOutputStream(Paths.get("D:/"+UDP_FILENAME)));
                System.out.println("wait client ....");
                dsk.receive(dpk);
                dpk = new DatagramPacket(buf, buf.length,new InetSocketAddress(InetAddress.getByName("localhost"), UdpPort.sender_Port));

                int readSize = 0;
                int readCount = 0;
                int flushSize = 0;
                while((readSize = dpk.getLength()) != 0){
                    // validate client send exit flag
                    if(UDPUtils.isEqualsByteArray(UDPUtils.exitData, buf, readSize)){
                        System.out.println("server exit ...");
                        // send exit flag
                        dpk.setData(UDPUtils.exitData, 0, UDPUtils.exitData.length);
                        dsk.send(dpk);
                        break;
                    }

                    bos.write(buf, 0, readSize);
                    if(++flushSize % 1000 == 0){
                        flushSize = 0;
                        bos.flush();
                    }
                    dpk.setData(UDPUtils.successData, 0, UDPUtils.successData.length);
                    dsk.send(dpk);

                    dpk.setData(buf,0, buf.length);
                    dsk.receive(dpk);

                }
                System.out.println("received successfully!");
                // last flush
                bos.flush();
            } catch (Exception e) {
                e.printStackTrace();
            } finally{
                try {
                    if(bos != null)
                        bos.close();
                    if(dsk != null)
                        dsk.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
