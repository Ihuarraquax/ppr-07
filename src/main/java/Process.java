import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

public abstract class Process {
    protected static final long SLEEP_TIME = 10000;
    protected static String MY_QUEUE;
    protected ZegarLamporta zegar = new ZegarLamporta();
    protected Map<String, MessageProducer> producers = new HashMap<>();
    protected Map<String, MessageConsumer> consumers = new HashMap<>();

    private Session session;
    private Connection con;

    public MessageConsumer createConsumer(String name) {
        try {
            Queue queue = session.createQueue(name);
            return session.createConsumer(queue);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Process(String queueName) {
        MY_QUEUE = queueName;
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);

        try {
            con = connectionFactory.createConnection();
            session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
            con.start();
            Destination destination = session.createQueue("E");
            MessageProducer messageProducer = session.createProducer(destination);
            producers.put("E", messageProducer);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public String getFrom(String consumerName) {
        try {
            MessageConsumer consumer;
            if(consumers.containsKey(consumerName)) {
                consumer = consumers.get(consumerName);
            } else {
                Destination destination = session.createQueue(consumerName);
                consumer = session.createConsumer(destination);
                consumers.put(consumerName, consumer);
            }
            Message message = consumer.receive();
            TextMessage textMessage = (TextMessage) message;
            String s = textMessage.getText();
            System.out.println(s);
            int indexOf = s.indexOf(':');
            int time = Integer.parseInt(s.substring(0, indexOf));

            zegar.reviceAction(time);
            s = s.substring(indexOf + 1);
            sendInfoToE("[" + zegar.getTime() + "]" + " odtrzymalem " + s);
            return s;
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void sendTo(String producerName, String liczby) {
        try {

            MessageProducer messageProducer;
            if (producers.containsKey(producerName)) {
                messageProducer = producers.get(producerName);
            } else {
                Destination destination = session.createQueue(producerName);
                messageProducer = session.createProducer(destination);
                messageProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                producers.put(producerName, messageProducer);
            }
            TextMessage message = session.createTextMessage(zegar.getTime() + ":" + " wyslalem " + liczby + " do " + producerName);
            messageProducer.send(message);
            sendInfoToE("[" + zegar.getTime() + "]" + " wyslalem " + liczby + " do " + producerName);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    protected void sendInfoToE(String message) {
        try {
            MessageProducer messageProducer = producers.get("E");
            TextMessage textMessage = session.createTextMessage(MY_QUEUE + message);
            messageProducer.send(textMessage);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

}
