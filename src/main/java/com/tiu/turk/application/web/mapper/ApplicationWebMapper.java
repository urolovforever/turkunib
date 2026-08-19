package com.tiu.turk.application.web.mapper;

import com.tiu.turk.application.common.entity.ApplicationEntity;
import com.tiu.turk.application.web.dto.ApplicationCreateDto;

public interface ApplicationWebMapper {
    public ApplicationEntity toEntity(ApplicationCreateDto var1);
}

