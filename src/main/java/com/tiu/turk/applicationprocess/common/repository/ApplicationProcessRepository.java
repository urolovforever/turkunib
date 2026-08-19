package com.tiu.turk.applicationprocess.common.repository;

import com.tiu.turk.applicationprocess.common.entity.ApplicationProcessEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationProcessRepository
extends JpaRepository<ApplicationProcessEntity, Long> {
    List<ApplicationProcessEntity> findByUniversitySentIdOrUniversityReceivedId(Long sentId, Long receivedId);
}
