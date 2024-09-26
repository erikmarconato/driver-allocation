package erik.marconato.driver_allocation.association.repository;

import erik.marconato.driver_allocation.association.entity.AssociationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssociationRepository extends JpaRepository<AssociationEntity, Long> {

    AssociationEntity findByDriverId(Long driver);

    AssociationEntity findByVehicleId(Long vehicle);
}
