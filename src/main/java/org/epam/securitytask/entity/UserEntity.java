package org.epam.securitytask.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String email;
    private String password;
    private List<Permissions> permissions;

    public UserEntity() {
    }

    public UserEntity(String email, String password, List<Permissions> permissions) {
        this.email = email;
        this.password = password;
        this.permissions = permissions;
    }

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<Permissions> getPermissions() {
        return permissions;
    }
}
