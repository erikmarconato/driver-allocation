package erik.marconato.driver_allocation.vehicle.service;

import erik.marconato.driver_allocation.exception.NotFoundException;
import erik.marconato.driver_allocation.exception.PlateExistsException;
import erik.marconato.driver_allocation.vehicle.dto.VehicleDto;
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
class VehicleServiceTest {

    @Mock
    VehicleRepository vehicleRepository;

    @InjectMocks
    VehicleService vehicleService;

    @Test
    @DisplayName("Should be successful in creating vehicle")
    void createVehicleSuccess() {

        VehicleDto vehicleDto = new VehicleDto(1L, "ABC-123", "Escolar", 2024);

        Mockito.when(vehicleRepository.findByPlate(vehicleDto.plate())).thenReturn(null);

        ResponseEntity <VehicleDto> response = vehicleService.createVehicle(vehicleDto);
        ArgumentCaptor<VehicleEntity> argumentCaptor = ArgumentCaptor.forClass(VehicleEntity.class);

        Mockito.verify(vehicleRepository, Mockito.times(1)).save(argumentCaptor.capture());

        VehicleEntity savedVehicle = argumentCaptor.getValue();

        Assertions.assertEquals(vehicleDto.plate(), savedVehicle.getPlate());
        Assertions.assertEquals(vehicleDto.model(), savedVehicle.getModel());
        Assertions.assertEquals(vehicleDto.year(), savedVehicle.getYear());

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());

