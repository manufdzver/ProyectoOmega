import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;

public class ChatReciever extends Thread{

    private Usuario usuario;
    private JTextArea textField;
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String colaPriv;
    private JFrame chatroom;
    private boolean goodByeReceived = false;
    private ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
    private Connection connection;
    private HashMap<String, Usuario> directorioTotal;

    private static MessageProducer messageProducer;
    private static ObjectMessage objMessageSender;
    private Mensaje mensaje = new Mensaje();

    public ChatReciever(JFrame frame, Usuario usr, JTextArea txt, HashMap directorioTotal){
        this.chatroom = frame;
        this.usuario = usr;
        this.textField = txt;
        this.colaPriv = usr.getClavePrivada();
        this.directorioTotal = directorioTotal;
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
                    Usuario autor = directorioTotal.get(mensaje.getClavePrivada());
                    System.out.println("Message received");
                    if(mensaje.getTipo()==1){
                        textField.setText(textField.getText() + "\n"+autor.getNombre()+"-"+mensaje.getMensaje());
                        mensajeLeido(autor.getClavePrivada(), mensaje.getMensaje());
                        mensaje.setTipo(2);
                    }
                    else{
                        textField.setText(textField.getText() + "\n    ("+autor.getNombre()+" "+mensaje.getMensaje()+")");
                    }
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
    public void mensajeLeido(String destino, String mens) {
        if (!destino.equals("")) {
            try {
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
                connectionFactory.setTrustAllPackages(true);
                Connection connection = connectionFactory.createConnection(); //Crea la conexion
                connection.start(); //Arranca la conexion

                Session session = connection.createSession(false /*Transacter*/, Session.AUTO_ACKNOWLEDGE); //Cuando consume un mensaje, automaticamente manda el acknowledgment

                Destination producer = session.createQueue(destino);
                messageProducer = session.createProducer(producer);
                objMessageSender = session.createObjectMessage();

                mensaje.setMensaje("leyó tu mensaje......."+mens);
                mensaje.setClavePrivada(usuario.getClavePrivada());
                mensaje.setTipo(3);
                System.out.println("Sending: aceptacion a Destinatario: " + destino + "\n de: " + usuario.getNombre());
                objMessageSender.setObject(mensaje);
                messageProducer.send(objMessageSender);

            } catch (JMSException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("No hay nadie quien agregar");
        }
    }
}
