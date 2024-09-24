package erik.marconato.driver_allocation.driver.service;

import erik.marconato.driver_allocation.driver.dto.DriverDto;
import erik.marconato.driver_allocation.driver.entity.DriverEntity;
import erik.marconato.driver_allocation.driver.repository.DriverRepository;
import erik.marconato.driver_allocation.exception.CpfExistsException;
import erik.marconato.driver_allocation.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DriverService {

    @Autowired
    DriverRepository driverRepository;

    public ResponseEntity<DriverDto> createDriver(DriverDto driverDto) {

        DriverEntity driverEntity = new DriverEntity(driverDto);

        DriverEntity CpfExists = driverRepository.findByCpf(driverEntity.getCpf());

        if (CpfExists != null) {
            throw new CpfExistsException("CPF já cadastrado no sistema.");
        }

        driverRepository.save(driverEntity);

        var driverConvert = new DriverDto(
                driverEntity.getId(),
                driverEntity.getName(),
                driverEntity.getCpf(),
                driverEntity.getCnh()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(driverConvert);
    }

    public List<DriverDto> findAllDrivers() {
        List<DriverEntity> driverEntityList = driverRepository.findAll();

        if (driverEntityList.isEmpty()){
            throw new NotFoundException("Não há motoristas cadastrados.");
        }
        return driverEntityList.stream().map(driverEntity ->
                new DriverDto(
                        driverEntity.getId(),
                        driverEntity.getName(),
                        driverEntity.getCpf(),
                        driverEntity.getCnh()
                )).collect(Collectors.toList());
    }

    public Optional<DriverDto> findByIdDriver (Long id){

        return Optional.ofNullable(driverRepository.findById(id).map(driver ->
                        new DriverDto(
                                driver.getId(),
                                driver.getName(),
                                driver.getCpf(),
                                driver.getCnh()
                        ))
                .orElseThrow(() -> new NotFoundException("Motorista não encontrado com o ID em questão. Verifique o ID.")));
    }

    public String deleteDriver (Long id){

        var driverExists = driverRepository.findById(id);

        if (driverExists.isPresent()){
            driverRepository.deleteById(id);
            return "Motorista deletado com sucesso.";
        }
        throw new NotFoundException("Motorista não encontrado. Por favor, verifique se o ID está correto.");
    }

    public DriverDto editDriver (Long id, DriverDto driverDto){

        var driverExists = driverRepository.findById(id);

        DriverEntity cpfExists = (DriverEntity) driverRepository.findByCpf(driverDto.cpf());

        if (driverExists.isPresent()){

            if (cpfExists != null && !cpfExists.getId().equals(id)){
                throw new CpfExistsException("CPF já cadastrado no sistema. Por favor, utilize outro CPF.");
            }

            DriverEntity driverEntity = driverExists.get();

            driverEntity.setName(driverDto.name());
            driverEntity.setCpf(driverDto.cpf());
            driverEntity.setCnh(driverDto.cnh());

            driverRepository.save(driverEntity);

            return new DriverDto(
                    driverEntity.getId(),
                    driverEntity.getName(),
                    driverEntity.getCpf(),
                    driverEntity.getCnh()
            );
        }

        throw new NotFoundException("Motorista não encontrado. Por favor, verifique se o ID está correto.");
    }
}
