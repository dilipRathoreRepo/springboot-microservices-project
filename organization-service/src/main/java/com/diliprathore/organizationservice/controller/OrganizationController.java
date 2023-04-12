package com.diliprathore.organizationservice.controller;

import com.diliprathore.organizationservice.dto.OrganizationDto;
import com.diliprathore.organizationservice.entity.Organization;
import com.diliprathore.organizationservice.mapper.OrganizationMapper;
import com.diliprathore.organizationservice.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/organizations")
@AllArgsConstructor
public class OrganizationController {
    private OrganizationService organizationService;

    @PostMapping
    public ResponseEntity<OrganizationDto> saveOrganization(@RequestBody OrganizationDto organizationDto){
        OrganizationDto savedOrganizationDto = organizationService.saveOrganization(organizationDto);
        return new ResponseEntity<>(savedOrganizationDto, HttpStatus.CREATED);
    }

    @GetMapping("{code}")
    public ResponseEntity<OrganizationDto> findByOrganizationCode(@PathVariable(name = "code") String organizationCode){
        return new ResponseEntity<>(organizationService.getByOrganizationCode(organizationCode), HttpStatus.OK);
    }
}
