package com.diliprathore.departmentservice.repository;

import com.diliprathore.departmentservice.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByDepartmentCode(String departmentCode); // query method
}
