package com.crm.crm_backend.user;

import com.crm.crm_backend.tenant.Tenant;
import com.crm.crm_backend.tenant.TenantRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            TenantRepository tenantRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.tenantRepository = tenantRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(
            String name,
            String email,
            String password,
            Role role,
            Long tenantId) {

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() ->
                        new RuntimeException("Tenant not found"));

        User user = new User();

        user.setName(name);
        user.setEmail(email);

        // Never store plain-text passwords
        user.setPassword(
                passwordEncoder.encode(password)
        );

        user.setRole(role);
        user.setStatus("ACTIVE");
        user.setTenant(tenant);

        return userRepository.save(user);
    }
}