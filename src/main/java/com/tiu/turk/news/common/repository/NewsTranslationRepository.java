package com.tiu.turk.news.common.repository;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsTranslationRepository
extends JpaRepository<NewsTranslationEntity, Long> {
    @Query(value="    select t from NewsTranslationEntity t\n    join fetch t.news n\n    where t.locale = (case when exists (select 1 from NewsTranslationEntity nx where nx.news = n and nx.locale = :locale) then :locale else 'EN' end) and n.category.id = :categoryId\n    order by n.createdAt desc\n")
    public Page<NewsTranslationEntity> findAllByLocaleAndCategoryId(@Param(value="locale") String var1, @Param(value="categoryId") Long var2, Pageable var3);

    public Optional<NewsTranslationEntity> findByNewsIdAndLocale(Long var1, TranslationLocale var2);

    public boolean existsByNewsIdAndLocale(Long var1, String var2);
}

