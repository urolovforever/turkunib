package com.tiu.turk.research.common.repository;

import com.tiu.turk.research.common.ResearchProjectStatus;
import com.tiu.turk.research.common.entity.ResearchProjectEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ResearchProjectRepository
extends JpaRepository<ResearchProjectEntity, Long> {
    List<ResearchProjectEntity> findByEnabledTrueOrderByCreatedAtDesc();

    List<ResearchProjectEntity> findByEnabledTrueAndStatusOrderByCreatedAtDesc(ResearchProjectStatus status);

    List<ResearchProjectEntity> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths={"translations"})
    @Query("select p from ResearchProjectEntity p where p.enabled = true order by p.createdAt desc")
    List<ResearchProjectEntity> findEnabledWithTranslations();

    @EntityGraph(attributePaths={"translations"})
    @Query("select p from ResearchProjectEntity p where p.enabled = true and p.status = :status order by p.createdAt desc")
    List<ResearchProjectEntity> findEnabledByStatusWithTranslations(@Param("status") ResearchProjectStatus status);

    @EntityGraph(attributePaths={"translations"})
    @Query("select p from ResearchProjectEntity p where p.id = :id")
    Optional<ResearchProjectEntity> findByIdWithTranslations(@Param("id") Long id);

    @Query("select distinct p from ResearchProjectEntity p left join p.translations t where p.enabled = true and ("
            + "lower(p.title) like :q or lower(p.description) like :q or lower(p.content) like :q or lower(p.subjectArea) like :q"
            + " or lower(t.title) like :q or lower(t.description) like :q or lower(t.content) like :q or lower(t.subjectArea) like :q)")
    List<ResearchProjectEntity> search(@Param("q") String q);
}
