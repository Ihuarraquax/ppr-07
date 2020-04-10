import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProcessE {

    private ServerSocket serverSocket;

    private void init() {
        try {
            serverSocket = new ServerSocket(8084);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ExecutorService executor = Executors.newFixedThreadPool(4);
        while (true){
            try {
                Socket accept = serverSocket.accept();
                executor.execute(new ConsolePrinter(accept,System.out));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void run() {
        init();
        while (true) {

        }
    }

    public static void main(String[] args) {
        ProcessE process = new ProcessE();
        process.run();
    }

    private class ConsolePrinter implements Runnable {

        private Socket socket;
        private PrintStream out;
        private DataInputStream input;

        private ConsolePrinter(Socket socket, PrintStream out) {
            this.socket = socket;
            this.out = out;
            try {
                input = new DataInputStream(socket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            String color = null;
            while (true) {
                try {
                    String message = input.readUTF();
                    switch (message.substring(0,4)){
                        case "8080":
                            color = ANSI_GREEN;
                            break;
                        case "8081":
                            color = ANSI_RED;
                            break;
                        case "8082":
                            color = ANSI_YELLOW;
                            break;
                        case "8083":
                            color = ANSI_BLUE;
                            break;

                    }
                    out.println(color+"[RT:"+ LocalTime.now()+"] "+message+ANSI_RESET);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
}

