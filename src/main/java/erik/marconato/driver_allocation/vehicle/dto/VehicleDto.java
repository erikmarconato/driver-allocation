package erik.marconato.driver_allocation.vehicle.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VehicleDto(

        @Schema(description = "ID do veículo (Único no sistema) (Não é salvo por requisições HTTP (ignore). É gerado automaticamente pelo banco de dados)")
        Long id,

        @NotBlank
        @Schema(description = "Placa do veículo (Único no sistema)")
        String plate,

        @NotBlank
        @Schema(description = "Modelo do veículo")
        String model,

        @NotNull
        @Schema(description = "Ano de fabricação do veículo")
        Integer year
) {
}
