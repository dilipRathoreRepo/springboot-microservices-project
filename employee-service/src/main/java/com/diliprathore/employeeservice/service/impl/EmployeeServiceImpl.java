package com.diliprathore.employeeservice.service.impl;

import com.diliprathore.employeeservice.dto.APIResponseDto;
import com.diliprathore.employeeservice.dto.DepartmentDto;
import com.diliprathore.employeeservice.dto.EmployeeDto;
import com.diliprathore.employeeservice.dto.OrganizationDto;
import com.diliprathore.employeeservice.entity.Employee;
import com.diliprathore.employeeservice.exception.EmailAlreadyExistsException;
import com.diliprathore.employeeservice.exception.ResourceNotFoundException;
import com.diliprathore.employeeservice.repository.EmployeeRepository;
import com.diliprathore.employeeservice.service.APIClient;
import com.diliprathore.employeeservice.service.EmployeeService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;
    //    private RestTemplate restTemplate;
    private WebClient webClient;
//    private APIClient apiClient;

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

    //    @CircuitBreaker(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartment")
    @Retry(name = "${spring.application.name}", fallbackMethod = "getDefaultDepartmentAndOrganization")
    @Override
    public APIResponseDto getEmployee(Long id) {

        LOGGER.info("Inside getEmployee() method");

        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        LOGGER.info("TODO EmployeeServiceImpl.java : employee.getDepartmentCode() : " + employee.getDepartmentCode());
        LOGGER.info("TODO employee organization code is : " + employee.getOrganizationCode());

//        ResponseEntity<DepartmentDto> responseEntity = restTemplate.getForEntity("http://localhost:8080/api/departments/" + employee.getDepartmentCode(),
//                DepartmentDto.class);

//        DepartmentDto departmentDto = responseEntity.getBody();

        DepartmentDto departmentDto = webClient.get()
                .uri("http://localhost:8089/api/departments/" + employee.getDepartmentCode())
                .retrieve()
                .bodyToMono(DepartmentDto.class)
                .block();

        OrganizationDto organizationDto = webClient.get()
                .uri("http://localhost:8084/api/organizations/" + employee.getOrganizationCode())
                .retrieve()
                .bodyToMono(OrganizationDto.class)
                .block();

//        DepartmentDto departmentDto = apiClient.getDepartment(employee.getDepartmentCode());
//        OrganizationDto organizationDto = apiClient.getOrganization(employee.getOrganizationCode());

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);
        apiResponseDto.setOrganization(organizationDto);

        return apiResponseDto;
    }

    public APIResponseDto getDefaultDepartmentAndOrganization(Long id, Exception exception) {
        LOGGER.info("Inside getDefaultDepartmentAndOrganization() method");
        Employee employee = employeeRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", id)
        );

        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);

        DepartmentDto departmentDto = new DepartmentDto();
        departmentDto.setDepartmentName("R&D Department");
        departmentDto.setDepartmentCode("RD001");
        departmentDto.setDepartmentDescription("Research and Development Department");

        OrganizationDto organizationDto = new OrganizationDto();
        organizationDto.setOrganizationCode("DEFAULT_001");
        organizationDto.setOrganizationName("Default organization");
        organizationDto.setOrganizationDescription("Default organization description");

        APIResponseDto apiResponseDto = new APIResponseDto();
        apiResponseDto.setEmployee(employeeDto);
        apiResponseDto.setDepartment(departmentDto);
        apiResponseDto.setOrganization(organizationDto);

        return apiResponseDto;
    }
}
