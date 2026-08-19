package com.tiu.turk.predeparture.common.repository;

import com.tiu.turk.predeparture.common.entity.PreDepartureResourceEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PreDepartureResourceRepository
extends JpaRepository<PreDepartureResourceEntity, Long> {
    List<PreDepartureResourceEntity> findByEnabledTrueOrderByCreatedAtDesc();

    List<PreDepartureResourceEntity> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths={"translations", "file"})
    @Query("select r from PreDepartureResourceEntity r where r.enabled = true order by r.createdAt desc")
    List<PreDepartureResourceEntity> findEnabledWithTranslations();

    @EntityGraph(attributePaths={"translations", "file"})
    @Query("select r from PreDepartureResourceEntity r where r.id = :id")
    Optional<PreDepartureResourceEntity> findByIdWithTranslations(@Param("id") Long id);
}
