package erik.marconato.driver_allocation.exception;

public class AssociationDriverAndVehicleExistsException extends RuntimeException{
    public AssociationDriverAndVehicleExistsException (String message){
        super(message);
    }
}
