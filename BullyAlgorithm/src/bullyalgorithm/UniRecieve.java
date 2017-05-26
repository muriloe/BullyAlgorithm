package bullyalgorithm;

import static bullyalgorithm.BullyAlgorithm.portCoord;
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
    public static int timeOut = 0;

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
        checkTimeOut();

        String message = null;
        socketClient = server.accept();
        inputStream = new DataInputStream(socketClient.getInputStream());
        message = inputStream.readUTF();
        System.out.println("Dados recebidos uni: " + message);
        decodeMessage(message);
        try {

        } catch (Exception ex) {
            if (BullyAlgorithm.instanceStatus.equals("AYA")) {
                BullyAlgorithm.instanceStatus = "ELECTION";
                MultiSender.sendMessage("ELECTION->" + BullyAlgorithm.myPort + "->" + BullyAlgorithm.myId);

            }
        }

    }

    public static void decodeMessage(String data) throws IOException, InterruptedException {
        System.out.println("---> " + data);
        String messageDiv[] = data.split("->");
        if (messageDiv[0].equals("IAC")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                BullyAlgorithm.portCoord = messageDiv[1];
                BullyAlgorithm.instanceStatus = "WAITING";
                System.out.println("Eu computador " + BullyAlgorithm.myId
                        + " reconheco o computador ID: " + messageDiv[2]
                        + " port: " + BullyAlgorithm.portCoord + " como o meu legitimo coordenador");
                BullyAlgorithm.isCoord = false;
            }
        }
        if (messageDiv[0].equals("AYA")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {

                UniSender.sendMessage("IAA->" + BullyAlgorithm.myPort, Integer.parseInt(messageDiv[1]));
                BullyAlgorithm.instanceStatus = "WAITING";
            }
        }
        if (messageDiv[0].equals("IAA")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                System.out.println("Eu coordenador estou vivo!");
            }
        }
        if (messageDiv[0].equals("OK")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                System.out.println("xxxx -- Eu não sou coordenador");
                BullyAlgorithm.instanceStatus = "WAITING";
            }
        }

    }

    public static void checkTimeOut() throws InterruptedException {
        Thread.sleep(100);
        switch (BullyAlgorithm.instanceStatus) {
            case "AYA":
                timeOut = 10000;
                break;
            default:
                timeOut = 1000000;
                break;
        }
    }

}
