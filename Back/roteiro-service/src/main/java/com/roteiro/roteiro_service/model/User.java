package com.roteiro.roteiro_service.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "users")
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;


    @Column(unique = true, nullable = false)
    private String email;


    @Column(nullable = false)
    private String password;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // A conta nunca expira
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // A conta nunca é bloqueada
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // As credenciais nunca expiram
    }

    @Override
    public boolean isEnabled() {
        return true; // A conta está sempre ativa
    }
}
