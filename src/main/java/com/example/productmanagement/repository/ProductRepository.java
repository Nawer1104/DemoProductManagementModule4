package com.example.productmanagement.repository;

import com.example.productmanagement.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends PagingAndSortingRepository<Product, Integer> {
    @Query(value = "SELECT p FROM Product p WHERE p.name LIKE %:keyword%")
    List<Product> search(@Param(value = "keyword") String keyword);

    Page<Product> findAllByNameContaining(String keyword, Pageable pageable);
}
