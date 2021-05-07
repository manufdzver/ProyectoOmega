import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RequestReciever extends Thread{

    private Usuario usuario;
    private JTextArea textField;
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String colaPublica;
    private JFrame chatroom;
    private JComboBox cbDestinatario;
    private boolean goodByeReceived = false;
    private ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
    private Connection connection;
    private HashMap<String, Usuario> directorioTotal;

    public RequestReciever(JFrame frame, Usuario usr, JTextArea txt,JComboBox cbDestinatario, HashMap directorioTotal){
        this.chatroom = frame;
        this.usuario = usr;
        this.textField = txt;
        this.colaPublica = usr.getNombre();
        this.cbDestinatario = cbDestinatario;
        this.directorioTotal = directorioTotal;
        actualizaDirectorio();
    }

    @Override
    public void run() {
        System.out.println("Usuario: " + usuario.getNombre());
        System.out.println("Clave publica: " + usuario.getClavePrivada());
        getMessages();
    }

    public void getMessages() {
        ObjectMessage objMessageReciever;
        Mensaje mensaje;

        try {
            connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false /*Transacter*/, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(colaPublica);
            MessageConsumer messageConsumer = session.createConsumer(destination);

            while (!goodByeReceived) {
                System.out.println("Waiting for messages...");
                objMessageReciever = (ObjectMessage) messageConsumer.receive();
                mensaje = (Mensaje) objMessageReciever.getObject();//Lee el primer mensaje en la cola
                if (mensaje != null) {
                    if(mensaje.getTipo()==2){
                        
                    }
                    if(mensaje.getTipo()==3){
                        Usuario autor = directorioTotal.get(mensaje.getClavePrivada());
                        System.out.println("Message received");

                        textField.setText(textField.getText() + "\n"+"Te agregó: " +autor.getNombre());
                        usuario.addDirectorio(autor);
                        actualizaDirectorio();
                    }
                }
                if (mensaje != null && mensaje.getMensaje().equals("Good bye!")) {
                    goodByeReceived = true;
                    System.out.println("Goodbye");
                }
            }
            System.out.println("Termino el hilo RequestReciever de"+usuario.getNombre());
            messageConsumer.close();
            session.close();
            connection.close();

        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
    public void actualizaDirectorio(){
        HashMap miDirectorio = usuario.getMiDirectorio();
        cbDestinatario.removeAllItems();
        Iterator miIterator = miDirectorio.entrySet().iterator();

        while (miIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry)miIterator.next();
            cbDestinatario.addItem(mapElement.getValue());
        }
    }
}
