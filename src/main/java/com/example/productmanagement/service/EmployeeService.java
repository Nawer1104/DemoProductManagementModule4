package com.example.productmanagement.service;

import com.example.productmanagement.model.Category;
import com.example.productmanagement.model.Employee;
import com.example.productmanagement.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService {
    List<Employee> findAll();
    void saveUserWithDeafultRole(Employee employee);
    void delete(int id);
    Employee getEmployee(int id);
    List<Employee> getEmployeeExceptItSelf(int id);
    List<Role> getRoles ();
    void save(Employee employee);
}
