package com.secureon.service.impl;

import com.secureon.domain.model.Employee;
import com.secureon.repository.EmployeeRepository;
import com.secureon.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceImpl  implements EmployeeService {

    @Autowired
    private  EmployeeRepository employeeRepository;

    @Override
    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return  employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getUserById(Long id) {
        return employeeRepository.findAllByUserId(id);
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
