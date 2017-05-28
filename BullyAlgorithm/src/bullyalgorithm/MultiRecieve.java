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
    public static int timeOut = 0;

    public MultiRecieve() throws IOException {

    }

    @Override
    public void run() {
        try {
            recieveMessage();
        } catch (IOException ex) {
            System.out.println(ex);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        }
    }

    public static void recieveMessage() throws IOException, InterruptedException {
        MulticastSocket mcs = new MulticastSocket(port);
        InetAddress grp = InetAddress.getByName(group);
        mcs.joinGroup(grp);
        byte rec[] = new byte[256];
        DatagramPacket pkg = new DatagramPacket(rec, rec.length);

        while (true) {
            try {
                mcs.receive(pkg);
                String data = new String(pkg.getData()).trim();
                System.out.println("Dados recebidos:" + data);
                decodeMessage(data);
            } catch (Exception ex) {
                System.out.println(ex);
            }
        }
    }

    public static void decodeMessage(String data) {
        String messageDiv[] = data.split("->");
        if (messageDiv[0].equals("WIC")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                if (BullyAlgorithm.isCoord == true) {
                    UniSender.sendMessage("IAC->" + BullyAlgorithm.myPort + "->" + BullyAlgorithm.myId, Integer.parseInt(messageDiv[1]));
                }
            }
        }

        if (messageDiv[0].equals("ELECTION")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                if (BullyAlgorithm.myId > Integer.parseInt(messageDiv[2])) {
                    UniSender.sendMessage("OK->" + BullyAlgorithm.myPort + "->" + BullyAlgorithm.myId, Integer.parseInt(messageDiv[1]));
                }
            }
        }

    }

}
