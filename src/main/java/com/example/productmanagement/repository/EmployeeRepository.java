package com.example.productmanagement.repository;

import com.example.productmanagement.model.Employee;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    @Query("SELECT e FROM Employee e WHERE e.number = ?1")
    Employee findByNumber(long number);

//    @Query("SELECT e FROM Employee e WHERE e.id not  ?1")
    List<Employee> findEmployeeByIdIsNotLike (int id);
}
