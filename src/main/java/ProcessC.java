import java.net.Socket;
import java.util.StringTokenizer;

public class ProcessC extends Process {

    private Socket socketA;

    public ProcessC(String name) {
        super(name);
    }
    public void run() {

        while (true) {
            String liczbyB = getFrom("B-C");
            zegar.tick();
            String liczbyD = getFrom("D-C");
            zegar.tick();
            String roznica = calculate(liczbyB, liczbyD);
            sendInfoToE("["+zegar.getTime()+"]" + "Przetworzylem liczby na " + roznica);
            zegar.tick();
            sendTo("A", roznica);
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
        ProcessC process = new ProcessC("C");
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
