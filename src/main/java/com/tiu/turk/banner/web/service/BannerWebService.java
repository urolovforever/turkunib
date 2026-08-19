package com.tiu.turk.banner.web.service;

import com.tiu.turk.banner.common.dto.BannerSingleTranslationDto;
import com.tiu.turk.banner.common.repository.BannerRepository;
import com.tiu.turk.common.enums.TranslationLocale;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Service;

@Service
public class BannerWebService {
    private final BannerRepository bannerRepository;

    public List<BannerSingleTranslationDto> getActiveBanners(TranslationLocale locale) {
        return this.bannerRepository.findActiveBanners(locale);
    }

    @Generated
    public BannerWebService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }
}

