package erik.marconato.driver_allocation.exception.ControllerAdvice;

import erik.marconato.driver_allocation.exception.CpfExistsException;
import erik.marconato.driver_allocation.exception.NotFoundException;
import erik.marconato.driver_allocation.exception.PlateExistsException;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(PlateExistsException.class)
    public ResponseEntity<String> handlePlateExistsException (PlateExistsException plateExistsException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(plateExistsException.getMessage());
    }

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<String> handlePropertyValueException (PropertyValueException propertyValueException){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Por favor, preencha todos os campos.");
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundException (NotFoundException notFoundException){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(notFoundException.getMessage());
    }

    @ExceptionHandler(CpfExistsException.class)
    public ResponseEntity<String> cpfExistsException (CpfExistsException cpfExistsException){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cpfExistsException.getMessage());
    }
}
