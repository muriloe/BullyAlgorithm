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

    public static void sendMessage(String message, int portToSend) {
        Socket clientSocket;
        try {
            clientSocket = new Socket("localhost", portToSend);
            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            outToServer.writeUTF(message);
            System.out.println("Enviou uni " + message);
        } catch (IOException ex) {
            System.out.println("não foi possível enviar unicast: " + ex);
        }
    }

}
