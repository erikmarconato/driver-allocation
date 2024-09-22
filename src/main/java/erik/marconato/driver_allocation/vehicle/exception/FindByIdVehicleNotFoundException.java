package erik.marconato.driver_allocation.vehicle.exception;

public class FindByIdVehicleNotFoundException extends RuntimeException{
    public FindByIdVehicleNotFoundException (String message){
        super(message);
    }
}
