package com.example.productmanagement.service.impl;

import com.example.productmanagement.model.CartItem;
import com.example.productmanagement.model.Employee;
import com.example.productmanagement.model.Product;
import com.example.productmanagement.repository.CartItemRepository;
import com.example.productmanagement.repository.ProductRepository;
import com.example.productmanagement.service.ProductService;
import com.example.productmanagement.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    ProductService productService;

    @PersistenceContext
    EntityManager em;

    @Override
    public List<CartItem> listCartItems(Employee employee) {
        return cartItemRepository.findCartItemByEmployee(employee);
    }

    @Override
    public void addProduct(int productId, double quantity, Employee employee) {
        double addedQuantity = quantity;
        Product product = productService.findById(productId);
        CartItem cartItem = cartItemRepository.findCartItemByEmployeeAndProduct(employee, product);
        if (cartItem!= null) {
            addedQuantity = cartItem.getQuantity() + quantity;
            cartItem.setQuantity(addedQuantity);
        } else {
            cartItem = new CartItem();
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            cartItem.setEmployee(employee);
        }
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void updateQuantity(double quantity, int productId, Employee employee) {
        em.joinTransaction();
        cartItemRepository.updateQuantity(quantity, productId, employee.getId());
    }

    @Override
    @Transactional
    public void remove(Employee employee, int productId) {
        em.joinTransaction();
        cartItemRepository.deleteCartItemByEmployeeAndProduct(employee.getId(), productId);
    }
}
