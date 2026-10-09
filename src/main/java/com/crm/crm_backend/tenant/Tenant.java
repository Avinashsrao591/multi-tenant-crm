package com.crm.crm_backend.tenant;

import jakarta.persistence.*;
import com.crm.crm_backend.user.User;
import java.util.List;

@SuppressWarnings("unused")
@Entity
@Table(name = "tenants")
public class Tenant {
@OneToMany(mappedBy = "tenant")
private List<User> users;
public List<User> getUsers() {
    return users;
}

public void setUsers(List<User> users) {
    this.users = users;
}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(unique = true)
    private String domain;

    @Column(nullable = false)
    private String status;

    public Tenant() {
    }

    public Tenant(String name, String domain, String status) {
        this.name = name;
        this.domain = domain;
        this.status = status;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}