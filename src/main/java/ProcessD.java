import java.net.Socket;
import java.util.StringTokenizer;

public class ProcessD extends Process {

    private  Socket socketC;

    public ProcessD(int port){
        super();
        MY_PORT = port;
    }

    private void init() {
        startMapSocketThread(MY_PORT);
        socketC = handShakeWith(PROCESS_C_PORT);

    }

    public void run() {
        init();
        while (true) {
            String liczby = getFrom(PROCESS_A_PORT);
            zegar.tick();
            String calculate = calculate(liczby);
            sendInfoToE("["+zegar.getTime()+"]" + "przetworzylem liczby na " + calculate);
            zegar.tick();
            sendTo(socketC, calculate);
            zegar.tick();
            sendInfoToE("["+zegar.getTime()+"]" + "wracam do kroku 1 ");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public static void main(String[] args) {
        ProcessD process = new ProcessD(PROCESS_D_PORT);
        process.run();
    }

    private String calculate(String liczby) {
        StringTokenizer stringTokenizer = new StringTokenizer(liczby);
        int[] tab = new int[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreElements()) {
            tab[i++] = Integer.parseInt(stringTokenizer.nextToken());
        }

        for (int j = 0; j < tab.length; j++) {
            tab[j] = (int) Math.pow(tab[j], 2);
        }

        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < tab.length; k++) {

            sb.append(String.valueOf(tab[k]));
            sb.append(" ");
        }
        return sb.toString();
    }
}
