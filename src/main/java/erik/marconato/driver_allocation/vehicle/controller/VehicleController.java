package erik.marconato.driver_allocation.vehicle.controller;

import erik.marconato.driver_allocation.vehicle.dto.VehicleDto;
import erik.marconato.driver_allocation.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    @Operation(summary = "Cria um novo veículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Veículo criado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Placa já está em uso no sistema. Por favor, escolha uma placa diferente."),
            @ApiResponse(responseCode = "409", description = "Por favor, preencha todos os campos."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PostMapping
    public ResponseEntity<VehicleDto> createVehicle (@RequestBody VehicleDto vehicle){
        return vehicleService.createVehicle(vehicle);
    }



    @Operation(summary = "Lista todos os veículos cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao listar todos os veículos."),
            @ApiResponse(responseCode = "404", description = "Não há veículos cadastrados."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping
    public List<VehicleDto> findAllVehicles (){
        return vehicleService.findAllVehicles();
    }



    @Operation(summary = "Busca o veículo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao buscar o veículo."),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado com o ID em questão. Verifique o ID."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping("/{id}")
    public Optional<VehicleDto> findByIdVehicle (@PathVariable Long id){
        return vehicleService.findByIdVehicle(id);
    }



    @Operation(summary = "Deleta o veículo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao Deletar o veículo."),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado com o ID em questão. Verifique o ID."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @DeleteMapping("/{id}")
    public String deleteVehicle (@PathVariable Long id){
        return vehicleService.deleteVehicle(id);
    }



    @Operation(summary = "Edita os dados do veículo por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao Editar o veículo."),
            @ApiResponse(responseCode = "400", description = "Placa já está em uso no sistema. Por favor, escolha uma placa diferente."),
            @ApiResponse(responseCode = "404", description = "Veículo não encontrado com o ID em questão. Verifique o ID."),
            @ApiResponse(responseCode = "409", description = "Por favor, preencha todos os campos."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PutMapping("/{id}")
    public VehicleDto editVehicle (@PathVariable Long id, @RequestBody VehicleDto vehicleDto){
       return vehicleService.editVehicle(id, vehicleDto);
    }
}
