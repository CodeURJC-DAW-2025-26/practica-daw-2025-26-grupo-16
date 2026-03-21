package es.codeurjc.daw.powergym.exception;


public class NotFoundException extends RuntimeException {

    private static final String message = "Entity not found";
    
    public NotFoundException() {
        super(message);
    }
}