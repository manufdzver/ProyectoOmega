import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ChatReciever extends Thread{

    private Usuario usuario;
    private JTextArea textField;
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String colaPriv;
    private JFrame chatroom;
    private boolean goodByeReceived = false;
    private ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
    private Connection connection;

    public ChatReciever(JFrame frame, Usuario usr, JTextArea txt){
        this.chatroom = frame;
        this.usuario = usr;
        this.textField = txt;
        this.colaPriv = usr.getClavePrivada();



    }

    @Override
    public void run() {
        System.out.println("Usuario: " + usuario.getNombre());
        System.out.println("Clave privada: " + usuario.getClavePrivada());
        getMessages();
    }

    public void getMessages() {
        ObjectMessage objMessageReciever;
        Mensaje mensaje;

        try {
            connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false /*Transacter*/, Session.AUTO_ACKNOWLEDGE);

            Destination destination = session.createQueue(colaPriv);

            MessageConsumer messageConsumer = session.createConsumer(destination);


            while (!goodByeReceived) {
                System.out.println("Waiting for messages...");
                objMessageReciever = (ObjectMessage) messageConsumer.receive();
                mensaje = (Mensaje) objMessageReciever.getObject();//Lee el primer mensaje en la cola
                if (mensaje != null) {
                    System.out.println("Message received");
                    textField.setText(textField.getText() + "\n"+mensaje.getAutor().getNombre()+"-"+mensaje.getMensaje());
                }
                if (mensaje != null && mensaje.getMensaje().equals("Good bye!")) {
                    goodByeReceived = true;
                    System.out.println("Goodbye");
                }
            }
            System.out.println("Termino el hilo Reciever");

            messageConsumer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
