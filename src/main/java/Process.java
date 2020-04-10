import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class Process {
    protected static final long SLEEP_TIME = 10000;
    protected static final int PROCESS_E_PORT = 8084;
    public static int MY_PORT;
    public ServerSocket serverSocket;
    public static final int PROCESS_A_PORT = 8080;
    public static final int PROCESS_B_PORT = 8081;
    public static final int PROCESS_C_PORT = 8082;
    public static final int PROCESS_D_PORT = 8083;
    public Map<Integer, Socket> clients = Collections.synchronizedMap(new HashMap<>());
    protected ZegarLamporta zegar = new ZegarLamporta();
    public DataOutputStream outputE;

    public Process() {
        try {
            Socket socket = new Socket("localhost", PROCESS_E_PORT);
            outputE = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getFrom(int processPort) {
        try {
            while(!clients.containsKey(processPort)){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Socket socket = clients.get(processPort);
            DataInputStream input = new DataInputStream(socket.getInputStream());
            String s = input.readUTF();
            int indexOf = s.indexOf(':');
            int time = Integer.parseInt(s.substring(0, indexOf));
            zegar.reviceAction(time);
            s = s.substring(indexOf + 1);
            sendInfoToE("["+zegar.getTime()+"]" + " odtrzymalem " + s + " od " + processPort);
            return s;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void sendTo(Socket socket, String liczby) {
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(zegar.getTime() +":" +liczby);
            sendInfoToE("["+zegar.getTime()+"]" + " wyslalem " + liczby + " do " + socket.getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void startMapSocketThread(int port) {
        Runnable runnable = () -> {
            try {
                serverSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            while (true) {
                try {
                    Socket accept = serverSocket.accept();
                    DataInputStream input = new DataInputStream(accept.getInputStream());
                    clients.put(Integer.parseInt(input.readUTF()), accept);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(runnable);
    }

    protected void sendInfoToE(String message) {
        try {
            System.out.println(message);
            outputE.writeUTF(MY_PORT+message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static Socket handShakeWith(int processPort) {
        Socket socket = null;
        while (socket == null) {
            try {
                Thread.sleep(1000);
                socket = new Socket("localhost", processPort);
            } catch (IOException | InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            output.writeUTF(String.valueOf(MY_PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return socket;
    }
}
