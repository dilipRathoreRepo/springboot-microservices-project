package com.diliprathore.employeeservice.service;

import com.diliprathore.employeeservice.dto.APIResponseDto;
import com.diliprathore.employeeservice.dto.EmployeeDto;

public interface EmployeeService {
    EmployeeDto saveEmployee(EmployeeDto employeeDto);
    APIResponseDto getEmployee(Long id);
}
