package bullyalgorithm;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author murilo erhardt
 */
public class BullyAlgorithm {

    //Flag para identificar se a instancia é coord
    public static boolean isCoord = false;
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
    public static String instanceStatus = "WAITING";
    //Instancia multicast
    public static MultiSender ms;

    public static void main(String[] args) throws IOException, InterruptedException {
        createId();
        creatServerSocket();
        ms = new MultiSender();

        Thread threadMultiRecieve = new Thread(new MultiRecieve());
        threadMultiRecieve.start();
        Thread threadUniRecieve = new Thread(new UniRecieve(server));
        threadUniRecieve.start();

        whoIsTheCoordinator();
        System.out.println("----->Entrnado no areyoualive");
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
        String message = "WIC->" + myPort + "->" + myId;

        System.out.println("Quem manda nessa porra?");
        instanceStatus = "WIC"; //WIC = Who Is Coordinator
        ms.sendMessage(message);

        while ((portCoord == null)) {
            Thread.sleep(10);
        }

    }

    public static void areYouAlive() {
        new Thread() {

            @Override
            public void run() {
                System.out.println("----->Entrou no areyoualive");
                while (true) {
                    if (!(BullyAlgorithm.instanceStatus.equals("ELECTION"))) {
                        Random rand;
                        rand = new Random();

                        int sleep = rand.nextInt(4900) + 5000;
                        try {
                            Thread.sleep(sleep);
                            if (isCoord == false && portCoord != null) {
                                if (!(instanceStatus.equals("ELECTION"))) {
                                    instanceStatus = "WIC";
                                    String message = "AYA->" + myPort;
                                    UniSender.sendMessage(message, Integer.parseInt(portCoord));
                                    System.out.println("Are you alive my old friend?");
                                }
                            } else {
                                System.out.println("Eu continuo sendo coordenador");
                            }
                        } catch (InterruptedException ex) {
                            System.out.println("---------------------------------ERRO 65916");
                            Logger.getLogger(BullyAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            System.out.println("--------------------------------ERRO 14683");
                            Logger.getLogger(BullyAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }

            }
        }.start();

    }

}
