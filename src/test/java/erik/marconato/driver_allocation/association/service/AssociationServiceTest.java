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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AssociationServiceTest {

    @Mock
    AssociationRepository associationRepository;

    @Mock
    DriverRepository driverRepository;

    @Mock
    VehicleRepository vehicleRepository;

    @InjectMocks
    AssociationService associationService;

    @Test
    @DisplayName("Should create an association successfully")
    void createAssociationSuccess() {
        DriverEntity driverEntity = new DriverEntity(1L, "NomeMotorista", "CPFMotorista", "CNHMotorista");
        VehicleEntity vehicleEntity = new VehicleEntity(1L, "PlacaVeículo", "ModeloVeículo", 2024);
        AssociationDto associationDto = new AssociationDto(1L, driverEntity.getId(), vehicleEntity.getId());
        ArgumentCaptor<AssociationEntity> argumentCaptor = ArgumentCaptor.forClass(AssociationEntity.class);

        Mockito.when(associationRepository.findByDriverId(associationDto.driver())).thenReturn(null);
        Mockito.when(associationRepository.findByVehicleId(associationDto.vehicle())).thenReturn(null);

        Mockito.when(driverRepository.findById(associationDto.driver())).thenReturn(Optional.of(driverEntity));
        Mockito.when(vehicleRepository.findById(associationDto.vehicle())).thenReturn(Optional.of(vehicleEntity));

        ResponseEntity<AssociationDto> response = associationService.createAssociation(associationDto);

        Mockito.verify(associationRepository, Mockito.times(1)).save(argumentCaptor.capture());

        AssociationEntity savedAssociation = argumentCaptor.getValue();

        Assertions.assertEquals(driverEntity, savedAssociation.getDriver());
        Assertions.assertEquals(vehicleEntity, savedAssociation.getVehicle());

        AssociationDto responseBody = Objects.requireNonNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(driverEntity.getId(), responseBody.driver());
        Assertions.assertEquals(vehicleEntity.getId(), responseBody.vehicle());
    }

    @Test
    @DisplayName("Should throw NotFoundException if the driver was not found in the database")
    void createAssociationNotFoundExceptionDriver(){
        Long driverDoesNotExist = 99L;
        VehicleEntity vehicleEntity = new VehicleEntity(1L, "PlacaVeículo", "ModeloVeículo", 2024);
        AssociationDto associationDto = new AssociationDto(1L, driverDoesNotExist, vehicleEntity.getId());

        Mockito.when(associationRepository.findByDriverId(associationDto.driver())).thenReturn(null);
        Mockito.when(associationRepository.findByVehicleId(associationDto.vehicle())).thenReturn(null);

        Mockito.when(driverRepository.findById(associationDto.driver())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> associationService.createAssociation(associationDto),
                "Deve lançar a exceção (NotFoundException) se o Motorista não for encontrado na base de dados.");

        Mockito.verify(associationRepository, Mockito.never()).save(Mockito.any(AssociationEntity.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException if the vehicle was not found in the database")
    void createAssociationNotFoundExceptionVehicle(){
        Long vehicleDoesNotExist = 99L;
        DriverEntity driverEntity = new DriverEntity(1L, "NomeMotorista", "CPFMotorista", "CNHMotorista");
        AssociationDto associationDto = new AssociationDto(1L, driverEntity.getId(), vehicleDoesNotExist);

        Mockito.when(associationRepository.findByDriverId(associationDto.driver())).thenReturn(null);
        Mockito.when(associationRepository.findByVehicleId(associationDto.vehicle())).thenReturn(null);

        Mockito.when(driverRepository.findById(associationDto.driver())).thenReturn(Optional.of(driverEntity));
        Mockito.when(vehicleRepository.findById(associationDto.vehicle())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> associationService.createAssociation(associationDto),
                "Deve lançar a exceção (NotFoundException) se o Veículo não for encontrado na base de dados.");

        Mockito.verify(associationRepository, Mockito.never()).save(Mockito.any(AssociationEntity.class));
    }

    @Test
    @DisplayName("Should throw AssociationExistsException if there is already an association created with the driver or vehicle in the database")
    void createAssociationAssociationExistsException(){
        DriverEntity driverEntity = new DriverEntity(1L, "NomeMotorista", "CPFMotorista", "CNHMotorista");
        VehicleEntity vehicleEntity = new VehicleEntity(1L, "PlacaVeículo", "ModeloVeículo", 2024);
        AssociationDto associationDto = new AssociationDto(1L, driverEntity.getId(), vehicleEntity.getId());

        Mockito.when(associationRepository.findByDriverId(associationDto.driver())).thenReturn(new AssociationEntity());
        Mockito.when(associationRepository.findByVehicleId(associationDto.vehicle())).thenReturn(new AssociationEntity());

        Assertions.assertThrows(AssociationExistsException.class, () -> associationService.createAssociation(associationDto),
                "Deve lançar a exceção (AssociationExistsException) se já houver associação de motorista ou veículo no banco de dados");

        Mockito.verify(associationRepository, Mockito.never()).save(Mockito.any(AssociationEntity.class));
    }

    @Test
    @DisplayName("Should list all associations successfully")
    void findAllAssociationsSuccess(){
        DriverEntity driverEntity = new DriverEntity(1L, "NomeMotorista", "CPFMotorista", "CNHMotorista");
        VehicleEntity vehicleEntity = new VehicleEntity(1L, "PlacaVeículo", "ModeloVeículo", 2024);
        AssociationEntity associationEntity = new AssociationEntity(1L, driverEntity, vehicleEntity);

        List<AssociationEntity> associationList = new ArrayList<>();
        associationList.add(associationEntity);

        Mockito.when(associationRepository.findAll()).thenReturn(associationList);

        List<AssociationDto> response = associationService.findAllAssociations();

        Mockito.verify(associationRepository, Mockito.times(1)).findAll();

        Assertions.assertFalse(associationList.isEmpty());
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(associationEntity.getId(), response.get(0).id());
        Assertions.assertEquals(associationEntity.getDriver().getId(), response.get(0).driver());
        Assertions.assertEquals(associationEntity.getVehicle().getId(), response.get(0).vehicle());
    }

    @Test
    @DisplayName("Should throw NotFoundException if the list of associations is empty")
    void findAllAssociationsNotFoundException(){
        List<AssociationEntity> associationList = new ArrayList<>();

        Mockito.when(associationRepository.findAll()).thenReturn(associationList);

        Assertions.assertThrows(NotFoundException.class, () -> associationService.findAllAssociations(),
                "Deve lançar (NotFoundException) caso não existir nenhuma associação cadastrada.");

        Mockito.verify(associationRepository, Mockito.times(1)).findAll();

        Assertions.assertTrue(associationList.isEmpty());
    }

    @Test
    @DisplayName("Should return association by id successfully")
    void findByIdAssociationSuccess(){
        Long validId = 1L;
        DriverEntity driverEntity = new DriverEntity(1L, "NomeMotorista", "CPFMotorista", "CNHMotorista");
        VehicleEntity vehicleEntity = new VehicleEntity(1L, "PlacaVeículo", "ModeloVeículo", 2024);
        AssociationEntity associationEntity = new AssociationEntity(validId, driverEntity, vehicleEntity);

        Mockito.when(associationRepository.findById(validId)).thenReturn(Optional.of(associationEntity));

        Optional<AssociationDto> response = associationService.findByIdAssociation(validId);
        Mockito.verify(associationRepository, Mockito.times(1)).findById(Mockito.anyLong());

        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(validId, response.get().id());
        Assertions.assertEquals(driverEntity.getId(), response.get().driver());
        Assertions.assertEquals(vehicleEntity.getId(), response.get().vehicle());
    }

    @Test
    @DisplayName("Should throw NotFoundException if the Association ID is not found in the database")
    void findByIdAssociationNotFoundException(){
        Long invalidId = 99L;

        Mockito.when(associationRepository.findById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> associationService.findByIdAssociation(invalidId),
                "Deve lançar a exceção (NotFoundException) caso o ID da Associação não seja encontrado no banco de dados.");

        Mockito.verify(associationRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Should delete the association successfully")
    void deleteAssociationSuccess(){
        Long validId = 1L;
        DriverEntity driverEntity = new DriverEntity(1L, "NomeMotorista", "CPFMotorista", "CNHMotorista");
        VehicleEntity vehicleEntity = new VehicleEntity(1L, "PlacaVeículo", "ModeloVeículo", 2024);
        AssociationEntity associationEntity = new AssociationEntity(validId, driverEntity, vehicleEntity);

        Mockito.when(associationRepository.findById(validId)).thenReturn(Optional.of(associationEntity));

        String response = associationService.deleteAssociation(validId);

        Mockito.verify(associationRepository, Mockito.times(1)).deleteById(Mockito.anyLong());

        Assertions.assertEquals("Associação deletada com sucesso.", response);
    }

    @Test
    @DisplayName("Should throw NotFoundException if the association ID is not found to delete an association")
    void deleteAssociationNotFoundException(){
        Long invalidId = 99L;

        Mockito.when(associationRepository.findById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> associationService.deleteAssociation(invalidId),
                "Deve lançar a exceção (NotFoundException) se a associação não for encontrada no banco de dados para ser deletada.");

        Mockito.verify(associationRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Should edit an association successfully")
    void editAssociationSuccess(){
        Long validId = 1L;
        DriverEntity oldDriver = new DriverEntity(1L, "NomeMotoristaVelho", "CPFMotoristaVelho", "CNHMotoristaVelho");
        VehicleEntity oldVehicle = new VehicleEntity(1L, "PlacaVeículoVelho", "ModeloVeículoVelho", 2024);
        AssociationEntity oldAssociation = new AssociationEntity();
        oldAssociation.setId(validId);
        oldAssociation.setDriver(oldDriver);
        oldAssociation.setVehicle(oldVehicle);

        DriverEntity newDriver = new DriverEntity(2L, "NomeMotoristaNovo", "CPFMotoristaNovo", "CNHMotoristaNovo");
        VehicleEntity newVehicle = new VehicleEntity(2L, "PlacaVeículoNovo", "ModeloVeículoNovo", 2025);
        AssociationDto associationEdit = new AssociationDto(validId, newDriver.getId(), newVehicle.getId());

        ArgumentCaptor<AssociationEntity> argumentCaptor = ArgumentCaptor.forClass(AssociationEntity.class);

        Mockito.when(associationRepository.findById(validId)).thenReturn(Optional.of(oldAssociation));

        Mockito.when(driverRepository.findById(associationEdit.driver())).thenReturn(Optional.of(newDriver));
        Mockito.when(vehicleRepository.findById(associationEdit.vehicle())).thenReturn(Optional.of(newVehicle));

        Mockito.when(associationRepository.findByDriverId(associationEdit.driver())).thenReturn(null);
        Mockito.when(associationRepository.findByVehicleId(associationEdit.vehicle())).thenReturn(null);

        AssociationDto response = associationService.editAssociation(validId, associationEdit);

        Mockito.verify(associationRepository, Mockito.times(1)).save(argumentCaptor.capture());

        AssociationEntity savedAssociation = argumentCaptor.getValue();

        Assertions.assertEquals(newDriver.getId(), savedAssociation.getDriver().getId());
        Assertions.assertEquals(newVehicle.getId(), savedAssociation.getVehicle().getId());

        Assertions.assertEquals(validId, response.id());
        Assertions.assertEquals(newDriver.getId(), response.driver());
        Assertions.assertEquals(newVehicle.getId(), response.vehicle());
    }

    @Test
    @DisplayName("Should throw AssociationExistsException if association ID is not found")
    void editAssociationExistsException(){
        Long invalidId = 99L;
        DriverEntity newDriver = new DriverEntity(2L, "NomeMotoristaNovo", "CPFMotoristaNovo", "CNHMotoristaNovo");
        VehicleEntity newVehicle = new VehicleEntity(2L, "PlacaVeículoNovo", "ModeloVeículoNovo", 2025);
        AssociationDto associationEdit = new AssociationDto(invalidId, newDriver.getId(), newVehicle.getId());

        Mockito.when(associationRepository.findById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(AssociationExistsException.class, () -> associationService.editAssociation(invalidId, associationEdit),
                "Deve lançar a exceção (AssociationExistsException) caso a associação não seja encontrada no banco de dados.");

        Mockito.verify(associationRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(driverRepository, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(vehicleRepository, Mockito.never()).findById(Mockito.anyLong());
        Mockito.verify(associationRepository, Mockito.never()).findByDriverId(Mockito.anyLong());
        Mockito.verify(associationRepository, Mockito.never()).findByVehicleId(Mockito.anyLong());
        Mockito.verify(associationRepository, Mockito.never()).save(Mockito.any(AssociationEntity.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException if driver is not found in database")
    void editAssociationNotFoundExceptionDriver() {
        Long validId = 1L;
        DriverEntity oldDriver = new DriverEntity(1L, "NomeMotoristaVelho", "CPFMotoristaVelho", "CNHMotoristaVelho");
        VehicleEntity oldVehicle = new VehicleEntity(1L, "PlacaVeículoVelho", "ModeloVeículoVelho", 2024);
        AssociationEntity oldAssociation = new AssociationEntity();
        oldAssociation.setId(validId);
        oldAssociation.setDriver(oldDriver);
        oldAssociation.setVehicle(oldVehicle);

        Long driverDoesNotExists = 99L;
        Long vehicleDoesNotExists = 99L;
        AssociationDto associationEdit = new AssociationDto(validId, driverDoesNotExists, vehicleDoesNotExists);

        Mockito.when(associationRepository.findById(validId)).thenReturn(Optional.of(oldAssociation));

        Mockito.when(driverRepository.findById(associationEdit.driver())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> associationService.editAssociation(validId, associationEdit),
                "Deve lançar a exceção (NotFoundException) caso o motorista não seja encontrado no banco de dados para uma associação ser editada.");

        Mockito.verify(associationRepository, Mockito.never()).findByDriverId(Mockito.anyLong());
        Mockito.verify(associationRepository, Mockito.never()).findByVehicleId(Mockito.anyLong());
        Mockito.verify(associationRepository, Mockito.never()).save(Mockito.any(AssociationEntity.class));
    }

    @Test
    @DisplayName("Should throw NotFoundException if the vehicle is not found in the database")
    void editAssociationNotFoundExceptionVehicle() {
        Long validId = 1L;
        DriverEntity oldDriver = new DriverEntity(1L, "NomeMotoristaVelho", "CPFMotoristaVelho", "CNHMotoristaVelho");
        VehicleEntity oldVehicle = new VehicleEntity(1L, "PlacaVeículoVelho", "ModeloVeículoVelho", 2024);
        AssociationEntity oldAssociation = new AssociationEntity();
        oldAssociation.setId(validId);
        oldAssociation.setDriver(oldDriver);
        oldAssociation.setVehicle(oldVehicle);

        DriverEntity newDriver = new DriverEntity(2L, "NomeMotoristaNovo", "CPFMotoristaNovo", "CNHMotoristaNovo");
        Long vehicleDoesNotExists = 99L;
        AssociationDto associationEdit = new AssociationDto(validId, newDriver.getId(), vehicleDoesNotExists);

        Mockito.when(associationRepository.findById(validId)).thenReturn(Optional.of(oldAssociation));

        Mockito.when(driverRepository.findById(associationEdit.driver())).thenReturn(Optional.of(newDriver));
        Mockito.when(vehicleRepository.findById(associationEdit.vehicle())).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> associationService.editAssociation(validId, associationEdit),
                "Deve lançar a exceção (NotFoundException) caso o veículo não seja encontrado no banco de dados para uma associação ser editada.");

        Mockito.verify(associationRepository, Mockito.never()).findByDriverId(Mockito.anyLong());
        Mockito.verify(associationRepository, Mockito.never()).findByVehicleId(Mockito.anyLong());
        Mockito.verify(associationRepository, Mockito.never()).save(Mockito.any(AssociationEntity.class));
    }

    @Test
    @DisplayName("Should throw AssociationDriverAndVehicleExistsException if an association already exists in the database with the driver ID.")
    void editAssociationAssociationDriverAndVehicleExistsExceptionDriver() {
        Long validId = 1L;
        DriverEntity oldDriver = new DriverEntity(1L, "NomeMotoristaVelho", "CPFMotoristaVelho", "CNHMotoristaVelho");
        VehicleEntity oldVehicle = new VehicleEntity(1L, "PlacaVeículoVelho", "ModeloVeículoVelho", 2024);
        AssociationEntity oldAssociation = new AssociationEntity();
        oldAssociation.setId(validId);
        oldAssociation.setDriver(oldDriver);
        oldAssociation.setVehicle(oldVehicle);

        DriverEntity newDriver = new DriverEntity(2L, "NomeMotoristaNovo", "CPFMotoristaNovo", "CNHMotoristaNovo");
        VehicleEntity newVehicle = new VehicleEntity(2L, "PlacaVeículoNovo", "ModeloVeículoNovo", 2025);
        AssociationDto associationEdit = new AssociationDto(validId, newDriver.getId(), newVehicle.getId());

        Mockito.when(associationRepository.findById(validId)).thenReturn(Optional.of(oldAssociation));

        Mockito.when(driverRepository.findById(associationEdit.driver())).thenReturn(Optional.of(newDriver));
        Mockito.when(vehicleRepository.findById(associationEdit.vehicle())).thenReturn(Optional.of(newVehicle));

        Mockito.when(associationRepository.findByDriverId(associationEdit.driver())).thenReturn(new AssociationEntity());

        Assertions.assertThrows(AssociationDriverAndVehicleExistsException.class, () -> associationService.editAssociation(validId, associationEdit),
                "Deve lançar a exceção (AssociationDriverAndVehicleExistsException) caso já exista associação existente no banco de dados com o ID do motorista informado.");

        Mockito.verify(associationRepository, Mockito.never()).save(Mockito.any(AssociationEntity.class));
    }

    @Test
    @DisplayName("Should throw AssociationDriverAndVehicleExistsException if an association already exists in the database with the vehicle ID.")
    void editAssociationAssociationDriverAndVehicleExistsExceptionVehicle() {
        Long validId = 1L;
        DriverEntity oldDriver = new DriverEntity(1L, "NomeMotoristaVelho", "CPFMotoristaVelho", "CNHMotoristaVelho");
        VehicleEntity oldVehicle = new VehicleEntity(1L, "PlacaVeículoVelho", "ModeloVeículoVelho", 2024);
        AssociationEntity oldAssociation = new AssociationEntity();
        oldAssociation.setId(validId);
        oldAssociation.setDriver(oldDriver);
        oldAssociation.setVehicle(oldVehicle);

        DriverEntity newDriver = new DriverEntity(2L, "NomeMotoristaNovo", "CPFMotoristaNovo", "CNHMotoristaNovo");
        VehicleEntity newVehicle = new VehicleEntity(2L, "PlacaVeículoNovo", "ModeloVeículoNovo", 2025);
        AssociationDto associationEdit = new AssociationDto(validId, newDriver.getId(), newVehicle.getId());

        Mockito.when(associationRepository.findById(validId)).thenReturn(Optional.of(oldAssociation));

        Mockito.when(driverRepository.findById(associationEdit.driver())).thenReturn(Optional.of(newDriver));
        Mockito.when(vehicleRepository.findById(associationEdit.vehicle())).thenReturn(Optional.of(newVehicle));

        Mockito.when(associationRepository.findByDriverId(associationEdit.driver())).thenReturn(null);
        Mockito.when(associationRepository.findByVehicleId(associationEdit.vehicle())).thenReturn(new AssociationEntity());

        Assertions.assertThrows(AssociationDriverAndVehicleExistsException.class, () -> associationService.editAssociation(validId, associationEdit),
                "Deve lançar a exceção (AssociationDriverAndVehicleExistsException) caso já exista associação existente no banco de dados com o ID do veículo informado.");

        Mockito.verify(associationRepository, Mockito.never()).save(Mockito.any(AssociationEntity.class));
    }
}