package Excepciones;

public class YaExisteURL extends RuntimeException{
    public YaExisteURL(String message) {
        super(message);
    }
}
