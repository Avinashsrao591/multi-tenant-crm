
package com.crm.crm_backend.user;

import com.crm.crm_backend.security.TenantContext;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;



@SuppressWarnings("unused")
@Validated
@RestController

@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(

        @RequestParam
@NotBlank(message = "Name is required")
@Size(max = 100, message = "Name must not exceed 100 characters")
String name,

@RequestParam
@NotBlank(message = "Email is required")
@Email(message = "Email must be valid")
@Size(max = 150, message = "Email must not exceed 150 characters")
String email,

@RequestParam
@NotBlank(message = "Password is required")
@Size(min = 8, max = 72, message = "Password must be 8–72 characters")
String password,
           
            @RequestParam Role role,
            Authentication authentication) {

        // Only authenticated tenant admins can create users.
        boolean isTenantAdmin = authentication != null
                && authentication.getAuthorities().stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_TENANT_ADMIN"));

        if (!isTenantAdmin) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Only tenant admins can create users"
            );
        }

        // Never trust a tenant ID supplied by the request.
        Long tenantId = TenantContext.getTenantId();

        if (tenantId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Authenticated tenant context is missing"
            );
        }

        // Prevent tenant admins from granting privileged roles.
        if (role == Role.SUPER_ADMIN || role == Role.TENANT_ADMIN) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You cannot assign this role"
            );
        }

        return userService.createUser(
                name,
                email,
                password,
                role,
                tenantId
        );
    }
}
