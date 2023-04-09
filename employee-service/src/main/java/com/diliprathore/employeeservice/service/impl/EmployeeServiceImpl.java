package com.diliprathore.employeeservice.service.impl;

import com.diliprathore.employeeservice.dto.EmployeeDto;
import com.diliprathore.employeeservice.entity.Employee;
import com.diliprathore.employeeservice.exception.EmailAlreadyExistsException;
import com.diliprathore.employeeservice.exception.ResourceNotFoundException;
import com.diliprathore.employeeservice.repository.EmployeeRepository;
import com.diliprathore.employeeservice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto) {
        Employee employee = modelMapper.map(employeeDto, Employee.class);

        String email = employee.getEmail();
        Optional<Employee> foundEmail = employeeRepository.findByEmail(email);
        if (foundEmail.isPresent()) {
            throw new EmailAlreadyExistsException("Email id already exists");
        }
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDto.class);
    }

    @Override
    public EmployeeDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );
        return modelMapper.map(employee, EmployeeDto.class);
    }
}
