package com.tiu.turk.webinar.common.repository;

import com.tiu.turk.webinar.common.entity.WebinarRegistrationEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WebinarRegistrationRepository
extends JpaRepository<WebinarRegistrationEntity, Long> {
    long countByWebinar_Id(Long webinarId);

    boolean existsByWebinar_IdAndEmailIgnoreCase(Long webinarId, String email);

    List<WebinarRegistrationEntity> findByWebinar_IdOrderByCreatedAtDesc(Long webinarId);
}
