package com.example.productmanagement.service;

import com.example.productmanagement.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductService {
    Page<Product> findAll(int pageNumber,  String sortField, String sortDir);
    Page<Product> findAllByName(int pageNumber,  String sortField, String sortDir, String keyword);
    void save(Product product);
    void delete(int id);
    Product findById(int id);
}
