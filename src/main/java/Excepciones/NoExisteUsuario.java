package Excepciones;

public class NoExisteUsuario extends RuntimeException{
    public NoExisteUsuario(String message) {
        super(message);
    }
}
