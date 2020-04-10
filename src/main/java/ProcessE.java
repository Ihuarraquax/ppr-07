import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.TextMessage;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.time.LocalTime;

public class ProcessE extends Process {


    public ProcessE(String queueName) {
        super(queueName);


    }

    public void run() {
        MessageConsumer e = createConsumer("E");
        while (true) {
            try {
                TextMessage receive = (TextMessage) e.receive();
                printMSG(receive.getText());
            } catch (JMSException jmsException) {
                jmsException.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ProcessE process = new ProcessE("E");
        process.run();
    }

    private void printMSG(String message){

        String color = null;
        switch (message.charAt(0)){
            case 'A':
                color = ANSI_GREEN;
                break;
            case 'B':
                color = ANSI_RED;
                break;
            case 'C':
                color = ANSI_YELLOW;
                break;
            case 'D':
                color = ANSI_BLUE;
                break;

        }
        System.out.println(color+"[RT:"+ LocalTime.now()+"] "+message+ANSI_RESET);
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

