import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ProcessC {

    private static Socket socketA;
    private static ServerSocket socket;

    static {
        try {
            socket = new ServerSocket(8082);
            socketA = new Socket("localhost", 8080);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        while(true) {

            String fromB = getFromB();
            String fromD = getFromD();

            process(fromB);

            String fromC = getFromC();
            System.out.println(fromC);
        }

    }

    private static String getFromB() throws IOException {
        Socket socketB = socket.accept();

        DataInputStream input =new DataInputStream(socketB.getInputStream());
        DataOutputStream output =new DataOutputStream(socketB.getOutputStream());
        if(input.readUTF().equals("ProcessB: gotowy?")){
            output.writeBoolean(true);
            return input.readUTF();
        }
        return null;
    }
    private static String getFromD() throws IOException {
        Socket socketD = socket.accept();

        DataInputStream input =new DataInputStream(socketD.getInputStream());
        DataOutputStream output =new DataOutputStream(socketD.getOutputStream());
        if(input.readUTF().equals("ProcessD: gotowy?")){
            output.writeBoolean(true);
            return input.readUTF();
        }
        return null;
    }



    private static void sendToA(String liczby) throws IOException {

        DataInputStream dataInputStream = new DataInputStream(socketD.getInputStream());
        DataOutputStream output = new DataOutputStream(socketD.getOutputStream());

        output.writeUTF("gotowy?");
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
