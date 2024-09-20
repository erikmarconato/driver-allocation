package erik.marconato.driver_allocation.vehicle.controller;

import erik.marconato.driver_allocation.vehicle.exception.PlateExistsException;
import org.hibernate.PropertyValueException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandlerController {

    @ExceptionHandler(PlateExistsException.class)
    public ResponseEntity<String> handlePlateExistsException (PlateExistsException plateExistsException){
        return ResponseEntity.status(HttpStatus.CONFLICT).body(plateExistsException.getMessage());
    }

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<String> handlePropertyValueException (PropertyValueException propertyValueException){
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Por favor, preencha todos os campos.");
    }
}
