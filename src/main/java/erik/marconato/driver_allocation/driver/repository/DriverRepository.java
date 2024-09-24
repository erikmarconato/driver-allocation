package erik.marconato.driver_allocation.driver.repository;

import erik.marconato.driver_allocation.driver.entity.DriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<DriverEntity, Long> {
    DriverEntity findByCpf(String cpf);
}
