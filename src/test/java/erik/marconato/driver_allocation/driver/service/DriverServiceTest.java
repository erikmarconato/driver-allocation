package erik.marconato.driver_allocation.driver.service;

import erik.marconato.driver_allocation.driver.dto.DriverDto;
import erik.marconato.driver_allocation.driver.entity.DriverEntity;
import erik.marconato.driver_allocation.driver.repository.DriverRepository;
import erik.marconato.driver_allocation.exception.CpfExistsException;
import erik.marconato.driver_allocation.exception.NotFoundException;
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
class DriverServiceTest {

    @Mock
    DriverRepository driverRepository;

    @InjectMocks
    DriverService driverService;

    @Test
    @DisplayName("Should create a driver successfully")
    void createDriverSuccess() {
        Long validId = 1L;
        DriverDto driverDto = new DriverDto(validId, "NomeMotorista", "123.456.789-0", "E");
        ArgumentCaptor<DriverEntity> argumentCaptor = ArgumentCaptor.forClass(DriverEntity.class);

        DriverEntity driverEntity = new DriverEntity();
        driverEntity.setId(validId);
        driverEntity.setName(driverDto.name());
        driverEntity.setCpf(driverDto.cpf());
        driverEntity.setCnh(driverDto.cnh());

        Mockito.when(driverRepository.findByCpf(driverDto.cpf())).thenReturn(null);

        ResponseEntity<DriverDto> response = driverService.createDriver(driverDto);

        Mockito.verify(driverRepository, Mockito.times(1)).save(argumentCaptor.capture());

        DriverEntity savedDriver = argumentCaptor.getValue();
        Assertions.assertEquals("NomeMotorista", savedDriver.getName());
        Assertions.assertEquals("123.456.789-0", savedDriver.getCpf());
        Assertions.assertEquals("E", savedDriver.getCnh());

        DriverDto responseBody = Objects.requireNonNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(validId, responseBody.id());
        Assertions.assertEquals("NomeMotorista", responseBody.name());
        Assertions.assertEquals("123.456.789-0", responseBody.cpf());
        Assertions.assertEquals("E", responseBody.cnh());
    }

    @Test
    @DisplayName("Should be CpfExistsException thrown if the same CPF is already registered in the system")
    void createDriverCpfExistsException(){
        Long validId = 1L;
        DriverDto driverDto = new DriverDto(validId, "NomeMotorista", "123.456.789-0", "E");

        Mockito.when(driverRepository.findByCpf(driverDto.cpf())).thenReturn(new DriverEntity());

        Assertions.assertThrows(CpfExistsException.class, () -> driverService.createDriver(driverDto),
                "Deve ser lançada a exceção (CpfExistsException) se já existir o mesmo CPF cadastrado no sistema.");

        Mockito.verify(driverRepository, Mockito.never()).save(Mockito.any(DriverEntity.class));
    }

    @Test
    @DisplayName("Should list all registered drivers")
    void findAllDriversSuccess(){
        List<DriverEntity> listDrivers = new ArrayList<>();
        DriverEntity driverEntity= new DriverEntity(1L, "NomeMotorista", "123.456.789-0", "E");
        listDrivers.add(driverEntity);

        Mockito.when(driverRepository.findAll()).thenReturn(listDrivers);

        List<DriverDto> response = driverService.findAllDrivers();

        Mockito.verify(driverRepository, Mockito.times(1)).findAll();

        Assertions.assertFalse(listDrivers.isEmpty());
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(1, listDrivers.size());

        Assertions.assertEquals(1L, response.get(0).id());
        Assertions.assertEquals("NomeMotorista", response.get(0).name());
        Assertions.assertEquals("123.456.789-0", response.get(0).cpf());
        Assertions.assertEquals("E", response.get(0).cnh());
    }

