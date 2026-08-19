package com.tiu.turk.banner.common.repository;

import com.tiu.turk.banner.common.dto.BannerSingleTranslationDto;
import com.tiu.turk.banner.common.entity.BannerEntity;
import com.tiu.turk.common.enums.TranslationLocale;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BannerRepository
extends JpaRepository<BannerEntity, Long> {
    @Query(value="    SELECT new com.tiu.turk.banner.common.dto.BannerSingleTranslationDto(\n                b.id, t.locale, t.title, t.urlTitle, t.shortTitle, t.description, b.url,\n                b.position, b.image, b.createdAt\n    )\n    FROM BannerEntity b\n    JOIN b.translations t\n    WHERE b.enabled = true AND t.locale = (case when exists (select 1 from BannerTranslationEntity bx where bx.banner = b and bx.locale = :locale) then :locale else com.tiu.turk.common.enums.TranslationLocale.EN end)\n    ORDER BY b.position ASC\n")
    public List<BannerSingleTranslationDto> findActiveBanners(@Param("locale") TranslationLocale locale);
}

