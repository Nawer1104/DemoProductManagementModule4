package com.example.productmanagement.service;

import com.example.productmanagement.model.Category;
import com.example.productmanagement.model.Product;

import java.util.List;

public interface CategoryService {
    List<Category> findAll();
    void save(Category category);
    void delete(int id);
    Category findById(int id);
}
