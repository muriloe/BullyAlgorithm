package bullyalgorithm;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Random;

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

    public static ServerSocket server;

    public static void main(String[] args) throws IOException {
        createId();
        MultiSender ms = new MultiSender();
        ms.sendMessage("cuzao");
        Thread thread = new Thread(new MultiRecieve());
        thread.start();

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

}
