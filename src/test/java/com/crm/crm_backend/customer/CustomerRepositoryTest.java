
package com.crm.crm_backend.customer;

import com.crm.crm_backend.tenant.Tenant;
import com.crm.crm_backend.tenant.TenantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

@DataJpaTest
@AutoConfigureTestDatabase(
        replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TenantRepository tenantRepository;

    @Test
    void tenantCannotRetrieveAnotherTenantsCustomer() {
        Tenant tenantA = tenantRepository.save(
                new Tenant("Tenant A Test", "tenant-a-test.example", "ACTIVE"));

        Tenant tenantB = tenantRepository.save(
                new Tenant("Tenant B Test", "tenant-b-test.example", "ACTIVE"));

        Customer customer = new Customer();
        customer.setName("Test Customer");
        customer.setEmail("customer@test.example");
        customer.setPhone("1234567890");
        customer.setTenant(tenantA);

        Customer savedCustomer = customerRepository.save(customer);

        assertThat(
                customerRepository.findByIdAndTenant_Id(
                        savedCustomer.getId(), tenantB.getId()))
                .isEmpty();

        assertThat(
                customerRepository.findAllByTenant_Id(tenantB.getId()))
                .isEmpty();

        assertThat(
                customerRepository.findByIdAndTenant_Id(
                        savedCustomer.getId(), tenantA.getId()))
                .isPresent();
    }
}
