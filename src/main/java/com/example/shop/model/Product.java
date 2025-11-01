package com.example.shop.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import jakarta.validation.constraints.PositiveOrZero;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"name", "category_id"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String description;

    @Positive
    private Double price;

    @PositiveOrZero
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0; // количество товара на складе

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull
    @JsonIgnoreProperties({"products"})
    private Category category;

}
