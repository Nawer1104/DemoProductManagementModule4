package com.example.productmanagement.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 55, name = "pname")
    @NotEmpty(message = "Name must not be empty")
    @Size(min = 6, max=15, message = "Name's length from 6 to 15 characters")
    private String name;

    @Column(length = 55, name = "pprice")
    @Range(min = 0, max = 100000, message = "Price range from $0 to $100.000")
    private double price;

    @Column(length = 55, name = "pquan")
    @Range(min = 1, max = 100000, message = "Quantity range from 1 to 100.000")
    private double quantity;

    @Column(length = 200, name = "pimg")
    private String image;

    @ManyToOne
    private Category category;

    public Product() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Transient
    public String getImagePath() {
        if (image == null) return null;
        return "/images/" + image;
    }
}
