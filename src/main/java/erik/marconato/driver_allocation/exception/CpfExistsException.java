package erik.marconato.driver_allocation.exception;

public class CpfExistsException extends RuntimeException{
    public CpfExistsException (String message){
        super(message);
    }
}
