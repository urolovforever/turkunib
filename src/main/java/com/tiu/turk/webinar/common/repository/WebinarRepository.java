package com.tiu.turk.webinar.common.repository;

import com.tiu.turk.webinar.common.entity.WebinarEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WebinarRepository
extends JpaRepository<WebinarEntity, Long> {
    List<WebinarEntity> findByEnabledTrueOrderByStartAtDesc();

    List<WebinarEntity> findAllByOrderByStartAtDesc();

    @EntityGraph(attributePaths={"translations"})
    @Query("select w from WebinarEntity w where w.enabled = true order by w.startAt desc")
    List<WebinarEntity> findEnabledWithTranslations();

    @EntityGraph(attributePaths={"translations"})
    @Query("select w from WebinarEntity w where w.id = :id")
    Optional<WebinarEntity> findByIdWithTranslations(@Param("id") Long id);

    @Query("select distinct w from WebinarEntity w left join w.translations t where w.enabled = true and ("
            + "lower(w.title) like :q or lower(w.description) like :q or lower(w.content) like :q"
            + " or lower(t.title) like :q or lower(t.description) like :q or lower(t.content) like :q)")
    List<WebinarEntity> search(@Param("q") String q);
}
