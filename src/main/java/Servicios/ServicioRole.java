package Servicios;
import Clases.Roles;
import Clases.Usuario;

public class ServicioRole extends GestionDb<Roles> {
    private static ServicioRole instancia;

    private ServicioRole(){
        super(Roles.class);
    }

    public static ServicioRole getInstancia(){
        if(instancia==null){
            instancia = new ServicioRole();
        }
        return instancia;
    }
}