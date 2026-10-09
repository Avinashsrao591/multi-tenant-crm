
package com.crm.crm_backend.customer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@SuppressWarnings("unused")
public interface CustomerRepository
        extends JpaRepository<Customer, Long> {


@Query("""
    SELECT c FROM Customer c
    WHERE c.tenant.id = :tenantId
      AND (
          :nameFilter = ''
          OR LOWER(c.name) LIKE LOWER(CONCAT('%', :nameFilter, '%'))
      )
      AND (
          :emailFilter = ''
          OR LOWER(c.email) LIKE LOWER(CONCAT('%', :emailFilter, '%'))
      )
    """)
Page<Customer> filterCustomers(
        @Param("tenantId") Long tenantId,
        @Param("nameFilter") String nameFilter,
        @Param("emailFilter") String emailFilter,
        Pageable pageable);

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
