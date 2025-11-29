package com.example.shop.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username; // можно email

    @Column(nullable = false)
    private String password; // хэшированный пароль

    @Column(nullable = false)
    private String role; // ROLE_USER или ROLE_ADMIN
}
