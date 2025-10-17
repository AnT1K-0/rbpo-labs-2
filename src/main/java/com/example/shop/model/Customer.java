package com.example.shop.model;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Data
public class Customer {
    private Long id;

    @NotBlank(message = "Имя обязательно")
    private String firstName;

    @NotBlank(message = "Фамилия обязательна")
    private String lastName;

    @Email(message = "Некорректный email")
    private String email;

    private String phone;
}