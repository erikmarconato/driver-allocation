package erik.marconato.driver_allocation.vehicle.entity;

import erik.marconato.driver_allocation.association.entity.AssociationEntity;
import erik.marconato.driver_allocation.vehicle.dto.VehicleDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "veiculos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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

    @OneToOne(mappedBy = "vehicle")
    AssociationEntity associationEntity;

    public VehicleEntity(VehicleDto vehicle) {
        this.id = vehicle.id();
        this.plate = vehicle.plate();
        this.model = vehicle.model();
        this.year = vehicle.year();
    }

}
