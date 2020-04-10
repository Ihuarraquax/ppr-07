import java.util.Random;

public class ProcessA extends Process {

    public ProcessA(String name){
        super(name);
    }

    public void run() {
        while (true) {
            String liczby = generate();
            sendInfoToE("["+zegar.getTime()+"]" + "wygenerowalem " + liczby);
            zegar.tick();
            sendTo("B", liczby);
            zegar.tick();
            sendTo("D", liczby);
            zegar.tick();
            String fromC = getFrom("C");
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
        ProcessA processA = new ProcessA("A");
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
