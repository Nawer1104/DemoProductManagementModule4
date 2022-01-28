package com.example.productmanagement.service;

import com.example.productmanagement.model.CartItem;
import com.example.productmanagement.model.Employee;

import java.util.List;

public interface ShoppingCartService {
    List<CartItem> listCartItems (Employee employee);
    void addProduct(int productId, double quantity, Employee employee);
    void updateQuantity(double quantity, int productId, Employee employee);
    void remove(Employee employee, int productId);
}
