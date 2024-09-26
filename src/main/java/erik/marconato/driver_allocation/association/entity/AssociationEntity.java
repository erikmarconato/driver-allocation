package erik.marconato.driver_allocation.association.entity;

import erik.marconato.driver_allocation.driver.entity.DriverEntity;
import erik.marconato.driver_allocation.vehicle.entity.VehicleEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "associacao_motoristas_veiculos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssociationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "motorista_id")
    private DriverEntity driver;

    @OneToOne
    @JoinColumn(name = "veiculo_id")
    private VehicleEntity vehicle;


    public AssociationEntity(DriverEntity driver, VehicleEntity vehicle) {
        this.driver = driver;
        this.vehicle = vehicle;
    }

}
