
package com.crm.crm_backend.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository
        extends JpaRepository<Customer, Long> {

    // Existing tenant-isolated lookup, now paginated
    Page<Customer> findAllByTenant_Id(
            Long tenantId,
            Pageable pageable);

    // Search customers by name or email within a tenant
    Page<Customer> findByTenant_IdAndNameContainingIgnoreCaseOrTenant_IdAndEmailContainingIgnoreCase(
            Long tenantId1,
            String name,
            Long tenantId2,
            String email,
            Pageable pageable);

    // Existing tenant-isolated customer lookup
    Optional<Customer> findByIdAndTenant_Id(
            Long customerId,
            Long tenantId);
}
