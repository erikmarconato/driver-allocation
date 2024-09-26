package erik.marconato.driver_allocation.association.dto;


import io.swagger.v3.oas.annotations.media.Schema;

public record AssociationDto(

        @Schema(description = "ID da Associação (Único no sistema) (Não é salvo por requisições HTTP (ignore). É gerado automaticamente pelo banco de dados)")
        Long id,

        @Schema(description = "ID do Motorista (Único no sistema)")
        Long driver,

        @Schema(description = "ID do Veículo (Único no sistema)")
        Long vehicle
) {
}
