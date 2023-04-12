package com.diliprathore.organizationservice.service.impl;

import com.diliprathore.organizationservice.dto.OrganizationDto;
import com.diliprathore.organizationservice.entity.Organization;
import com.diliprathore.organizationservice.mapper.OrganizationMapper;
import com.diliprathore.organizationservice.repository.OrganizationRepository;
import com.diliprathore.organizationservice.service.OrganizationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {
    private OrganizationRepository organizationRepository;

    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto){
        Organization organization = OrganizationMapper.mapToOrganization(organizationDto);
        Organization savedOrganization = organizationRepository.save(organization);
        OrganizationDto savedOrganizationDto = OrganizationMapper.mapToOrganizationDto(savedOrganization);
        return savedOrganizationDto;
    }

    @Override
    public OrganizationDto getByOrganizationCode(String code) {
        Organization foundOrganization = organizationRepository.findByOrganizationCode(code);
        OrganizationDto foundOrganizationDto = OrganizationMapper.mapToOrganizationDto(foundOrganization);
        return foundOrganizationDto;
    }


}
