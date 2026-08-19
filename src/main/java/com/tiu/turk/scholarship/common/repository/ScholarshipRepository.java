package com.tiu.turk.scholarship.common.repository;

import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ScholarshipRepository
extends JpaRepository<ScholarshipEntity, Long> {
    List<ScholarshipEntity> findByEnabledTrueOrderByDisplayOrderAscIdAsc();

    List<ScholarshipEntity> findAllByOrderByDisplayOrderAscIdAsc();

    @EntityGraph(attributePaths={"translations", "document"})
    @Query("select s from ScholarshipEntity s where s.enabled = true order by s.displayOrder asc, s.id asc")
    List<ScholarshipEntity> findEnabledWithTranslations();

    @EntityGraph(attributePaths={"translations", "document"})
    @Query("select s from ScholarshipEntity s where s.id = :id")
    Optional<ScholarshipEntity> findByIdWithTranslations(@Param("id") Long id);

    @Query("select distinct s from ScholarshipEntity s left join s.translations t where s.enabled = true and ("
            + "lower(s.title) like :q or lower(s.description) like :q or lower(s.content) like :q or lower(s.provider) like :q"
            + " or lower(t.title) like :q or lower(t.description) like :q or lower(t.content) like :q or lower(t.provider) like :q)")
    List<ScholarshipEntity> search(@Param("q") String q);
}
