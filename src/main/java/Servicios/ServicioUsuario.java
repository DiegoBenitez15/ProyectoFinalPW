package Servicios;
import Clases.Usuario;

import java.util.List;

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

    public Usuario autenticar(String usuario, String password){
        List<Usuario> users = findAll();
        for (Usuario user : users) {
            if(user.getUsuario().equals(usuario) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }


}