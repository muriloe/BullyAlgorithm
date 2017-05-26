package bullyalgorithm;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author murilo erhardt
 */
public class UniSender {

    public UniSender() {
    }

    public static void sendMessage(String message, int portToSend) throws IOException, InterruptedException {
        Socket clientSocket;
        try {
            clientSocket = new Socket("localhost", portToSend);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeUTF(message);
        } catch (IOException ex) {
            if (!(BullyAlgorithm.instanceStatus.equals("WAITING"))) {
                System.out.println("Iniciando Eleição");
                BullyAlgorithm.instanceStatus = "ELECTION";
                MultiSender.sendMessage("ELECTION->" + BullyAlgorithm.myPort + "->" + BullyAlgorithm.myId);
            }
            Thread.sleep(100);
        }
    }

}
