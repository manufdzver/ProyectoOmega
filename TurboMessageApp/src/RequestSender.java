import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class RequestSender extends Thread{
    private Usuario usuario;
    private JButton btnSolicitud;
    private JTextField txtSolicitud;
    private JButton btnEnviar;
    private JTextArea txtMensaje;
    private JComboBox cbDestinatario;
    private JFrame chatroom;

    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private static String destinatario;
    private static MessageProducer messageProducer;
    private static ObjectMessage objMessageSender;
    private Mensaje mensaje = new Mensaje();
    private HashMap<String, Usuario> directorioTotal;

    public RequestSender(JFrame frame, Usuario usr, JButton btnSol, JTextField txtSol, JButton btnEnv, JTextArea txtMens, JComboBox dest, HashMap directorioTotal){
        this.chatroom = frame;
        this.usuario = usr;
        this.btnSolicitud = btnSol;
        this.txtSolicitud = txtSol;
        this.btnEnviar = btnEnv;
        this.txtMensaje = txtMens;
        this.cbDestinatario = dest;
        this.directorioTotal = directorioTotal;

        chatroom.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        chatroom.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.out.println("Cierra cola publica");
                txtMensaje.setText("Good bye!");
                enviarSolicitud(-1);
                chatroom.dispose();
            }
        });

        btnSolicitud.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarSolicitud(1);
            }
        });
    }

    public void enviarSolicitud(int opcion){
        String destino;
        if(opcion>0)
            destino = txtSolicitud.getText();
        else
            destino = usuario.getNombre();
        if(!destino.equals("")) {
            try {
                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
                connectionFactory.setTrustAllPackages(true);
                Connection connection = connectionFactory.createConnection(); //Crea la conexion
                connection.start(); //Arranca la conexion

                Session session = connection.createSession(false /*Transacter*/, Session.AUTO_ACKNOWLEDGE); //Cuando consume un mensaje, automaticamente manda el acknowledgment

                Destination producer = session.createQueue(destino);
                messageProducer = session.createProducer(producer);
                objMessageSender = session.createObjectMessage();

                mensaje.setMensaje("Solicitud de amistad");
                mensaje.setClavePrivada(usuario.getClavePrivada());
                //mensaje.setTipo(2);
                mensaje.setTipo(2);
                System.out.println("Sending: solicitud a Destinatario: " + destino + "\n de: "+usuario.getNombre());
                objMessageSender.setObject(mensaje);
                messageProducer.send(objMessageSender);
                txtSolicitud.setText("");

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("No hay nadie quien agregar");
        }
    }

    @Override
    public void run() {

    }
}

