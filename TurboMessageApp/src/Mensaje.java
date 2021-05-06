import java.io.Serializable;

public class Mensaje implements Serializable {
    private int tipo;
    private String mensaje;
    private Usuario autor;

    public Mensaje(){
    }

    public Mensaje(int tipo, String mensaje, Usuario autor) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.autor = autor;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Usuario getAutor() {
        return autor;
    }

    public void setAutor(Usuario autor) {
        this.autor = autor;
    }
}
