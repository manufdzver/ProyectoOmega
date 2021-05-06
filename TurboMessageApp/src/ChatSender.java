import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatSender extends Thread{
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

    public ChatSender(JFrame frame, Usuario usr, JButton btnSol, JTextField txtSol, JButton btnEnv, JTextArea txtMens, JComboBox dest){
        this.chatroom = frame;
        this.usuario = usr;
        this.btnSolicitud = btnSol;
        this.txtSolicitud = txtSol;
        this.btnEnviar = btnEnv;
        this.txtMensaje = txtMens;
        this.cbDestinatario = dest;
        destinatario = dest.getSelectedItem().toString();

        chatroom.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        chatroom.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(chatroom,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION){

                    System.out.println("Ya termino ");
                    chatroom.dispose();
                    txtMensaje.setText("Good bye!");
                    enviarMensaje();
                }
            }
        });

        btnSolicitud.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarSolicitud();
            }
        });
        btnEnviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enviarMensaje();
            }
        });
    }

    public void enviarSolicitud(){

    }

    public void enviarMensaje(){
        String texto = txtMensaje.getText();
        if(!texto.equals("")) {
            try {

                ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
                connectionFactory.setTrustAllPackages(true);
                Connection connection = connectionFactory.createConnection(); //Crea la conexion
                connection.start(); //Arranca la conexion

                Session session = connection.createSession(false /*Transacter*/, Session.AUTO_ACKNOWLEDGE); //Cuando consume un mensaje, automaticamente manda el acknowledgment

                Destination producer = session.createQueue(destinatario);
                messageProducer = session.createProducer(producer);
                objMessageSender = session.createObjectMessage();

                mensaje.setMensaje(texto);
                mensaje.setAutor(usuario);
                mensaje.setTipo(1);
                System.out.println("Sending: " + texto + ", Destinatario: " + destinatario + "\n");
                objMessageSender.setObject(mensaje);
                messageProducer.send(objMessageSender);
                txtMensaje.setText("");

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("No hay nada escrito");
        }
    }

    @Override
    public void run() {

    }
}
