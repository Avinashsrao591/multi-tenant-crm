
package com.crm.crm_backend.customer;


import com.crm.crm_backend.tenant.Tenant;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @NotBlank(message = "Customer name is required")
@Size(max = 100, message = "Customer name must not exceed 100 characters")
@Column(nullable = false, length = 100)
private String name;

@NotBlank(message = "Customer email is required")
@Email(message = "Customer email must be valid")
@Size(max = 150, message = "Customer email must not exceed 150 characters")
@Column(nullable = false, length = 150)
private String email;

@Pattern(
    regexp = "^$|^[0-9+() -]{7,20}$",
    message = "Phone must contain 7–20 valid phone characters"
)
private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenant tenant;

    public Customer() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
