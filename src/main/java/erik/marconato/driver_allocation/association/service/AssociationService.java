package erik.marconato.driver_allocation.association.service;

import erik.marconato.driver_allocation.association.dto.AssociationDto;
import erik.marconato.driver_allocation.association.entity.AssociationEntity;
import erik.marconato.driver_allocation.association.repository.AssociationRepository;
import erik.marconato.driver_allocation.driver.entity.DriverEntity;
import erik.marconato.driver_allocation.driver.repository.DriverRepository;
import erik.marconato.driver_allocation.exception.AssociationDriverAndVehicleExistsException;
import erik.marconato.driver_allocation.exception.AssociationExistsException;
import erik.marconato.driver_allocation.exception.NotFoundException;
import erik.marconato.driver_allocation.vehicle.entity.VehicleEntity;
import erik.marconato.driver_allocation.vehicle.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssociationService {

    @Autowired
    AssociationRepository associationRepository;

    @Autowired
    DriverRepository driverRepository;

    @Autowired
    VehicleRepository vehicleRepository;


    public ResponseEntity<AssociationDto> createAssociation (AssociationDto associationDto){

        AssociationEntity findByDriverId = associationRepository.findByDriverId(associationDto.driver());
        AssociationEntity findByVehicleId = associationRepository.findByVehicleId(associationDto.vehicle());

        if (findByDriverId != null || findByVehicleId != null){
            throw new AssociationExistsException("Motorista ou Veículo já associado no banco de dados. Por favor, verifique os IDs.");
        }

        Optional<DriverEntity> driver = Optional.ofNullable(driverRepository.findById(associationDto.driver())
                .orElseThrow(() -> new NotFoundException("Motorista não encontrado. Por favor, verifique se o ID está correto.")));
        Optional<VehicleEntity> vehicle = Optional.ofNullable(vehicleRepository.findById(associationDto.vehicle())
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado. Por favor, verifique se o ID está correto.")));

        DriverEntity driverEntityId = driver.get();
        VehicleEntity vehicleEntityId = vehicle.get();

        AssociationEntity associationEntity = new AssociationEntity(
                driverEntityId,
                vehicleEntityId
        );
        associationRepository.save(associationEntity);

        AssociationDto association = new AssociationDto(
                associationEntity.getId(),
                associationEntity.getDriver().getId(),
                associationEntity.getVehicle().getId()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(association);
    }

    public List<AssociationDto> findAllAssociations (){
        List<AssociationEntity> associationFindAll = associationRepository.findAll();

        if (associationFindAll.isEmpty()){
            throw new NotFoundException("Não há associações cadastradas");
        }

        return associationFindAll.stream().map(associationEntity -> new AssociationDto(
                associationEntity.getId(),
                associationEntity.getDriver().getId(),
                associationEntity.getVehicle().getId()
        )).collect(Collectors.toList());
    }

    public Optional<AssociationDto> findByIdAssociation (Long id){

        return Optional.ofNullable(associationRepository.findById(id).map(associationEntity ->
                new AssociationDto(
                        associationEntity.getId(),
                        associationEntity.getDriver().getId(),
                        associationEntity.getVehicle().getId()
                )).orElseThrow(() -> new NotFoundException("Associação não encontrada. Por favor, verifique se o ID está correto.")));
    }

    public String deleteAssociation (Long id){

        var associationExists = associationRepository.findById(id);

        if (associationExists.isEmpty()){
            throw new NotFoundException("Associação não encontrada. Por favor, verifique se o ID está correto.");
        }

        associationRepository.deleteById(id);

        return "Associação deletada com sucesso.";
    }

    public AssociationDto editAssociation (Long id, AssociationDto associationDto){

        Optional<AssociationEntity> associationExists = associationRepository.findById(id);

        if (associationExists.isEmpty()){
            throw new AssociationExistsException("Associação não encontrada. Por favor, verifique se o ID está correto.");
        }

        DriverEntity driverEntity = driverRepository.findById(associationDto.driver())
                .orElseThrow(() -> new NotFoundException("Motorista não encontrado. Por favor, verifique o ID."));
        VehicleEntity vehicleEntity = vehicleRepository.findById(associationDto.vehicle())
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado. Por favor, verifique o ID."));

        var driverExists = associationRepository.findByDriverId(associationDto.driver());
        var vehicleExists = associationRepository.findByVehicleId(associationDto.vehicle());

        if (driverExists != null || vehicleExists != null){
            throw new AssociationDriverAndVehicleExistsException("Motorista ou Veículo já associado no banco de dados. Delete a associação correspondente ou verifique os IDs.");
        }


        AssociationEntity association = associationExists.get();

        association.setDriver(driverEntity);
        association.setVehicle(vehicleEntity);

        associationRepository.save(association);

        return new AssociationDto(
                association.getId(),
                association.getDriver().getId(),
                association.getVehicle().getId()
        );
    }
}
