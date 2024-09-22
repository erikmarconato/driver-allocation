package erik.marconato.driver_allocation.vehicle.exception;

public class DeleteVehicleNotFoundException extends RuntimeException{
    public DeleteVehicleNotFoundException (String message){
        super(message);
    }
}
