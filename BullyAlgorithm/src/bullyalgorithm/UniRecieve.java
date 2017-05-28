package bullyalgorithm;

import static bullyalgorithm.MultiRecieve.timeOut;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author murilo erhardt
 */
public class UniRecieve implements Runnable {

    public static ServerSocket server;
    public static DataInputStream inputStream;
    public static Socket socketClient = null;

    public UniRecieve(ServerSocket server) {
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                recieveMessage();
            } catch (IOException ex) {
                Logger.getLogger(UniRecieve.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(UniRecieve.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public static void recieveMessage() throws IOException, InterruptedException {
        String message = null;
        socketClient = server.accept();
        inputStream = new DataInputStream(socketClient.getInputStream());
        message = inputStream.readUTF();

        decodeMessage(message);
    }

    public static void decodeMessage(String data) throws InterruptedException {
        System.out.println("UNI RECEBIDO: " + data);
        Thread.sleep(100);
        String messageDiv[] = data.split("->");
        if (messageDiv[0].equals("IAC")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                BullyAlgorithm.portCoord = messageDiv[1];
                BullyAlgorithm.instanceStatus = "Eu computador " + BullyAlgorithm.myId
                        + " declaro o computador " + messageDiv[2] + " porta " + messageDiv[1] + " como meu legitimo coordenador";
            }
        }

        if (messageDiv[0].equals("AYA")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                UniSender.sendMessage("IAA->" + BullyAlgorithm.myPort + "->" + BullyAlgorithm.myId, Integer.parseInt(messageDiv[1]));
            }
        }

        if (messageDiv[0].equals("IAA")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                BullyAlgorithm.isCoordAlive = true;
                BullyAlgorithm.instanceStatus = "Computador coordenador está vivo!";
            }
        }

        if (messageDiv[0].equals("OK")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                BullyAlgorithm.okMessage = true;
                BullyAlgorithm.instanceStatus = "Eu não sou o coordenador";
            }
        }

    }

}
