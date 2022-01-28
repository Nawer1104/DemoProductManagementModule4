package com.example.productmanagement;

import com.example.productmanagement.model.Employee;
import com.example.productmanagement.model.Role;
import com.example.productmanagement.repository.EmployeeRepository;
import com.example.productmanagement.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class EmployeeRepoTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    public void testFindEmployeeByNumber () {
        long number = 400457;
        Employee employee = employeeRepository.findByNumber(number);
        if (employee != null) {
            System.out.println("true");
        }
    }

    @Test
    public void testAddRoleToNewUser() {
        Employee employee = new Employee();
        employee.setNumber(400111);
        employee.setPassword("hoangnam");
        employee.setFirstName("Na");
        employee.setLastName("Na");

        Role roleEm = roleRepository.findRoleByName("Partner");
        employee.addRole(roleEm);

        Employee savedEm = employeeRepository.save(employee);
        if (savedEm.getRoles().size() == 1) {
            System.out.println("true");;
        }
    }

}
