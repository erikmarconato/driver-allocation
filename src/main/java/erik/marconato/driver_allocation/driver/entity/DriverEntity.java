package erik.marconato.driver_allocation.driver.entity;

import erik.marconato.driver_allocation.association.entity.AssociationEntity;
import erik.marconato.driver_allocation.driver.dto.DriverDto;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "motoristas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class DriverEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false)
    private String name;

    @Column(name = "cpf", nullable = false, unique = true)
    private String cpf;

    @Column(name = "cnh", nullable = false)
    private String cnh;

    @OneToOne(mappedBy = "driver")
    AssociationEntity associationEntity;


    public DriverEntity(DriverDto driverDto) {
        this.id = driverDto.id();
        this.name = driverDto.name();
        this.cpf = driverDto.cpf();
        this.cnh = driverDto.cnh();
    }

    public DriverEntity(Long id, String name, String cpf, String cnh) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.cnh = cnh;
    }
}
