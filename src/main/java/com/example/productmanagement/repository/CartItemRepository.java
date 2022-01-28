package com.example.productmanagement.repository;

import com.example.productmanagement.model.CartItem;
import com.example.productmanagement.model.Employee;
import com.example.productmanagement.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findCartItemByEmployee(Employee employee);

    CartItem findCartItemByEmployeeAndProduct(Employee employee, Product product);

    @Query("UPDATE CartItem c SET c.quantity = ?1 WHERE c.product.id = ?2 AND c.employee.id = ?3")
    @Modifying
    void updateQuantity(double quantity, int productId, int employeeId);

    @Query("DELETE FROM CartItem c where c.employee.id = ?1 and c.product.id = ?2")
    @Modifying
    void deleteCartItemByEmployeeAndProduct(int employeeId, int productId);
}
