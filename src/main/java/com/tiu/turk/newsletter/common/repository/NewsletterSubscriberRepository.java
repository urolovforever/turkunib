package com.tiu.turk.newsletter.common.repository;

import com.tiu.turk.newsletter.common.entity.NewsletterSubscriberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsletterSubscriberRepository
extends JpaRepository<NewsletterSubscriberEntity, Long> {
    boolean existsByEmailIgnoreCase(String email);

    List<NewsletterSubscriberEntity> findAllByOrderByCreatedAtDesc();
}
