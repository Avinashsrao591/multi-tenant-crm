
package com.crm.crm_backend.customer;

import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

  @PostMapping
public Customer createCustomer(
        @Valid @RequestBody Customer customer) {
    return customerService.createCustomer(customer);
}
    @GetMapping
    public List<Customer> getCustomers() {
        return customerService.getCustomers();
    }

    @GetMapping("/{id}")
    public Customer getCustomerById(
            @PathVariable Long id) {
        return customerService.getCustomerById(id);
    }
    
@PutMapping("/{id}")
public Customer updateCustomer(
        @PathVariable Long id,
        @Valid @RequestBody Customer customer) {
    return customerService.updateCustomer(id, customer);
}

@DeleteMapping("/{id}")
public void deleteCustomer(@PathVariable Long id) {
    customerService.deleteCustomer(id);
}

}
