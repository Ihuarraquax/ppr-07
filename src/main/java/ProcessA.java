import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ProcessA {


    private static Socket socketB;
    private static Socket socketD;
    private static ServerSocket socket;

    static {
        try {
            socket = new ServerSocket(8080);
            socketB = new Socket("localhost", 8081);
            socketD = new Socket("localhost", 8083);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        while(true) {
            String liczby = generate();

            sendToB(liczby);

            sendToD(liczby);

            String fromC = getFromC();
            System.out.println(fromC);
        }

    }

    private static String getFromC() throws IOException {
        Socket socketA = socket.accept();

        DataInputStream input =new DataInputStream(socketA.getInputStream());
        DataOutputStream output =new DataOutputStream(socketA.getOutputStream());
        if(input.readUTF().equals("gotowy?")){
            output.writeBoolean(true);
            return input.readUTF();
        }
        return null;
    }

    private static void sendToD(String liczby) throws IOException {

        DataInputStream dataInputStream = new DataInputStream(socketD.getInputStream());
        DataOutputStream output = new DataOutputStream(socketD.getOutputStream());

        output.writeUTF("gotowy?");
        boolean b = dataInputStream.readBoolean();
        if(b){
            output.writeUTF(liczby);
        }
    }

    private static void sendToB(String liczby) throws IOException {

        DataInputStream dataInputStream = new DataInputStream(socketB.getInputStream());
        DataOutputStream output = new DataOutputStream(socketB.getOutputStream());
        output.writeUTF("ProcessA: gotowy?");
        boolean b = dataInputStream.readBoolean();
        if(b){
            output.writeUTF(liczby);
        }
    }

    private static String generate() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100; i++) {

            sb.append(String.valueOf(random.nextInt(100)));
            sb.append(" ");
        }
        return sb.toString();
    }
}
