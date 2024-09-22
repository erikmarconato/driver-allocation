package erik.marconato.driver_allocation.vehicle.controller;

import erik.marconato.driver_allocation.vehicle.dto.VehicleDto;
import erik.marconato.driver_allocation.vehicle.entity.VehicleEntity;
import erik.marconato.driver_allocation.vehicle.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/veiculos")
@CrossOrigin
public class VehicleController {

    @Autowired
    VehicleService vehicleService;

    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle (@RequestBody VehicleDto vehicle){
        return vehicleService.createVehicle(vehicle);
    }

    @GetMapping
    public List<VehicleDto> findAllVehicles (){
        return vehicleService.findAllVehicles();
    }

    @GetMapping("/{id}")
    public Optional<VehicleDto> findByIdVehicle (@PathVariable Long id){
        return vehicleService.findByIdVehicle(id);
    }
}
