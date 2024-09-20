package erik.marconato.driver_allocation.vehicle.entity;

import erik.marconato.driver_allocation.vehicle.dto.VehicleDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "veiculos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "placa", unique = true, nullable = false)
    private String plate;

    @Column(name = "modelo", nullable = false)
    private String model;

    @Column(name = "ano", nullable = false)
    private Integer year;


    public VehicleEntity(VehicleDto vehicle) {
        this.id = vehicle.id();
        this.plate = vehicle.plate();
        this.model = vehicle.model();
        this.year = vehicle.year();
    }
}
