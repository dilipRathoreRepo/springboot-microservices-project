package com.diliprathore.employeeservice.service;

import com.diliprathore.employeeservice.dto.DepartmentDto;
import com.diliprathore.employeeservice.dto.OrganizationDto;
import jakarta.ws.rs.Path;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// FeignClient library will dynamically create an implementation for this interface

//@FeignClient(url = "http://localhost:8080/", value = "DEPARTMENT-SERVICE")
@FeignClient(name="DEPARTMENT-SERVICE")
public interface APIClient {
    // Build get department REST API
    @GetMapping("api/departments/{department-code}")
    DepartmentDto getDepartment(@PathVariable(name = "department-code") String departmentCode);

    @GetMapping("api/organizations/{organization-code}")
    OrganizationDto getOrganization(@PathVariable(name="organization-code") String organizationCode);
}
