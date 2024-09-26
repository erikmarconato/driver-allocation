package erik.marconato.driver_allocation.association.controller;

import erik.marconato.driver_allocation.association.dto.AssociationDto;
import erik.marconato.driver_allocation.association.service.AssociationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/associacao")
@CrossOrigin
public class AssociationController {

    @Autowired
    AssociationService associationService;


    @Operation(summary = "Cria uma nova associação de motorista e veículo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associação criada com sucesso."),
            @ApiResponse(responseCode = "400", description = "Motorista ou Veículo já associado no banco de dados. Por favor, verifique os IDs."),
            @ApiResponse(responseCode = "404", description = "Motorista ou veículo não encontrado. Por favor, verifique os IDs."),
            @ApiResponse(responseCode = "409", description = "Por favor, preencha todos os campos."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PostMapping
    public ResponseEntity<AssociationDto> createAssociation (@RequestBody AssociationDto associationDto){
        return associationService.createAssociation(associationDto);
    }



    @Operation(summary = "Lista todas as associações cadastradas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao listar todas as associações."),
            @ApiResponse(responseCode = "404", description = "Não há associações cadastradas."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping
    public List<AssociationDto> findAllAssociations (){
        return associationService.findAllAssociations();
    }



    @Operation(summary = "Busca a Associação por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao buscar a associação."),
            @ApiResponse(responseCode = "404", description = "Associação não encontrada com o ID em questão. Verifique o ID."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @GetMapping("/{id}")
    public Optional<AssociationDto> findByIdAssociation (@PathVariable Long id){
        return associationService.findByIdAssociation(id);
    }



    @Operation(summary = "Deleta a Associação por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao Deletar a associação."),
            @ApiResponse(responseCode = "404", description = "Associação não encontrada com o ID em questão. Verifique o ID."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @DeleteMapping("/{id}")
    public String deleteAssociation (@PathVariable Long id){
        return associationService.deleteAssociation(id);
    }



    @Operation(summary = "Edita a Associação por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sucesso ao Editar a associação"),
            @ApiResponse(responseCode = "400", description = "Associação não encontrada. Por favor, verifique se o ID está correto."),
            @ApiResponse(responseCode = "404", description = "Motorista ou Veículo não encontrado. Por favor, verifique o ID."),
            @ApiResponse(responseCode = "409", description = "Por favor, preencha todos os campos."),
            @ApiResponse(responseCode = "412", description = "Motorista ou Veículo já associado no banco de dados. Delete a associação correspondente ou verifique os IDs."),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor.")
    })
    @PutMapping("/{id}")
    public AssociationDto editAssociation (@PathVariable Long id, @RequestBody AssociationDto associationDto){
        return associationService.editAssociation(id, associationDto);
    }
}
