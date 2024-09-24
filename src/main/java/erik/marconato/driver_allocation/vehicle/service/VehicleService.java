package erik.marconato.driver_allocation.vehicle.service;

import erik.marconato.driver_allocation.vehicle.dto.VehicleDto;
import erik.marconato.driver_allocation.vehicle.entity.VehicleEntity;
import erik.marconato.driver_allocation.exception.NotFoundException;
import erik.marconato.driver_allocation.exception.PlateExistsException;
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

    public ResponseEntity<VehicleDto> createVehicle (VehicleDto vehicle) {

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
            throw new NotFoundException("Não há veículos cadastrados.");
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
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado com o ID em questão. Verifique o ID.")));
    }

    public String deleteVehicle (Long id){

        var vehicleExists = vehicleRepository.findById(id);

        if (vehicleExists.isPresent()){
            vehicleRepository.deleteById(id);
            return "Veículo deletado com sucesso.";
        }
        throw new NotFoundException("Veículo não encontrado. Por favor, verifique se o ID está correto.");
    }

    public VehicleDto editVehicle (Long id, VehicleDto vehicleDto){

        var vehicleExists = vehicleRepository.findById(id);

        VehicleEntity plateExists = (VehicleEntity) vehicleRepository.findByPlate(vehicleDto.plate());

        if (vehicleExists.isPresent()){

            if (plateExists != null && !plateExists.getId().equals(id)){
                throw new PlateExistsException("Placa já está em uso no sistema. Por favor, escolha uma placa diferente.");
            }

            VehicleEntity vehicleEntity = vehicleExists.get();

            vehicleEntity.setPlate(vehicleDto.plate());
            vehicleEntity.setModel(vehicleDto.model());
            vehicleEntity.setYear(vehicleDto.year());

            vehicleRepository.save(vehicleEntity);

            return new VehicleDto(
                    vehicleEntity.getId(),
                    vehicleEntity.getPlate(),
                    vehicleEntity.getModel(),
                    vehicleEntity.getYear()
            );
        }

        throw new NotFoundException("Veículo não encontrado. Por favor, verifique se o ID está correto.");
    }
}
