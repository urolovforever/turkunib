package com.tiu.turk.translation.service;

import com.tiu.turk.banner.common.entity.BannerEntity;
import com.tiu.turk.banner.common.entity.BannerTranslationEntity;
import com.tiu.turk.banner.common.repository.BannerRepository;
import com.tiu.turk.common.enums.AppModule;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.event.common.entity.EventEntity;
import com.tiu.turk.event.common.entity.EventTranslationEntity;
import com.tiu.turk.event.common.repository.EventRepository;
import com.tiu.turk.news.common.entity.NewsCategoryEntity;
import com.tiu.turk.news.common.entity.NewsCategoryTranslationEntity;
import com.tiu.turk.news.common.entity.NewsEntity;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;
import com.tiu.turk.news.common.repository.NewsCategoryRepository;
import com.tiu.turk.news.common.repository.NewsRepository;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryEntity;
import com.tiu.turk.photogallery.common.entity.PhotoGalleryTranslationEntity;
import com.tiu.turk.photogallery.common.repository.PhotoGalleryRepository;
import com.tiu.turk.staticpage.common.entity.StaticPageEntity;
import com.tiu.turk.staticpage.common.entity.StaticPageTranslationEntity;
import com.tiu.turk.staticpage.common.repository.StaticPageRepository;
import com.tiu.turk.translation.entity.TranslationTaskEntity;
import com.tiu.turk.translation.repository.TranslationTaskRepository;
import com.tiu.turk.translation.service.TranslationTaskService;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class TranslationTaskService {
    private final TranslationTaskRepository translationTaskRepository;
    private final EventRepository eventRepository;
    private final NewsRepository newsRepository;
    private final NewsCategoryRepository newsCategoryRepository;
    private final BannerRepository bannerRepository;
    private final PhotoGalleryRepository photoGalleryRepository;
    private final StaticPageRepository staticPageRepository;
    private static final String KEY_TITLE = "title";

    public Page<TranslationTaskEntity> getTasksByModule(AppModule module, int page, int size) {
        return this.translationTaskRepository.findAllByModule(module, (Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public Page<TranslationTaskEntity> getAllByModuleAndEntityId(AppModule module, Long entityId, int page, int size) {
        return this.translationTaskRepository.findAllByModuleAndEntityId(module, entityId, (Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public Map<String, String> getEntityMetaData(AppModule module, Long entityId) {
        Map<String, String> metaData = new HashMap<String, String>();
        switch (module) {
            case EVENT: {
                Optional<EventEntity> event = this.eventRepository.findById(entityId);
                if (!event.isPresent()) break;
                metaData = Map.of(KEY_TITLE, ((EventTranslationEntity)event.get().getTranslations().get(TranslationLocale.EN)).getTitle());
                break;
            }
            case NEWS: {
                Optional<NewsEntity> news = this.newsRepository.findById(entityId);
                if (!news.isPresent()) break;
                metaData = Map.of(KEY_TITLE, ((NewsTranslationEntity)news.get().getTranslations().get(TranslationLocale.EN)).getTitle());
                break;
            }
            case NEWS_CATEGORY: {
                Optional<NewsCategoryEntity> category = this.newsCategoryRepository.findById(entityId);
                if (!category.isPresent()) break;
                metaData = Map.of(KEY_TITLE, ((NewsCategoryTranslationEntity)category.get().getTranslations().get(TranslationLocale.EN)).getTitle());
                break;
            }
            case BANNER: {
                Optional<BannerEntity> banner = this.bannerRepository.findById(entityId);
                if (!banner.isPresent()) break;
                metaData = Map.of(KEY_TITLE, ((BannerTranslationEntity)banner.get().getTranslations().get(TranslationLocale.EN)).getTitle());
                break;
            }
            case PHOTO_GALLERY: {
                Optional<PhotoGalleryEntity> gallery = this.photoGalleryRepository.findById(entityId);
                if (!gallery.isPresent()) break;
                metaData = Map.of(KEY_TITLE, ((PhotoGalleryTranslationEntity)gallery.get().getTranslations().get(TranslationLocale.EN)).getTitle());
                break;
            }
            case STATIC_PAGE: {
                Optional<StaticPageEntity> staticPage = this.staticPageRepository.findById(entityId);
                if (!staticPage.isPresent()) break;
                metaData = Map.of(KEY_TITLE, ((StaticPageTranslationEntity)staticPage.get().getTranslations().get(TranslationLocale.EN)).getTitle());
            }
        }
        if (metaData.isEmpty()) {
            metaData.put(KEY_TITLE, "N/A");
        }
        return metaData;
    }

    @Generated
    public TranslationTaskService(TranslationTaskRepository translationTaskRepository, EventRepository eventRepository, NewsRepository newsRepository, NewsCategoryRepository newsCategoryRepository, BannerRepository bannerRepository, PhotoGalleryRepository photoGalleryRepository, StaticPageRepository staticPageRepository) {
        this.translationTaskRepository = translationTaskRepository;
        this.eventRepository = eventRepository;
        this.newsRepository = newsRepository;
        this.newsCategoryRepository = newsCategoryRepository;
        this.bannerRepository = bannerRepository;
        this.photoGalleryRepository = photoGalleryRepository;
        this.staticPageRepository = staticPageRepository;
    }
}

