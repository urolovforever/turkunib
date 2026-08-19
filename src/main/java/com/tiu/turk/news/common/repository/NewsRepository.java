package com.tiu.turk.news.common.repository;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.news.common.dto.NewsSingleTranslationDto;
import com.tiu.turk.news.common.entity.NewsEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository
extends JpaRepository<NewsEntity, Long> {
    @Query(value="SELECT * FROM news\nWHERE MATCH(title, content, description) AGAINST (?q IN NATURAL LANGUAGE MODE)\n", countQuery="SELECT COUNT(*) FROM news\nWHERE MATCH(title, content, description) AGAINST (?q IN NATURAL LANGUAGE MODE)\n", nativeQuery=true)
    public Page<NewsEntity> searchFulltext(@Param(value="q") String q, Pageable pageable);

    @Query(value="    select new com.tiu.turk.news.common.dto.NewsSingleTranslationDto(\n            n.id, t.locale, t.title, t.slug, t.content, t.description,\n            n.image, n.category, n.authorName, n.createdAt\n    )\n    from NewsEntity n\n    join n.translations t\n    where n.enabled = true\n    and t.locale = (case when exists (select 1 from NewsTranslationEntity nx where nx.news = n and nx.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\n    order by n.createdAt desc\n")
    public List<NewsSingleTranslationDto> findActiveNews(@Param("locale") TranslationLocale locale, Pageable pageable);

    @Query(value="    select new com.tiu.turk.news.common.dto.NewsSingleTranslationDto(\n            n.id, t.locale, t.title, t.slug, t.content, t.description,\n            n.image, n.category, n.authorName, n.createdAt\n    )\n    from NewsEntity n\n    join n.translations t\n    where n.enabled = true\n    and t.locale = (case when exists (select 1 from NewsTranslationEntity nx where nx.news = n and nx.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\n    order by n.createdAt desc\n")
    public Page<NewsSingleTranslationDto> findActiveNewsPage(@Param("locale") TranslationLocale locale, Pageable pageable);

    @Query(value="    select new com.tiu.turk.news.common.dto.NewsSingleTranslationDto(\n            n.id, t.locale, t.title, t.slug, t.content, t.description,\n            n.image, n.category, n.authorName, n.createdAt\n    )\n    from NewsEntity n\n    join n.translations t\n    where n.enabled = true\n    and n.id = :id\n    and t.locale = (case when exists (select 1 from NewsTranslationEntity nx where nx.news = n and nx.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\n")
    public Optional<NewsSingleTranslationDto> findActiveNewsByIdWithTranslations(@Param("id") Long id, @Param("locale") TranslationLocale locale);

    @Query("select distinct n from NewsEntity n join n.translations t where n.enabled = true and ("
            + "lower(t.title) like :q or lower(t.content) like :q or lower(t.description) like :q) order by n.createdAt desc")
    List<NewsEntity> search(@Param("q") String q);
}

