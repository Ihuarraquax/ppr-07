import java.net.Socket;
import java.util.Random;

public class ProcessA extends Process {

    private Socket socketB;
    private Socket socketD;

    public ProcessA(int port){
        super();
        MY_PORT = port;
    }

    private void init() {
        startMapSocketThread(PROCESS_A_PORT);
        socketB = handShakeWith(PROCESS_B_PORT);
        socketD = handShakeWith(PROCESS_D_PORT);

    }

    public void run() {
        init();
        while (true) {
            String liczby = generate();
            sendInfoToE("["+zegar.getTime()+"]" + "wygenerowalem " + liczby);
            zegar.tick();
            sendTo(socketB, liczby);
            zegar.tick();
            sendTo(socketD, liczby);
            zegar.tick();
            String fromC = getFrom(PROCESS_C_PORT);
            zegar.tick();
            sendInfoToE("["+zegar.getTime()+"]" + "wracam do kroku 1 ");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {
        ProcessA processA = new ProcessA(PROCESS_A_PORT);

        processA.run();
    }

    private String generate() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 5; i++) {

            sb.append(String.valueOf(random.nextInt(1000)));
            sb.append(" ");
        }
        return sb.toString();
    }
}
