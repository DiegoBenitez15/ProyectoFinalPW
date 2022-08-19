package Servicios;
import Clases.Usuario;

public class ServicioUsuario extends GestionDb<Usuario> {
    private static ServicioUsuario instancia;

    private ServicioUsuario(){
        super(Usuario.class);
    }

    public static ServicioUsuario getInstancia(){
        if(instancia==null){
            instancia = new ServicioUsuario();
        }
        return instancia;
    }
}