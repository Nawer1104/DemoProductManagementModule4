package com.example.productmanagement.service.impl;

import com.example.productmanagement.service.CustomEmployeeDetails;
import com.example.productmanagement.model.Employee;
import com.example.productmanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomEployeeDetailsService implements UserDetailsService {
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String number) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByNumber(Long.parseLong(number));
        if (employee == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomEmployeeDetails(employee);
    }
}
