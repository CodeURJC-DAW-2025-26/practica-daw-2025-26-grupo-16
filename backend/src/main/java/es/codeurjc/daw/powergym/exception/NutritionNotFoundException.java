package es.codeurjc.daw.powergym.exception;


public class NutritionNotFoundException extends RuntimeException {

    private static final String message = "Nutrition not found";
    
    public NutritionNotFoundException() {
        super(message);
    }
}