        VehicleDto responseBody = Objects.requireNonNull(response.getBody());
        Assertions.assertEquals(vehicleDto.plate(), responseBody.plate());
        Assertions.assertEquals(vehicleDto.model(), responseBody.model());
        Assertions.assertEquals(vehicleDto.year(), responseBody.year());
    }

    @Test
    @DisplayName("Should be PlateExistsException thrown if the plate is already registered in the system")
    void createVehiclePlateExistsException(){
        VehicleDto vehicleDto = new VehicleDto(1L, "ABC-123", "Escolar", 2024);

        Mockito.when(vehicleRepository.findByPlate(vehicleDto.plate())).thenReturn(new VehicleEntity());

        Assertions.assertThrows(PlateExistsException.class, () -> vehicleService.createVehicle(vehicleDto),
                "Deve ser lançada a exceção (PlateExistsException) caso a placa já esteja cadastrada no sistema.");

        Mockito.verify(vehicleRepository, Mockito.never()).save(Mockito.any(VehicleEntity.class));
    }

    @Test
    @DisplayName("Should list all vehicles successfully")
    void findAllVehiclesSuccess (){
        VehicleEntity vehicleEntity = new VehicleEntity(1L, "ABC-123", "Escolar", 2024);

        List <VehicleEntity> vehicleList= new ArrayList<>();
        vehicleList.add(vehicleEntity);

        Mockito.when(vehicleRepository.findAll()).thenReturn(vehicleList);

        List<VehicleDto> response = vehicleService.findAllVehicles();

        Mockito.verify(vehicleRepository, Mockito.times(1)).findAll();

        Assertions.assertFalse(vehicleList.isEmpty());
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(1, response.size());

        Assertions.assertEquals(1L, response.get(0).id());
        Assertions.assertEquals("ABC-123", response.get(0).plate());
        Assertions.assertEquals("Escolar", response.get(0).model());
        Assertions.assertEquals(2024, response.get(0).year());
    }

    @Test
    @DisplayName("Should be NotFoundException  thrown if the list is empty")
    void findAllVehiclesNotFoundException (){
        List <VehicleEntity> vehicleList= new ArrayList<>();

        Mockito.when(vehicleRepository.findAll()).thenReturn(vehicleList);

        Assertions.assertThrows(NotFoundException.class, () -> vehicleService.findAllVehicles(),
                "Deve ser lançada a exceção (NotFoundException) se a lista estiver vazia.");
    }

    @Test
    @DisplayName("Should show the vehicle by ID")
    void findByIdVehicleSuccess (){
        VehicleEntity vehicleEntity = new VehicleEntity(1L, "ABC-123", "Escolar", 2024);

        Mockito.when(vehicleRepository.findById(vehicleEntity.getId())).thenReturn(Optional.of(vehicleEntity));

        Optional<VehicleDto> response = vehicleService.findByIdVehicle(vehicleEntity.getId());

        Assertions.assertEquals(1L, response.get().id());
        Assertions.assertEquals("ABC-123", response.get().plate());
        Assertions.assertEquals("Escolar", response.get().model());
        Assertions.assertEquals(2024, response.get().year());
    }

    @Test
    @DisplayName("Should return NotFoundException if the ID is not found")
    void findByIdVehicleNotFoundException (){
        Long idInvalid = 99L;
        Mockito.when(vehicleRepository.findById(idInvalid)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> vehicleService.findByIdVehicle(idInvalid),
                "Deve ser lançada a exceção (NotFoundException) se o ID não for encontrado.");
    }

    @Test
    @DisplayName("Should delete vehicle success")
    void deleteVehicleSuccess (){
        VehicleEntity vehicleEntity = new VehicleEntity(1L, "ABC-123", "Escolar", 2024);

        Mockito.when(vehicleRepository.findById(vehicleEntity.getId())).thenReturn(Optional.of(vehicleEntity));

        String response = vehicleService.deleteVehicle(vehicleEntity.getId());

        Mockito.verify(vehicleRepository, Mockito.times(1)).deleteById(vehicleEntity.getId());

        Assertions.assertEquals("Veículo deletado com sucesso.", response);
    }

    @Test
    @DisplayName("Should return NotFoundException if the ID is not found to delete the vehicle")
    void deleteVehicleNotFoundException (){
        Long idInvalid = 99L;
        Mockito.when(vehicleRepository.findById(idInvalid)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> vehicleService.deleteVehicle(idInvalid),
                "Deve ser lançada a exceção (NotFoundException) se o ID não for encontrado.");

        Mockito.verify(vehicleRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Should edit the vehicle successfully")
    void editVehicleSuccess (){
        Long validId = 1L;
        VehicleDto vehicleDtoEdit = new VehicleDto(validId, "ABC-123", "Escolar", 2025);
        ArgumentCaptor<VehicleEntity> argumentCaptor = ArgumentCaptor.forClass(VehicleEntity.class);

        VehicleEntity existingVehicle = new VehicleEntity();
        existingVehicle.setId(validId);
        existingVehicle.setPlate("PLACA ANTIGA");
        existingVehicle.setModel("Escolar");
        existingVehicle.setYear(2024);

        Mockito.when(vehicleRepository.findById(validId)).thenReturn(Optional.of(existingVehicle));
        Mockito.when(vehicleRepository.findByPlate(vehicleDtoEdit.plate())).thenReturn(null);
        Mockito.when(vehicleRepository.save(Mockito.any(VehicleEntity.class))).thenReturn(existingVehicle);

        VehicleDto response = vehicleService.editVehicle(validId, vehicleDtoEdit);

        Mockito.verify(vehicleRepository, Mockito.times(1)).save(argumentCaptor.capture());
        VehicleEntity savedVehicle = argumentCaptor.getValue();

        Assertions.assertEquals(vehicleDtoEdit.id(), savedVehicle.getId());
        Assertions.assertEquals(vehicleDtoEdit.plate(), savedVehicle.getPlate());
        Assertions.assertEquals(vehicleDtoEdit.model(), savedVehicle.getModel());
        Assertions.assertEquals(vehicleDtoEdit.year(), savedVehicle.getYear());

        Assertions.assertEquals(vehicleDtoEdit.id(), response.id());
        Assertions.assertEquals(vehicleDtoEdit.plate(), response.plate());
        Assertions.assertEquals(vehicleDtoEdit.model(), response.model());
        Assertions.assertEquals(vehicleDtoEdit.year(), response.year());
    }

    @Test
    @DisplayName("Should return NotFoundException if the vehicle ID is not found")
    void editVehicleNotFoundException (){
        Long idInvalid = 99L;
        VehicleDto vehicleDtoEdit = new VehicleDto(idInvalid, "ABC-123", "Escolar", 2025);

        Mockito.when(vehicleRepository.findById(idInvalid)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> vehicleService.editVehicle(idInvalid, vehicleDtoEdit),
                "Deve ser lançada a exceção (NotFoundException) se o ID não for encontrado.");

        Mockito.verify(vehicleRepository, Mockito.never()).findByPlate(Mockito.any(String.class));
        Mockito.verify(vehicleRepository, Mockito.never()).save(Mockito.any(VehicleEntity.class));
    }

    @Test
    @DisplayName("Should be PlateExistsException returned if the specified plate already exists in the system on a vehicle with a different ID")
    void editVehiclePlateExistsException (){
        Long validID = 1L;
        Long anotherVehicleId = 2L;
        VehicleDto vehicleDtoEdit = new VehicleDto(validID, "PLACA IGUAL", "Escolar", 2025);

        VehicleEntity existingVehicle = new VehicleEntity();
        existingVehicle.setId(validID);
        existingVehicle.setPlate("PLACA ANTIGA");
        existingVehicle.setModel("Escolar");
        existingVehicle.setYear(2024);

        VehicleEntity anotherVehicle = new VehicleEntity();
        anotherVehicle.setId(anotherVehicleId);
        anotherVehicle.setPlate("PLACA IGUAL");

        Mockito.when(vehicleRepository.findById(validID)).thenReturn(Optional.of(existingVehicle));
        Mockito.when(vehicleRepository.findByPlate(vehicleDtoEdit.plate())).thenReturn(anotherVehicle);

        Assertions.assertThrows(PlateExistsException.class, () -> vehicleService.editVehicle(validID, vehicleDtoEdit));

        Mockito.verify(vehicleRepository, Mockito.never()).save(Mockito.any(VehicleEntity.class));
    }
}