    @Test
    @DisplayName("Should throw NotFoundException if driver list is empty")
    void findAllDriversNotFoundException(){
        List<DriverEntity> listDrivers = new ArrayList<>();

        Mockito.when(driverRepository.findAll()).thenReturn(listDrivers);

        Assertions.assertThrows(NotFoundException.class, () -> driverService.findAllDrivers(),
                "Deve ser lançada a exceção (NotFoundException) se a lista de motoristas estiver vazia");

        Mockito.verify(driverRepository, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("Should show driver details by id successfully")
    void findByIdDriverSuccess(){
        Long validId = 1L;
        DriverEntity driverEntity= new DriverEntity(validId, "NomeMotorista", "123.456.789-0", "E");

        Mockito.when(driverRepository.findById(validId)).thenReturn(Optional.of(driverEntity));

        Optional<DriverDto> response = driverService.findByIdDriver(validId);

        Mockito.verify(driverRepository, Mockito.times(1)).findById(Mockito.anyLong());

        Assertions.assertTrue(response.isPresent());

        Assertions.assertEquals(validId, response.get().id());
        Assertions.assertEquals("NomeMotorista", response.get().name());
        Assertions.assertEquals("123.456.789-0", response.get().cpf());
        Assertions.assertEquals("E", response.get().cnh());
    }

    @Test
    @DisplayName("Should throw NotFoundException if the ID is not found in the system")
    void findByIdDriverNotFoundException(){
        Long invalidID = 99L;

        Mockito.when(driverRepository.findById(invalidID)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> driverService.findByIdDriver(invalidID),
                "Deve ser lançada a exceção (NotFoundException) se o ID não for encontrado no sistema.");

        Mockito.verify(driverRepository, Mockito.times(1)).findById(Mockito.anyLong());
    }

    @Test
    @DisplayName("Should delete the driver successfully")
    void deleteDriverSuccess(){
        Long validId = 1L;
        DriverEntity driverEntity = new DriverEntity(validId, "NomeMotorista", "123.456.789-0", "E");

        Mockito.when(driverRepository.findById(validId)).thenReturn(Optional.of(driverEntity));

        String response = driverService.deleteDriver(validId);

        Mockito.verify(driverRepository, Mockito.times(1)).deleteById(Mockito.anyLong());
        Mockito.verify(driverRepository, Mockito.times(1)).findById(Mockito.anyLong());

        Assertions.assertEquals("Motorista deletado com sucesso.", response);
    }

    @Test
    @DisplayName("Should throw NotFoundException if the ID is not found in the system to delete a driver")
    void deleteDriverNotFoundException(){
        Long invalidId = 99L;

        Mockito.when(driverRepository.findById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> driverService.deleteDriver(invalidId),
                "Deve ser lançada a exceção (NotFoundException) se o ID não foi encontrado no sistema para deletar um motorista.");

        Mockito.verify(driverRepository, Mockito.times(1)).findById(Mockito.anyLong());
        Mockito.verify(driverRepository, Mockito.never()).deleteById(invalidId);
    }

    @Test
    @DisplayName("Should edit a driver successfully")
    void editDriverSuccess(){
        Long validId = 1L;
        DriverDto driverDto = new DriverDto(validId, "MotoristaNovo", "CPF NOVO", "CNH NOVA");
        ArgumentCaptor<DriverEntity> argumentCaptor = ArgumentCaptor.forClass(DriverEntity.class);

        DriverEntity oldDriver = new DriverEntity();
        oldDriver.setId(validId);
        oldDriver.setName("MotoristaAntigo");
        oldDriver.setCpf("CPF ANTIGO");
        oldDriver.setCnh("CNH ANTIGA");

        Mockito.when(driverRepository.findById(validId)).thenReturn(Optional.of(oldDriver));
        Mockito.when(driverRepository.findByCpf(driverDto.cpf())).thenReturn(null);
        Mockito.when(driverRepository.save(Mockito.any(DriverEntity.class))).thenReturn(oldDriver);

        DriverDto response = driverService.editDriver(validId, driverDto);

        Mockito.verify(driverRepository, Mockito.times(1)).save(argumentCaptor.capture());

        DriverEntity savedDriver = argumentCaptor.getValue();
        Assertions.assertEquals(validId, savedDriver.getId());
        Assertions.assertEquals("MotoristaNovo", savedDriver.getName());
        Assertions.assertEquals("CPF NOVO", savedDriver.getCpf());
        Assertions.assertEquals("CNH NOVA", savedDriver.getCnh());

        Assertions.assertEquals(validId, response.id());
        Assertions.assertEquals("MotoristaNovo", response.name());
        Assertions.assertEquals("CPF NOVO", response.cpf());
        Assertions.assertEquals("CNH NOVA", response.cnh());
    }

    @Test
    @DisplayName("Should throw NotFoundException exception if ID is not found in the system to edit a driver")
    void editDriverNotFoundException(){
        Long invalidId = 99L;
        DriverDto driverDto = new DriverDto(100L, "MotoristaQualquer", "CPF QUALQUER", "CNH QUALQUER");

        Mockito.when(driverRepository.findById(invalidId)).thenReturn(Optional.empty());

        Assertions.assertThrows(NotFoundException.class, () -> driverService.editDriver(invalidId, driverDto),
                "Deve ser lançada a exceção (NotFoundException) se o ID não for encontrado no sistema para editar um motorista.");

        Mockito.verify(driverRepository, Mockito.never()).findByCpf(Mockito.any(String.class));
        Mockito.verify(driverRepository, Mockito.never()).save(Mockito.any(DriverEntity.class));
    }

    @Test
    @DisplayName("Should CpfExistsException exception thrown if the CPF already exists in the system with a different ID")
    void editDriverCpfExistsException(){
        Long validId1 = 1L;
        Long validId2 = 2L;
        DriverDto driverDto = new DriverDto(validId2, "MotoristaNovo", "CPF IGUAL", "CNH Nova");

        DriverEntity oldDriver = new DriverEntity();
        oldDriver.setId(validId1);
        oldDriver.setName("MotoristaAntigo");
        oldDriver.setCpf("CPF IGUAL");
        oldDriver.setCnh("CNHAntiga");

        DriverEntity oldDriver2 = new DriverEntity();
        oldDriver2.setId(validId2);
        oldDriver2.setName("MotoristaAntigo2");
        oldDriver2.setCpf("CPFAntigo2");
        oldDriver2.setCnh("CNHAntiga2");

        Mockito.when(driverRepository.findById(validId2)).thenReturn(Optional.of(oldDriver2));
        Mockito.when(driverRepository.findByCpf(driverDto.cpf())).thenReturn(oldDriver);

        Assertions.assertThrows(CpfExistsException.class, () -> driverService.editDriver(validId2, driverDto),
                "Deve lançar a exceção (CpfExistsException) se o CPF já for cadastrado no sistema em outro ID diferente.");

        Mockito.verify(driverRepository, Mockito.never()).save(Mockito.any(DriverEntity.class));
    }
}