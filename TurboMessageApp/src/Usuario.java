import java.io.Serializable;

public class Usuario implements Serializable {

    private String nombre;
    private String clavePrivada;
    //private String pswd;
    //private String[] contactos;


    public Usuario(String nombre, String clavePrivada) {
        this.nombre = nombre;
        this.clavePrivada = clavePrivada;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getClavePrivada() {
        return clavePrivada;
    }

    public void setClavePrivada(String clavePrivada) {
        this.clavePrivada = clavePrivada;
    }


}
