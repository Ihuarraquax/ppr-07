import java.net.Socket;
import java.util.StringTokenizer;

public class ProcessD extends Process {

    private  Socket socketC;

    public ProcessD(String name){
        super(name);
    }

    public void run() {
        while (true) {
            String liczby = getFrom("D");
            zegar.tick();
            String calculate = calculate(liczby);
            sendInfoToE("["+zegar.getTime()+"]" + "przetworzylem liczby na " + calculate);
            zegar.tick();
            sendTo("D-C", calculate);
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
        ProcessD process = new ProcessD("D");
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
