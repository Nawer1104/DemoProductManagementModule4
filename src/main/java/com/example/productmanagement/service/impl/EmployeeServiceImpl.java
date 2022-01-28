package com.example.productmanagement.service.impl;

import com.example.productmanagement.model.Employee;
import com.example.productmanagement.model.Product;
import com.example.productmanagement.model.Role;
import com.example.productmanagement.repository.EmployeeRepository;
import com.example.productmanagement.repository.RoleRepository;
import com.example.productmanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    @Override
    public void saveUserWithDeafultRole(Employee employee) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String endcodePass = bCryptPasswordEncoder.encode(employee.getPassword());
        employee.setPassword(endcodePass);

        Role roleUser = roleRepository.findRoleByName("Partner");
        employee.addRole(roleUser);
        employeeRepository.save(employee);
    }

    @Override
    public void delete(int id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee getEmployee(int id) {
        Optional<Employee> result = employeeRepository.findById(id);
        return result.get();
    }

    @Override
    public List<Employee> getEmployeeExceptItSelf(int id) {
        return employeeRepository.findEmployeeByIdIsNotLike(id);
    }

    @Override
    public List<Role> getRoles () {
        return roleRepository.findAll();
    }

    @Override
    public void save (Employee employee) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String endcodePass = bCryptPasswordEncoder.encode(employee.getPassword());
        employee.setPassword(endcodePass);
        employeeRepository.save(employee);
    }
}
