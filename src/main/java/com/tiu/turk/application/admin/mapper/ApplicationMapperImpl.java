package com.tiu.turk.application.admin.mapper;

import com.tiu.turk.application.admin.dto.ApplicationDto;
import com.tiu.turk.application.admin.mapper.ApplicationMapper;
import com.tiu.turk.application.common.entity.ApplicationEntity;
import com.tiu.turk.user.admin.dto.UserDto;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMapperImpl
implements ApplicationMapper {
    @Autowired
    private UserMapper userMapper;

    public ApplicationDto toDto(ApplicationEntity entity) {
        if (entity == null) {
            return null;
        }
        Long id = null;
        String fullName = null;
        String studentId = null;
        String institution = null;
        String department = null;
        String phoneNumber = null;
        UserDto author = null;
        LocalDateTime updatedAt = null;
        LocalDateTime createdAt = null;
        id = entity.getId();
        fullName = entity.getFullName();
        studentId = entity.getStudentId();
        institution = entity.getInstitution();
        department = entity.getDepartment();
        phoneNumber = entity.getPhoneNumber();
        author = this.userMapper.toDto(entity.getAuthor());
        updatedAt = entity.getUpdatedAt();
        createdAt = entity.getCreatedAt();
        ApplicationDto applicationDto = new ApplicationDto(id, fullName, studentId, institution, department, phoneNumber, author, updatedAt, createdAt);
        return applicationDto;
    }
}

