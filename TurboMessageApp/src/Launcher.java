import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class Launcher {

    static HashMap<String, Usuario> directorioTotal = new HashMap<String, Usuario>();
    static JFrame frame = new JFrame("Turbo-Message App");
    static JLabel nombre  = new JLabel();
    static JLabel iniciales = new JLabel();
    static JLabel numero = new JLabel();
    static JLabel titulo = new JLabel();

    //diccionario

    public static void main(String[] args) {

        //Crea la ventana de registro
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        titulo.setText("¡BIENVENIDO A LA TURBO-MESSAGE APP!");
        titulo.setBounds(120,10,300,25);
        frame.add(titulo);

        //Pide los datos al usuario desde la GUI
        nombre.setText("Ingresa tu nombre de usuario: ");
        nombre.setBounds(40,65,250,25);
        JTextField inputNombre = new JTextField();
        inputNombre.setBounds(250,65,200,25);
        frame.add(inputNombre);
        frame.add(nombre);

        iniciales.setText("Ingresa tu usuario: ");
        iniciales.setBounds(40,105,250,25);
        JTextField inputIniciales = new JTextField();
        inputIniciales.setBounds(250,105,200,25);
        frame.add(inputIniciales);
        frame.add(iniciales);


        //Registra al juagdor al hacer click en el boton y abre su ventana de chat personal
        JButton registrar = new JButton("Iniciar sesión / Registrar");
        registrar.setBounds(130,200,200,25);
        frame.add(registrar);
        registrar.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //Crea la ventana del chatroom personal
                JFrame frame2 =  new JFrame("Turbo-Message App");
                frame2.setSize(800, 500);
                frame2.setLocationRelativeTo(null);
                frame2.setLayout(null);
                frame2.setVisible(true);
                JLabel bienvenido = new JLabel("Turbo-Message App");
                bienvenido.setBounds(20,50,200,25);
                JLabel nomUsr = new JLabel("Nombre: " + inputNombre.getText());
                nomUsr.setBounds(20,90,200,25);
                JLabel status = new JLabel( "Status: Online");
                status.setBounds(20,120,100,25);
                frame2.add(bienvenido);
                frame2.add(nomUsr);
                frame2.add(status);
                JSeparator separator = new JSeparator();
                separator.setBounds(20,200,260,10);
                JSeparator separator2 = new JSeparator();
                separator2.setOrientation(SwingConstants.VERTICAL);
                separator2.setBounds(280,20,10,440);
                frame2.add(separator);
                frame2.add(separator2);


                //Cajas de rececpion/envio de mensajes
                JTextArea msgReciever = new JTextArea(100,1);
                msgReciever.setLineWrap(true);
                msgReciever.setEditable(false);
                JTextArea msgSender = new JTextArea(100,1);
                msgSender.setLineWrap(true);
                JScrollPane scroll1 = new JScrollPane(msgReciever);
                scroll1.setBounds(300, 50, 450,150);
                JScrollPane scroll2 = new JScrollPane(msgSender);
                scroll2.setBounds(300, 250, 450,60);
                frame2.add(scroll1);
                frame2.add(scroll2);

                //Seccion de agregar contacto nuevo
                JLabel agregar = new JLabel("Agrega un nuevo contacto");
                agregar.setBounds(20,250,200,25);
                frame2.add(agregar);
                JLabel lblNomNuevo = new JLabel("Nombre:");
                lblNomNuevo.setBounds(20,300,200,25);
                frame2.add(lblNomNuevo);
                JTextField txtNomNuevo = new JTextField();
                txtNomNuevo.setBounds(100,300,160,25);
                frame2.add(txtNomNuevo);
                JButton btnAgregar = new JButton("Enviar solicitud");
                btnAgregar.setBounds(80, 380, 150,25 );
                frame2.add(btnAgregar);

                //Seleccion de destinatario y envio
                JLabel lblDestinatario = new JLabel("Selecciona el destinatario:");
                lblDestinatario.setBounds(300, 330, 200,25);
                frame2.add(lblDestinatario);
                JComboBox cbDestinatario = new JComboBox();
                cbDestinatario.setBounds(480, 330, 200,25);
                cbDestinatario.addItem("SBR123");
                frame2.add(cbDestinatario);
                JButton btnEnviar = new JButton("Enviar mensaje");
                btnEnviar.setBounds(300, 380, 150,25);
                frame2.add(btnEnviar);

                //Crea al usuario al registrarlo e instancia 3 hilos:
                // Recepcion de solicitudes
                // Recepcion de mensajes
                // Envio de mensajes y solicitudes

                String clave = inputIniciales.getText();
                String nombre = inputNombre.getText();
                Usuario usuario = directorioTotal.get(clave);

                if(usuario == null){
                    usuario = new Usuario(nombre, clave);
                    directorioTotal.put(clave,usuario);
                }

                System.out.println(directorioTotal.values());

                //Hilo para la recepcion de solicitudes

                //Hilo para la recepcion de mensajes
                ChatReciever reciever = new ChatReciever(frame2, usuario, msgReciever, directorioTotal);
                reciever.start();
                RequestReciever rReciever = new RequestReciever(frame2, usuario, msgReciever,cbDestinatario,directorioTotal);
                rReciever.start();
                //Hilo para el envio de mensajes
                ChatSender sender = new ChatSender(frame2, usuario, btnAgregar, txtNomNuevo, btnEnviar, msgSender, cbDestinatario,directorioTotal);
                RequestSender rSender = new RequestSender(frame2, usuario, btnAgregar, txtNomNuevo, btnEnviar, msgSender, cbDestinatario, directorioTotal);
                //TODO:
                //Registrar usuario nuevo en archivo txt (BaseDatos)
                //Consultar usuarios existentes en archivo txt (BaseDatos)
                //  If usuario existe -> buscar sus contactos en archivo
                //  else -> crear su instancia en archivo sin contactos
            }
        });

        frame.setLayout(null);
        frame.setVisible(true);
    }

}
