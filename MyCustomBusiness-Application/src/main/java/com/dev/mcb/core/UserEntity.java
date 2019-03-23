package com.dev.mcb.core;

import javax.persistence.*;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "mcb_user")
@NamedQueries(
        {
                @NamedQuery(
                        name = "com.dev.mcb.core.UserEntity.findAll",
                        query = "SELECT u FROM UserEntity u"
                ),
                @NamedQuery(
                        name = "com.dev.mcb.core.UserEntity.findByEmail",
                        query = "SELECT u FROM UserEntity WHERE u.email = :email"
                )
        })
public class UserEntity implements Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<UserConfigEntity> configurations;

    public UserEntity() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<UserConfigEntity> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<UserConfigEntity> configurations) {
        this.configurations = configurations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(username, that.username) &&
                Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(configurations, that.configurations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, email, password, configurations);
    }

    @Override
    public String getName() {
        return this.username;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", configurations=" + configurations +
                '}';
    }
}
