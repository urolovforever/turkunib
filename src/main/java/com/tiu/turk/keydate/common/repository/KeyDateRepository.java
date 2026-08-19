package com.tiu.turk.keydate.common.repository;

import com.tiu.turk.keydate.common.entity.KeyDateEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KeyDateRepository
extends JpaRepository<KeyDateEntity, Long> {
    List<KeyDateEntity> findByEnabledTrueOrderByEventDateAsc();

    List<KeyDateEntity> findAllByOrderByEventDateAsc();

    @EntityGraph(attributePaths={"translations"})
    @Query("select k from KeyDateEntity k where k.enabled = true order by k.eventDate asc")
    List<KeyDateEntity> findEnabledWithTranslations();

    @EntityGraph(attributePaths={"translations"})
    @Query("select d from KeyDateEntity d where d.id = :id")
    Optional<KeyDateEntity> findByIdWithTranslations(@Param("id") Long id);
}
