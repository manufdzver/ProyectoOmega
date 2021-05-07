import java.io.Serializable;

public class Mensaje implements Serializable {
    private int tipo;
    private String mensaje;
    private String clavePrivada;

    public Mensaje(){
    }

    public Mensaje(int tipo, String mensaje, String clavePrivada) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.clavePrivada = clavePrivada;
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

    public String getClavePrivada() {
        return clavePrivada;
    }

    public void setClavePrivada(String clavePrivada) {
        this.clavePrivada = clavePrivada;
    }
}
