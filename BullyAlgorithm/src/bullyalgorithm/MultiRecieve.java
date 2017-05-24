package bullyalgorithm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author murilo erhardt
 */
public class MultiRecieve implements Runnable {

    public static String group = "228.5.6.7";
    public static Integer port = 5865;

    public MultiRecieve() throws IOException {

    }

    @Override
    public void run() {
        try {
            recieveMessage();
        } catch (IOException ex) {
            Logger.getLogger(MultiRecieve.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void recieveMessage() throws IOException {
        while (true) {
            MulticastSocket mcs = new MulticastSocket(port);
            InetAddress grp = InetAddress.getByName(group);
            mcs.joinGroup(grp);
            byte rec[] = new byte[256];
            DatagramPacket pkg = new DatagramPacket(rec, rec.length);
            mcs.receive(pkg);
            String data = new String(pkg.getData()).trim();
            System.out.println("Dados recebidos:" + data);
        }
    }

}
