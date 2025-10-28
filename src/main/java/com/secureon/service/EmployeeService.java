package com.secureon.service;

import com.secureon.domain.model.Employee;

import java.util.List;

public interface EmployeeService {

    List<Employee> getAllEmployee();
    Employee createEmployee(Employee employee);
    List<Employee> getUserById(Long id);

}
