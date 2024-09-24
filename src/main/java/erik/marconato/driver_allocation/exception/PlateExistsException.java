package erik.marconato.driver_allocation.exception;

public class PlateExistsException extends RuntimeException{
    public PlateExistsException(String message){
        super(message);
    }
}