package bullyalgorithm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 *
 * @author murilo erhardt
 */
public class MultiSender {

    public static InetAddress addrMulti;
    public static DatagramSocket ds;
    public static String group = "228.5.6.7";
    public static Integer port = 5865;

    public MultiSender() throws UnknownHostException, SocketException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        InetAddress addr = InetAddress.getByName(this.group);
        this.addrMulti = addr;
        DatagramSocket ds = new DatagramSocket();
        this.ds = ds;
    }

    public static void sendMessage(String message) throws IOException {
        byte[] sendMessage = message.getBytes();
        DatagramPacket pkg = new DatagramPacket(sendMessage, sendMessage.length, addrMulti, port);
        DatagramSocket ds = new DatagramSocket();
        System.out.println("Enviou multi");
        ds.send(pkg);
    }
}
