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
            Logger.getLogger(MultiRecieve.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(MultiRecieve.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void recieveMessage() throws IOException, InterruptedException {
        MulticastSocket mcs = new MulticastSocket(port);
        InetAddress grp = InetAddress.getByName(group);
        mcs.joinGroup(grp);
        byte rec[] = new byte[256];
        DatagramPacket pkg = new DatagramPacket(rec, rec.length);

        while (true) {
            checkTimeOut();
            mcs.setSoTimeout(timeOut);

            try {
                mcs.receive(pkg);
                String data = new String(pkg.getData()).trim();
                System.out.println("Dados recebidos:" + data);
                decodeMessage(data);
            } catch (Exception ex) {
                System.out.println("Virei coordenador nessa parada!!");
                BullyAlgorithm.isCoord = true;
                BullyAlgorithm.portCoord = BullyAlgorithm.myPort;
                BullyAlgorithm.instanceStatus="WAITING";
                timeOut = 0;
                
            }

        }
    }

    public static void decodeMessage(String data) {
        String messageDiv[] = data.split("->");
        if (messageDiv[0].equals("WIC")) {
            //Se a mensagem WIC (who is coordinator, for diferente da porta da instancia chega se a instancia é coord
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                if (BullyAlgorithm.isCoord == true) {
                    String answer = "IAC->" + BullyAlgorithm.myPort + "->" + BullyAlgorithm.myId;
                    UniSender.sendMessage(answer, Integer.parseInt(messageDiv[1]));
                }
            }
        }

    }

    public static void checkTimeOut() throws InterruptedException {
        Thread.sleep(100);
        if (BullyAlgorithm.instanceStatus.equals("WIC")) {
            timeOut = 10000;
        }
        else{
            timeOut = 0;
        }
    }

}
