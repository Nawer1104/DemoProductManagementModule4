package com.example.productmanagement.service;

import com.example.productmanagement.model.Employee;
import com.example.productmanagement.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class CustomEmployeeDetails implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Employee employee;

    public CustomEmployeeDetails(Employee employee) {
        this.employee = employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Role> roles = employee.getRoles();
        List<SimpleGrantedAuthority>  authorities = new ArrayList<>();
        for (Role r : roles) {
            authorities.add(new SimpleGrantedAuthority(r.getName()));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return  String.valueOf(employee.getNumber());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public String getFullName() {
        return employee.getFirstName() + " " + employee.getLastName();
    }

    public void setFirstName (String firstName) {
        this.employee.setFirstName(firstName);
    }

    public void setLastName (String lastName) {
        this.employee.setLastName(lastName);
    }

    public Employee getLoggedEmployee () {
        return this.employee;
    }
}
