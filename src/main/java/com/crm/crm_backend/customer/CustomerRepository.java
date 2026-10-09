
package com.crm.crm_backend.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    List<Customer> findAllByTenant_Id(Long tenantId);

    Optional<Customer> findByIdAndTenant_Id(
            Long customerId,
            Long tenantId);
}
