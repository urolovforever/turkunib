package com.tiu.turk.applicationprocess.admin.mapper;

import com.tiu.turk.applicationprocess.admin.dto.ApplicationProcessDto;
import com.tiu.turk.applicationprocess.admin.mapper.ApplicationProcessMapper;
import com.tiu.turk.applicationprocess.common.entity.ApplicationProcessEntity;
import com.tiu.turk.files.FileMapper;
import com.tiu.turk.member.admin.mapper.MemberMapper;
import com.tiu.turk.user.admin.mapper.UserMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProcessMapperImpl
implements ApplicationProcessMapper {
    @Autowired
    private FileMapper fileMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MemberMapper memberMapper;

    public ApplicationProcessDto toDto(ApplicationProcessEntity entity) {
        if (entity == null) {
            return null;
        }
        ApplicationProcessDto applicationProcessDto = new ApplicationProcessDto();
        applicationProcessDto.setId(entity.getId());
        applicationProcessDto.setUniversitySent(this.memberMapper.toDto(entity.getUniversitySent()));
        applicationProcessDto.setUniversityReceived(this.memberMapper.toDto(entity.getUniversityReceived()));
        applicationProcessDto.setFile(this.fileMapper.toDto(entity.getFile()));
        applicationProcessDto.setComment(entity.getComment());
        applicationProcessDto.setStatus(entity.getStatus());
        applicationProcessDto.setAuthor(this.userMapper.toDto(entity.getAuthor()));
        applicationProcessDto.setUpdatedAt(entity.getUpdatedAt());
        applicationProcessDto.setCreatedAt(entity.getCreatedAt());
        return applicationProcessDto;
    }

    public List<ApplicationProcessDto> toDtoList(List<ApplicationProcessEntity> entities) {
        if (entities == null) {
            return null;
        }
        ArrayList<ApplicationProcessDto> list = new ArrayList<ApplicationProcessDto>(entities.size());
        for (ApplicationProcessEntity applicationProcessEntity : entities) {
            list.add(this.toDto(applicationProcessEntity));
        }
        return list;
    }
}

