package com.example.productmanagement;

import com.example.productmanagement.model.Role;
import com.example.productmanagement.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    @Test
    public void testCreateRoles () {
        Role user = new Role("User");
        Role admin = new Role("Admin");
        Role customer = new Role("Customer");

        roleRepository.save(user);
        roleRepository.save(admin);
        roleRepository.save(customer);

        List<Role> list = roleRepository.findAll();

        if (list.size() == 3) {
            System.out.println("true");
        }
    }
}
