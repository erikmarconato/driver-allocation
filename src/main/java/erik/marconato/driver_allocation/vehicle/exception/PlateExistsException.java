package erik.marconato.driver_allocation.vehicle.exception;

public class PlateExistsException extends RuntimeException{
    public PlateExistsException(String message){
        super(message);
    }
}