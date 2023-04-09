package com.diliprathore.employeeservice.service;

import com.diliprathore.employeeservice.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    EmployeeDto getEmployee(Long id);
}
