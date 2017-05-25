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
            }
            
            

        }
    }
    
    public static void recieveMessage() throws IOException{
        String message = null;
        socketClient = server.accept();
        inputStream = new DataInputStream(socketClient.getInputStream());
        message = inputStream.readUTF();
        decodeMessage(message);
    }
    
    public static void decodeMessage(String data){
         String messageDiv[] = data.split("->");
         if (messageDiv[0].equals("IAC")) {
            if (!(messageDiv[1].equals(BullyAlgorithm.myPort))) {
                BullyAlgorithm.portCoord = messageDiv[1];
                BullyAlgorithm.instanceStatus="WAITING";
                System.out.println("Eu computador " + BullyAlgorithm.myId 
                        + " reconheco o computador ID: " + messageDiv[2] 
                        + " port: "+ BullyAlgorithm.portCoord + " como o meu legitimo coordenador");
            }
        }
    }

}
