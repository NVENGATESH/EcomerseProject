package com.ecommerce.project.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ProductId;

    @NotBlank
    @Size(min = 3,message = "product Must contain atleast 3 charecters")
    private String productName;

    @NotBlank
    @Size(min = 6,message = "product Must contain atleast 6 charecters")
    private String description;

    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;


    private String imageName;
    private String imageType;
    @Lob
    private byte[] imageData;



    @ManyToOne
    @JoinColumn(name = "category_Id")
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Category category;
}
