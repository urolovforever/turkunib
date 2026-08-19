package com.tiu.turk.staticpage.common.repository;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.staticpage.common.dto.StaticPageSingleTranslationDto;
import com.tiu.turk.staticpage.common.entity.StaticPageEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StaticPageRepository
extends JpaRepository<StaticPageEntity, Long> {
    @Query(value="SELECT new com.tiu.turk.staticpage.common.dto.StaticPageSingleTranslationDto(\n    sp.id,\n    sp.slug,\n    t.title,\n    t.content\n)\nFROM StaticPageEntity sp\nJOIN sp.translations t\nWHERE sp.slug = :slug AND t.locale = (case when exists (select 1 from StaticPageTranslationEntity spx where spx.staticPage = sp and spx.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end) AND sp.enabled = true\n")
    public Optional<StaticPageSingleTranslationDto> findStaticPageBySlugAndLocale(@Param("slug") String slug, @Param("locale") TranslationLocale locale);
}

