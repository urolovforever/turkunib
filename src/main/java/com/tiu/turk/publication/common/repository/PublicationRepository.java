package com.tiu.turk.publication.common.repository;

import com.tiu.turk.publication.common.PublicationCategory;
import com.tiu.turk.publication.common.entity.PublicationEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PublicationRepository
extends JpaRepository<PublicationEntity, Long> {
    List<PublicationEntity> findByEnabledTrueOrderByCreatedAtDesc();

    List<PublicationEntity> findByEnabledTrueAndCategoryOrderByCreatedAtDesc(PublicationCategory category);

    List<PublicationEntity> findAllByOrderByCreatedAtDesc();

    @EntityGraph(attributePaths={"translations", "file"})
    @Query("select p from PublicationEntity p where p.enabled = true order by p.createdAt desc")
    List<PublicationEntity> findEnabledWithTranslations();

    @EntityGraph(attributePaths={"translations", "file"})
    @Query("select p from PublicationEntity p where p.enabled = true and p.category = :category order by p.createdAt desc")
    List<PublicationEntity> findEnabledByCategoryWithTranslations(@Param("category") PublicationCategory category);

    @EntityGraph(attributePaths={"translations"})
    @Query("select p from PublicationEntity p where p.id = :id")
    Optional<PublicationEntity> findByIdWithTranslations(@Param("id") Long id);

    @Query("select distinct p from PublicationEntity p left join p.translations t where p.enabled = true and ("
            + "lower(p.title) like :q or lower(p.description) like :q"
            + " or lower(t.title) like :q or lower(t.description) like :q)")
    List<PublicationEntity> search(@Param("q") String q);
}
