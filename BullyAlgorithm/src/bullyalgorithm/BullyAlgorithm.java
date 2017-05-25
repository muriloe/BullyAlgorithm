package bullyalgorithm;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author murilo erhardt
 */
public class BullyAlgorithm {

    //Flag para identificar se a instancia é coord
    public static boolean isCoord;
    //Armazena a porta na rede do coordenador
    public static String portCoord;
    //Armazena o id da instancia usada para a eleição na rede
    public static int myId;
    //Armazena a porta da instancia
    public static String myPort;
    //Flag para identificar se essa instancia já está fazendo uma eleição
    public static boolean doingElection;
    //ServerSocket
    public static ServerSocket server;
    //Armazena uma string representando o status do programa
    public static String instanceStatus;

    public static void main(String[] args) throws IOException, InterruptedException {
        createId();
        creatServerSocket();

        Thread threadMultiRecieve = new Thread(new MultiRecieve());
        threadMultiRecieve.start();
        Thread threadUniRecieve = new Thread(new UniRecieve(server));
        threadUniRecieve.start();

        whoIsTheCoordinator();
        areYouAlive();

    }

    //Gera um id para a instancia
    public static void createId() {
        Random rand;
        rand = new Random();
        myId = rand.nextInt(1000) + 1;
        System.out.println("Meu Id: " + myId);
    }

    public static void creatServerSocket() throws IOException {
        server = new ServerSocket(0);
        myPort = Integer.toString(server.getLocalPort());
        System.out.println("Minha porta: " + myPort);
    }

    public static void whoIsTheCoordinator() throws IOException, InterruptedException {
        MultiSender multiSend = null;
        String message = "WIC->" + myPort + "->" + myId;
        MultiSender ms = new MultiSender();
        System.out.println("Quem manda nessa porra?");
        instanceStatus = "WIC"; //WIC = Who Is Coordinator
        ms.sendMessage(message);
        
        while ((portCoord == null)) {
            break;
        }

    }

    public static void areYouAlive() {
        new Thread() {

            @Override
            public void run() {
                while (true) {
                    Random rand;
                    rand = new Random();
                    int sleep = rand.nextInt(10000) + 5000;
                    try {
                        Thread.sleep(sleep);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(BullyAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    System.out.println("Are you alive my old friend?");
                }

            }
        }.start();

    }

}
