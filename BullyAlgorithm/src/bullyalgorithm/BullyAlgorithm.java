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
    public static String portCoord = null;
    //Armazena o id da instancia usada para a eleição na rede
    public static int myId;
    //Armazena a porta da instancia
    public static String myPort = null;
    //Flag para identificar se essa instancia já está fazendo uma eleição
    public static boolean doingElection;
    //ServerSocket
    public static ServerSocket server;
    //Armazena uma string representando o status do programa
    public static String instanceStatus;
    public static boolean isCoordAlive = false;
    public static boolean okMessage = false;

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

        if (okMessage == true) {
            portCoord = null;
            String message = "WIC->" + myPort + "->" + myId;
            MultiSender ms = new MultiSender();
            System.out.println("Quem manda nessa porra?");
            boolean foundCoord = false;
            while (portCoord == null) {
                ms.sendMessage(message);
                long startTime = System.currentTimeMillis(); //fetch starting time
                while (false || (System.currentTimeMillis() - startTime) < 10000) {
                    Thread.sleep(10); //Não apagar
                    if (portCoord != null && portCoord != myPort) {
                        System.out.println(instanceStatus);
                        foundCoord = true;
                        break;
                    }
                }
            }

        }

        if (okMessage == false) {
            portCoord = null;
            String message = "WIC->" + myPort + "->" + myId;
            MultiSender ms = new MultiSender();
            System.out.println("Quem manda nessa porra?");
            boolean foundCoord = false;
            ms.sendMessage(message);

            long startTime = System.currentTimeMillis(); //fetch starting time
            while (false || (System.currentTimeMillis() - startTime) < 10000) {
                Thread.sleep(10); //Não apagar
                if (portCoord != null && portCoord != myPort) {
                    System.out.println(instanceStatus);
                    foundCoord = true;
                    break;
                }
            }
            if (foundCoord == false) {
                System.out.println("Virei coordenador!!!");
                isCoord = true;
                portCoord = myPort;
            }
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
                    if (isCoord == false) {
                        System.out.println("Are you alive my old friend?");
                        UniSender.sendMessage("AYA->" + myPort + "->" + myId, Integer.parseInt(portCoord));

                        isCoordAlive = false;
                        long startTime = System.currentTimeMillis(); //fetch starting time
                        while (false || (System.currentTimeMillis() - startTime) < 10000) {
                            try {
                                Thread.sleep(10); //Não apagar
                            } catch (InterruptedException ex) {
                                Logger.getLogger(BullyAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (isCoordAlive == true) {
                                System.out.println(instanceStatus);

                                break;
                            }
                        }
                        if (isCoordAlive == false) {
                            try {
                                election();
                            } catch (IOException ex) {
                                System.out.println(ex);
                            } catch (InterruptedException ex) {
                                System.out.println(ex);
                            }
                        }
                    } else {
                        System.out.println("Ainda sou coordenador");
                    }

                }

            }
        }.start();
    }

    public static void election() throws IOException, InterruptedException {
        System.out.println("----------INICIANDO ELEIÇÃO----------");
        long startTime = System.currentTimeMillis(); //fetch starting time
        okMessage = false;
        MultiSender.sendMessage("ELECTION->" + myPort + "->" + myId);
        while (false || (System.currentTimeMillis() - startTime) < 10000) {
            try {
                Thread.sleep(10); //Não apagar
            } catch (InterruptedException ex) {
                Logger.getLogger(BullyAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (okMessage == true) {
                System.out.println(instanceStatus);

                break;
            }
        }
        if (okMessage == true) {
            System.out.println("sleep 15s");
            Thread.sleep(15000);
            whoIsTheCoordinator();
        }
        if (okMessage == false) {
            whoIsTheCoordinator();
        }

    }

}
