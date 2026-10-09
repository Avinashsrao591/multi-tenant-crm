package com.crm.crm_backend.customer;
import org.springframework.data.domain.PageRequest;
import com.crm.crm_backend.security.TenantContext;
import com.crm.crm_backend.tenant.TenantRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SuppressWarnings("unused")
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TenantRepository tenantRepository;

    @InjectMocks
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        TenantContext.setTenantId(2L);
    }

    @AfterEach
    void tearDown() {
        TenantContext.clear();
    }

    @Test
    void updateCustomerShouldRejectCustomerFromAnotherTenant() {
        when(customerRepository.findByIdAndTenant_Id(1L, 2L))
                .thenReturn(Optional.empty());

        Customer updatedCustomer = new Customer();
        updatedCustomer.setName("Unauthorized Update");
        updatedCustomer.setEmail("unauthorized@example.com");
        updatedCustomer.setPhone("1111111111");

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> customerService.updateCustomer(1L, updatedCustomer)
        );

        assertEquals(404, exception.getStatusCode().value());

        verify(customerRepository, never()).save(any(Customer.class));
    }

    @Test
    void deleteCustomerShouldRejectCustomerFromAnotherTenant() {
        when(customerRepository.findByIdAndTenant_Id(1L, 2L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> customerService.deleteCustomer(1L)
        );

        assertEquals(404, exception.getStatusCode().value());

        verify(customerRepository, never()).delete(any(Customer.class));
    }

    @Test
    void customerOperationShouldRejectMissingTenantContext() {
        TenantContext.clear();

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> customerService.getCustomers(0,10)
        );

        assertEquals(401, exception.getStatusCode().value());

        verify(customerRepository, never())
        .findAllByTenant_Id(anyLong(), any(org.springframework.data.domain.Pageable.class));
    }
}
