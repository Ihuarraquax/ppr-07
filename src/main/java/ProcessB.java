import java.net.Socket;
import java.util.StringTokenizer;

public class ProcessB extends Process {


    private Socket socketC;

    public ProcessB(String name){
        super(name);
    }

    public void run() {
        while (true) {
            String liczby = getFrom("B");
            zegar.tick();
            String logLiczby = calculateLog(liczby);
            sendInfoToE("["+zegar.getTime()+"]" + "Przetworzylem liczby na " + logLiczby);
            zegar.tick();
            sendTo("B-C", logLiczby);
            zegar.tick();
            sendInfoToE("["+zegar.getTime()+"]" + "wracam do kroku 1 ");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ProcessB process = new ProcessB("B");
        process.run();
    }



    private String calculateLog(String liczby) {
        StringTokenizer stringTokenizer = new StringTokenizer(liczby);
        int[] tab = new int[stringTokenizer.countTokens()];
        int i = 0;
        while (stringTokenizer.hasMoreElements()) {
            tab[i++] = Integer.parseInt(stringTokenizer.nextToken());
        }

        for (int j = 0; j < tab.length; j++) {
            tab[j] = (int) Math.ceil(Math.log(tab[j]));
        }

        StringBuilder sb = new StringBuilder();
        for (int k = 0; k < tab.length; k++) {

            sb.append(String.valueOf(tab[k]));
            sb.append(" ");
        }
        return sb.toString();
    }
}
