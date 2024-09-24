package erik.marconato.driver_allocation.driver.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record DriverDto(

        @Schema(description = "ID do motorista (Único no sistema) (Não é salvo por requisições HTTP (ignore). É gerado automaticamente pelo banco de dados)")
        Long id,

        @NotBlank
        @Schema(description = "Nome do motorista")
        String name,

        @NotBlank
        @Schema(description = "CPF do motorista (Único no sistema)")
        String cpf,

        @NotBlank
        @Schema(description = "CNH do motorista")
        String cnh
) {
}
