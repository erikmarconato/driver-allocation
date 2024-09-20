package erik.marconato.driver_allocation.vehicle.dto;

public record VehicleDto(
        Long id,
        String plate,
        String model,
        Integer year
) {
}
