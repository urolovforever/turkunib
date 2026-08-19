package com.tiu.turk.faq.common.repository;

import com.tiu.turk.faq.common.entity.FaqEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FaqRepository
extends JpaRepository<FaqEntity, Long> {
    List<FaqEntity> findByEnabledTrueOrderByCategoryAscDisplayOrderAscIdAsc();

    List<FaqEntity> findAllByOrderByCategoryAscDisplayOrderAscIdAsc();

    @EntityGraph(attributePaths={"translations"})
    @Query("select f from FaqEntity f where f.enabled = true order by f.category asc, f.displayOrder asc, f.id asc")
    List<FaqEntity> findEnabledWithTranslations();

    @EntityGraph(attributePaths={"translations"})
    @Query("select f from FaqEntity f where f.id = :id")
    Optional<FaqEntity> findByIdWithTranslations(@Param("id") Long id);

    @Query("select distinct f from FaqEntity f left join f.translations t where f.enabled = true and ("
            + "lower(f.question) like :q or lower(f.answer) like :q"
            + " or lower(t.question) like :q or lower(t.answer) like :q)")
    List<FaqEntity> search(@Param("q") String q);
}
