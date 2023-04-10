package com.diliprathore.employeeservice.service.impl;

import com.diliprathore.employeeservice.dto.APIResponseDto;
import com.diliprathore.employeeservice.dto.DepartmentDto;
import com.diliprathore.employeeservice.dto.EmployeeDto;
import com.diliprathore.employeeservice.entity.Employee;
import com.diliprathore.employeeservice.exception.EmailAlreadyExistsException;
import com.diliprathore.employeeservice.exception.ResourceNotFoundException;
import com.diliprathore.employeeservice.repository.EmployeeRepository;
import com.diliprathore.employeeservice.service.APIClient;
import com.diliprathore.employeeservice.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;
//    private RestTemplate restTemplate;
//    private WebClient webClient;
    private APIClient apiClient;

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
    public APIResponseDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        System.out.println("TODO EmployeeServiceImpl.java : employee.getDepartmentCode() : " + employee.getDepartmentCode());

//        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/" + employee.getDepartmentCode(),
//                DepartmentDto.class);

//        DepartmentDto departmentDto = responseEntity.getBody();

//        DepartmentDto departmentDto = webClient.get()
//                .uri("http://localhost:8080/api/departments/" + employee.getDepartmentCode())
//                .retrieve()
//                .bodyToMono(DepartmentDto.class)
//                .block();

        DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);

        return apiResponseDto;
    }
}
