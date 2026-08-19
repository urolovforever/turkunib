package com.tiu.turk.institutionaldoc.common.repository;

import com.tiu.turk.institutionaldoc.common.InstitutionalDocumentSection;
import com.tiu.turk.institutionaldoc.common.entity.InstitutionalDocumentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InstitutionalDocumentRepository
extends JpaRepository<InstitutionalDocumentEntity, Long> {
    List<InstitutionalDocumentEntity> findByEnabledTrueAndSectionOrderByDocumentYearDescIdDesc(InstitutionalDocumentSection section);

    List<InstitutionalDocumentEntity> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths={"translations", "file"})
    @Query("select d from InstitutionalDocumentEntity d where d.enabled = true and d.section = :section order by d.documentYear desc, d.id desc")
    List<InstitutionalDocumentEntity> findBySectionWithTranslations(@Param("section") InstitutionalDocumentSection section);

    @EntityGraph(attributePaths={"translations", "file"})
    @Query("select d from InstitutionalDocumentEntity d where d.id = :id")
    Optional<InstitutionalDocumentEntity> findByIdWithTranslations(@Param("id") Long id);
}
