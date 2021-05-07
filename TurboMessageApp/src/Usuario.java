import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Usuario implements Serializable {

    private String nombre;
    private String clavePrivada;
    private HashMap<Usuario, String> miDirectorio;
    //private String[] contactos;


    public Usuario(String nombre, String clavePrivada) {
        this.nombre = nombre;
        this.clavePrivada = clavePrivada;
        this.miDirectorio = new HashMap<Usuario, String>();
    }

    public HashMap<Usuario, String> getMiDirectorio() {
        return miDirectorio;
    }

    public void setMiDirectorio(HashMap<Usuario, String> miDirectorio) {
        this.miDirectorio = miDirectorio;
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

    public void addDirectorio(Usuario usuario){
        miDirectorio.put(usuario, usuario.getClavePrivada());
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", clavePrivada='" + clavePrivada + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return clavePrivada.equals(usuario.clavePrivada);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clavePrivada);
    }
}
