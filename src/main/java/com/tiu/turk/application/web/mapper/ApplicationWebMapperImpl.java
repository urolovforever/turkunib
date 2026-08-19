package com.tiu.turk.application.web.mapper;

import com.tiu.turk.application.common.entity.ApplicationEntity;
import com.tiu.turk.application.web.dto.ApplicationCreateDto;
import com.tiu.turk.application.web.mapper.ApplicationWebMapper;
import org.springframework.stereotype.Component;

@Component
public class ApplicationWebMapperImpl
implements ApplicationWebMapper {
    public ApplicationEntity toEntity(ApplicationCreateDto dto) {
        if (dto == null) {
            return null;
        }
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setFullName(dto.fullName());
        applicationEntity.setStudentId(dto.studentId());
        applicationEntity.setInstitution(dto.institution());
        applicationEntity.setDepartment(dto.department());
        applicationEntity.setPhoneNumber(dto.phoneNumber());
        return applicationEntity;
    }
}

