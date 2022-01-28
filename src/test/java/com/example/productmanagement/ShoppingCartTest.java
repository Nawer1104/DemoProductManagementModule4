package com.example.productmanagement;

import com.example.productmanagement.model.CartItem;
import com.example.productmanagement.model.Employee;
import com.example.productmanagement.model.Product;
import com.example.productmanagement.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ShoppingCartTest {
    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testAddOneCartItem() {
       Product product = entityManager.find(Product.class, 15);
        Employee employee = entityManager.find(Employee.class, 9);

        CartItem cartItem = new CartItem();
        cartItem.setEmployee(employee);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);

        CartItem cartSaved = cartItemRepository.save(cartItem);

        if (cartSaved.getId() > 0) {
            System.out.println("true");
        }
    }

    @Test
    public void testGetCartItemByEmployee () {
        Employee employee = new Employee();
        employee.setId(9);

        List<CartItem> cartItemList = cartItemRepository.findCartItemByEmployee(employee);
        if (cartItemList.size() == 1) {
            System.out.println("true");
        }
    }
}
