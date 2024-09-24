package erik.marconato.driver_allocation.driver.controller;

import erik.marconato.driver_allocation.driver.dto.DriverDto;
import erik.marconato.driver_allocation.driver.service.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/motoristas")
@CrossOrigin
public class DriverController {

    @Autowired
    DriverService driverService;


    @Operation(summary = "Cria um novo motorista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Motorista criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "CPF já existe no sistema."),
            @ApiResponse(responseCode = "409", description = "Por favor, preencha todos os campos."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PostMapping
    public ResponseEntity<DriverDto> createDriver (@RequestBody DriverDto driverDto){
        return driverService.createDriver(driverDto);
    }



    @Operation(summary = "Lista todos os motoristas cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao listar todos os motoristas."),
            @ApiResponse(responseCode = "404", description = "Não há motoristas cadastrados."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping
    public List<DriverDto> findAllDrivers(){
        return driverService.findAllDrivers();
    }



    @Operation(summary = "Busca o motorista por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao buscar o motorista."),
            @ApiResponse(responseCode = "404", description = "Motorista não encontrado com o ID em questão. Verifique o ID."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping("/{id}")
    public Optional<DriverDto> findByIdDriver (@PathVariable Long id){
        return driverService.findByIdDriver(id);
    }



    @Operation(summary = "Deleta o motorista por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao Deletar o motorista."),
            @ApiResponse(responseCode = "404", description = "Motorista não encontrado com o ID em questão. Verifique o ID."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @DeleteMapping("/{id}")
    public String deleteDriver (@PathVariable Long id){
        return driverService.deleteDriver(id);
    }



    @Operation(summary = "Edita os dados do motorista por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao Editar os dados do motorista."),
            @ApiResponse(responseCode = "400", description = "CPF já está em uso no sistema. Por favor, escolha uma CPF diferente."),
            @ApiResponse(responseCode = "404", description = "Motorista não encontrado com o ID em questão. Verifique o ID."),
            @ApiResponse(responseCode = "409", description = "Por favor, preencha todos os campos."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PutMapping("/{id}")
    public DriverDto editVehicle (@PathVariable Long id, @RequestBody DriverDto driverDto){
        return driverService.editDriver(id, driverDto);
    }
}
