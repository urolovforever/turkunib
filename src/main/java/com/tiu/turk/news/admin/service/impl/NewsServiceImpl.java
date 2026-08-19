package com.tiu.turk.news.admin.service.impl;

import com.github.slugify.Slugify;
import com.tiu.turk.common.constant.Constants;
import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.service.ImageService;
import com.tiu.turk.news.admin.service.NewsService;
import com.tiu.turk.news.common.entity.NewsEntity;
import com.tiu.turk.news.common.entity.NewsTranslationEntity;
import com.tiu.turk.news.common.repository.NewsRepository;
import com.tiu.turk.translation.service.TranslationInitializerService;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class NewsServiceImpl
implements NewsService {
    private final NewsRepository newsRepository;
    private final ImageService imageService;
    private final TranslationInitializerService translationInitializerService;
    private final Slugify slugify = Slugify.builder().transliterator(Boolean.valueOf(true)).build();

    public Page<NewsEntity> getAllNews(int page, int size) {
        return this.newsRepository.findAll((Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public NewsEntity getNewsById(Long id) {
        return (NewsEntity)this.newsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("News not found with ID: " + id));
    }

    public NewsEntity saveNews(MultipartFile file, NewsEntity news, Long authorId) throws IOException {
        if (news.getCategory() == null || news.getCategory().getId() == null) {
            throw new IllegalArgumentException("News category must be provided");
        }
        if (file != null && !file.isEmpty()) {
            String fileName = Optional.ofNullable(file.getResource().getFilename()).orElse("image");
            news.setImage(this.imageService.store(file, fileName, "news", authorId));
        }
        Map<TranslationLocale, NewsTranslationEntity> incomingTranslations = news.getTranslations();
        NewsTranslationEntity enTranslation = incomingTranslations != null ? (NewsTranslationEntity)incomingTranslations.get(TranslationLocale.EN) : null;
        if (enTranslation == null || enTranslation.getTitle() == null || enTranslation.getTitle().isBlank()) {
            throw new IllegalArgumentException("English (EN) title must be provided");
        }
        EnumMap<TranslationLocale, NewsTranslationEntity> translations = new EnumMap<TranslationLocale, NewsTranslationEntity>(TranslationLocale.class);
        for (Map.Entry<TranslationLocale, NewsTranslationEntity> entry : incomingTranslations.entrySet()) {
            TranslationLocale locale = entry.getKey();
            NewsTranslationEntity source = entry.getValue();
            if (source == null) {
                continue;
            }
            String title = Optional.ofNullable(source.getTitle()).orElse("");
            if (title.isBlank()) {
                continue;
            }
            String content = Optional.ofNullable(source.getContent()).orElse("");
            String description = Optional.ofNullable(source.getDescription()).orElse("");
            NewsTranslationEntity newsTranslation = new NewsTranslationEntity();
            newsTranslation.setLocale(locale);
            newsTranslation.setTitle(title);
            newsTranslation.setSlug(this.slugify.slugify(title));
            newsTranslation.setContent(Jsoup.clean((String)content, (Safelist)Constants.JSOUP_SAFELIST));
            newsTranslation.setDescription(description);
            newsTranslation.setUpdatedAt(LocalDateTime.now());
            newsTranslation.setCreatedAt(LocalDateTime.now());
            newsTranslation.setNews(news);
            translations.put(locale, newsTranslation);
        }
        news.setTranslations(translations);
        news.setAuthor(new UserEntity(authorId));
        news.setUpdatedAt(LocalDateTime.now());
        news.setCreatedAt(LocalDateTime.now());
        return (NewsEntity)this.newsRepository.save(news);
    }

    public NewsEntity updateNews(Long id, MultipartFile file, NewsEntity news, Long authorId) throws IOException {
        NewsEntity existingNews = (NewsEntity)this.newsRepository.getReferenceById(id);
        if (news.getCategory() == null || news.getCategory().getId() == null) {
            throw new IllegalArgumentException("News category must be provided");
        }
        if (file != null && !file.isEmpty()) {
            String fileName = Optional.ofNullable(file.getResource().getFilename()).orElse("image");
            Optional<ImageEntity> existingImageOpt = Optional.ofNullable(existingNews.getImage());
            if (existingImageOpt.isPresent()) {
                this.imageService.deleteImage(existingImageOpt.get().getId());
            }
            existingNews.setImage(this.imageService.store(file, fileName, "news", authorId));
        }
        if (news.getTranslations() != null && !news.getTranslations().isEmpty()) {
            for (Map.Entry entry : news.getTranslations().entrySet()) {
                TranslationLocale locale = (TranslationLocale)entry.getKey();
                NewsTranslationEntity translation = (NewsTranslationEntity)entry.getValue();
                NewsTranslationEntity existingTranslation = (NewsTranslationEntity)existingNews.getTranslations().get(locale);
                if (existingTranslation == null) {
                    existingTranslation = new NewsTranslationEntity();
                    existingTranslation.setNews(existingNews);
                    existingTranslation.setLocale(locale);
                    existingTranslation.setCreatedAt(LocalDateTime.now());
                }
                String safeTitle = Optional.ofNullable(translation.getTitle()).orElse("");
                String safeContent = Optional.ofNullable(translation.getContent()).orElse("");
                String safeDescription = Optional.ofNullable(translation.getDescription()).orElse("");
                existingTranslation.setTitle(safeTitle);
                String baseForSlug = safeTitle.isBlank() ? "news" : safeTitle;
                existingTranslation.setSlug(this.slugify.slugify(baseForSlug));
                existingTranslation.setContent(Jsoup.clean((String)safeContent, (Safelist)Constants.JSOUP_SAFELIST));
                existingTranslation.setDescription(safeDescription);
                existingTranslation.setUpdatedAt(LocalDateTime.now());
                existingNews.getTranslations().put(locale, existingTranslation);
            }
        }
        existingNews.setCategory(news.getCategory());
        existingNews.setAuthorName(news.getAuthorName());
        existingNews.setEnabled(news.getEnabled());
        existingNews.setAuthor(new UserEntity(authorId));
        existingNews.setUpdatedAt(LocalDateTime.now());
        return (NewsEntity)this.newsRepository.save(existingNews);
    }

    public void deleteNews(Long id) {
        NewsEntity news = (NewsEntity)this.newsRepository.getReferenceById(id);
        Optional.ofNullable(news.getImage()).ifPresent(i -> {
            try {
                this.imageService.deleteImage(i.getId());
            }
            catch (Exception exception) {
                // empty catch block
            }
        });
        this.newsRepository.delete(news);
    }

    @Transactional(propagation=Propagation.NOT_SUPPORTED)
    public List<String> executeTranslationTasks(Long newsId) throws Exception {
        NewsEntity news = this.getNewsById(newsId);
        NewsTranslationEntity enLocale = (NewsTranslationEntity)news.getTranslations().get(TranslationLocale.EN);
        return this.translationInitializerService.initializeNewsTranslationTasks(newsId, TranslationLocale.defaultTargetLocales(), enLocale.getTitle() + enLocale.getDescription() + enLocale.getContent());
    }

    @Generated
    public NewsServiceImpl(NewsRepository newsRepository, ImageService imageService, TranslationInitializerService translationInitializerService) {
        this.newsRepository = newsRepository;
        this.imageService = imageService;
        this.translationInitializerService = translationInitializerService;
    }
}

