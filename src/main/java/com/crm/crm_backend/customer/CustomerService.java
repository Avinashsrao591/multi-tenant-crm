
package com.crm.crm_backend.customer;

import com.crm.crm_backend.security.TenantContext;
import com.crm.crm_backend.tenant.Tenant;
import com.crm.crm_backend.tenant.TenantRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final TenantRepository tenantRepository;

    public CustomerService(
            CustomerRepository customerRepository,
            TenantRepository tenantRepository) {
        this.customerRepository = customerRepository;
        this.tenantRepository = tenantRepository;
    }

    public Customer createCustomer(Customer customer) {
        Long tenantId = requireTenantId();

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tenant not found"));

        customer.setTenant(tenant);
        return customerRepository.save(customer);
    }

    

public Page<Customer> getCustomers(
        int page,
        int size,
        String sortBy,
        String direction) {

    Long tenantId = requireTenantId();

    Pageable pageable = createPageable(
            page, size, sortBy, direction);

    return customerRepository.findAllByTenant_Id(
            tenantId, pageable);
}


public Page<Customer> searchCustomers(
        String keyword,
        int page,
        int size,
        String sortBy,
        String direction) {

    Long tenantId = requireTenantId();
    Pageable pageable = createPageable(page, size, sortBy, direction);

    if (keyword == null || keyword.isBlank()) {
        return customerRepository.findAllByTenant_Id(
                tenantId, pageable);
    }

    return customerRepository
            .findByTenant_IdAndNameContainingIgnoreCaseOrTenant_IdAndEmailContainingIgnoreCase(
                    tenantId,
                    keyword.trim(),
                    tenantId,
                    keyword.trim(),
                    pageable);
}

    
private Pageable createPageable(
        int page,
        int size,
        String sortBy,
        String direction) {

    if (page < 0) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Page number cannot be negative");
    }

    if (size < 1 || size > 100) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Page size must be between 1 and 100");
    }

    if (sortBy == null ||
            !java.util.Set.of("id", "name", "email", "phone")
                    .contains(sortBy)) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Invalid sort field. Allowed fields: id, name, email, phone");
    }

    Sort.Direction sortDirection;

    try {
        sortDirection = Sort.Direction.fromString(direction);
    } catch (IllegalArgumentException | NullPointerException exception) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Sort direction must be asc or desc");
    }

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(sortDirection, sortBy));

    return pageable;
}

public Customer getCustomerById1(Long customerId) {
    Long tenantId = requireTenantId();

    return customerRepository
            .findByIdAndTenant_Id(customerId, tenantId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found"));
}


    public Customer updateCustomer(
            Long customerId, Customer updatedCustomer) {

        Long tenantId = requireTenantId();

        Customer existingCustomer = customerRepository
                .findByIdAndTenant_Id(customerId, tenantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhone(updatedCustomer.getPhone());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(Long customerId) {
        Long tenantId = requireTenantId();

        Customer customer = customerRepository
                .findByIdAndTenant_Id(customerId, tenantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));

        customerRepository.delete(customer);
    }

    private Long requireTenantId() {
        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authenticated tenant context is missing");
        }

        return tenantId;
    }

    public Customer getCustomerById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCustomerById'");
    }
}
