package erik.marconato.driver_allocation.vehicle.repository;

import erik.marconato.driver_allocation.vehicle.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {
    Object findByPlate(String plate);

}
