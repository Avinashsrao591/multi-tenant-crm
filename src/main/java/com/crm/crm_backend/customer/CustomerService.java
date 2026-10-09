
package com.crm.crm_backend.customer;

import com.crm.crm_backend.security.TenantContext;
import com.crm.crm_backend.tenant.Tenant;
import com.crm.crm_backend.tenant.TenantRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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

    public List<Customer> getCustomers() {
        Long tenantId = requireTenantId();
        return customerRepository.findAllByTenant_Id(tenantId);
    }

    public Customer getCustomerById(Long customerId) {
        Long tenantId = requireTenantId();

        return customerRepository
                .findByIdAndTenant_Id(customerId, tenantId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Customer not found"));
    }

public Customer updateCustomer(
        Long customerId,
        Customer updatedCustomer) {

    Long tenantId = requireTenantId();

    Customer existingCustomer = customerRepository
            .findByIdAndTenant_Id(customerId, tenantId)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Customer not found"));

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
                    HttpStatus.NOT_FOUND,
                    "Customer not found"));

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
}
