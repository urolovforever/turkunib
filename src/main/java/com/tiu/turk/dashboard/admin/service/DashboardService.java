package com.tiu.turk.dashboard.admin.service;

import com.tiu.turk.application.common.entity.ApplicationEntity;
import com.tiu.turk.application.common.repository.ApplicationRepository;
import com.tiu.turk.banner.common.repository.BannerRepository;
import com.tiu.turk.contacts.common.entity.FeedbackEntity;
import com.tiu.turk.contacts.common.repository.FeedbackRepository;
import com.tiu.turk.event.common.repository.EventRepository;
import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.repository.ImageRepository;
import com.tiu.turk.member.common.repository.CountryRepository;
import com.tiu.turk.member.common.repository.MemberRepository;
import com.tiu.turk.news.common.repository.NewsCategoryRepository;
import com.tiu.turk.news.common.repository.NewsRepository;
import com.tiu.turk.photogallery.common.repository.PhotoGalleryRepository;
import com.tiu.turk.staticpage.common.repository.StaticPageRepository;
import com.tiu.turk.user.common.repository.UserRepository;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ImageRepository imageRepository;
    private final NewsRepository newsRepository;
    private final NewsCategoryRepository newsCategoryRepository;
    private final PhotoGalleryRepository photoGalleryRepository;
    private final EventRepository eventRepository;
    private final CountryRepository countryRepository;
    private final MemberRepository memberRepository;
    private final BannerRepository bannerRepository;
    private final StaticPageRepository staticPageRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final FeedbackRepository feedbackRepository;

    public Long countAllImages() {
        return this.imageRepository.count();
    }

    public String getAllImagesSize() {
        List<ImageEntity> images = this.imageRepository.findAll();
        Long totalSizeInBytes = images.stream().map(ImageEntity::getImageSize).filter(Objects::nonNull).reduce(0L, Long::sum);
        double sizeInMB = (double)totalSizeInBytes.longValue() / 1048576.0;
        return String.format("%.2f MB", sizeInMB);
    }

    public Long countAllNews() {
        return this.newsRepository.count();
    }

    public Long countAllNewsCategories() {
        return this.newsCategoryRepository.count();
    }

    public Long countAllPhotoGalleries() {
        return this.photoGalleryRepository.count();
    }

    public Long countAllEvent() {
        return this.eventRepository.count();
    }

    public Long countAllCountries() {
        return this.countryRepository.count();
    }

    public Long countAllMembers() {
        return this.memberRepository.count();
    }

    public Long countAllBanners() {
        return this.bannerRepository.count();
    }

    public Long countAllStaticPages() {
        return this.staticPageRepository.count();
    }

    public Long countAllUsers() {
        return this.userRepository.count();
    }

    public List<ApplicationEntity> getLatestApplication(Long size) {
        return this.applicationRepository.findLatestApplications(size);
    }

    public List<ApplicationEntity> getLatestApplicationByMemberName(String memberName) {
        return this.applicationRepository.findByMemberName(memberName, PageRequest.of(0, 10, Sort.by("id").descending())).getContent();
    }

    public List<FeedbackEntity> getLatestFeedbacks(Long size) {
        return this.feedbackRepository.findLatestFeedbacks(size);
    }
}
