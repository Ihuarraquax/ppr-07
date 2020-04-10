import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;

public class ProcessC extends Process {

    private Socket socketA;

    public ProcessC(int port) {
        super();
        MY_PORT = port;
    }

    private void init() {
        startMapSocketThread(MY_PORT);
        socketA = handShakeWith(PROCESS_A_PORT);

    }

    public void run() {
        init();
        while (true) {
            String liczbyB = getFrom(PROCESS_B_PORT);
            zegar.tick();
            String liczbyD = getFrom(PROCESS_D_PORT);
            zegar.tick();
            String roznica = calculate(liczbyB, liczbyD);
            sendInfoToE("["+zegar.getTime()+"]" + "Przetworzylem liczby na " + roznica);
            zegar.tick();
            sendTo(socketA, roznica);
            zegar.tick();
            sendInfoToE("["+zegar.getTime()+"]" + "wracam do kroku 1 ");
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
    public static void main(String[] args) {
        ProcessC process = new ProcessC(PROCESS_C_PORT);

        process.run();
    }

    private String calculate(String liczbyB, String liczbyD) {
        StringTokenizer stringTokenizer = new StringTokenizer(liczbyB);
        int[] tab = new int[stringTokenizer.countTokens()];
        int[] tabB = new int[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreElements()) {
            tabB[i++] = Integer.parseInt(stringTokenizer.nextToken());
        }

        stringTokenizer = new StringTokenizer(liczbyD);
        int[] tabD = new int[stringTokenizer.countTokens()];
        i = 0;
        while (stringTokenizer.hasMoreElements()) {
            tabD[i++] = Integer.parseInt(stringTokenizer.nextToken());
        }

        for (int j = 0; j < tab.length; j++) {
            tab[j] = tabB[j] - tabD[j];
        }

        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < tab.length; k++) {

            sb.append(String.valueOf(tab[k]));
            sb.append(" ");
        }
        return sb.toString();
    }
}
