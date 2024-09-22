package erik.marconato.driver_allocation.vehicle.service;

import erik.marconato.driver_allocation.vehicle.dto.VehicleDto;
import erik.marconato.driver_allocation.vehicle.entity.VehicleEntity;
import erik.marconato.driver_allocation.vehicle.exception.FindAllVehiclesIsEmptyException;
import erik.marconato.driver_allocation.vehicle.exception.FindByIdVehicleNotFoundException;
import erik.marconato.driver_allocation.vehicle.exception.PlateExistsException;
import erik.marconato.driver_allocation.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    public ResponseEntity createVehicle (VehicleDto vehicle) {

        var plateExists = vehicleRepository.findByPlate(vehicle.plate());

        if (plateExists != null){
            throw new PlateExistsException("Placa já está em uso no sistema. Por favor, escolha uma placa diferente.");
        }

            VehicleEntity vehicleEntity = new VehicleEntity(vehicle);

            vehicleRepository.save(vehicleEntity);

            VehicleDto vehicleDto = new VehicleDto(
                    vehicleEntity.getId(),
                    vehicleEntity.getPlate(),
                    vehicleEntity.getModel(),
                    vehicleEntity.getYear()
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(vehicleDto);
    }

    public List<VehicleDto> findAllVehicles (){
        List<VehicleEntity> getAllVehicles = vehicleRepository.findAll();

        if (getAllVehicles.isEmpty()){
            throw new FindAllVehiclesIsEmptyException("Não há veículos cadastrados.");
        }

        return  getAllVehicles.stream().map(vehicleEntity ->
                new VehicleDto(
                vehicleEntity.getId(),
                vehicleEntity.getPlate(),
                vehicleEntity.getModel(),
                vehicleEntity.getYear()
        )).collect(Collectors.toList());
    }

    public Optional<VehicleDto> findByIdVehicle (Long id){


        return Optional.ofNullable(vehicleRepository.findById(id).map(vehicle ->
                        new VehicleDto(
                                vehicle.getId(),
                                vehicle.getPlate(),
                                vehicle.getModel(),
                                vehicle.getYear()
                        ))
                .orElseThrow(() -> new FindByIdVehicleNotFoundException("Id não encontrado.")));
    }
}
