package com.diliprathore.organizationservice.service;

import com.diliprathore.organizationservice.dto.OrganizationDto;

public interface OrganizationService {
    OrganizationDto saveOrganization(OrganizationDto organizationDto);
    OrganizationDto getByOrganizationCode(String code);
}
