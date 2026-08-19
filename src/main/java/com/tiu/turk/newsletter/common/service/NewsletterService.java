package com.tiu.turk.newsletter.common.service;

import com.tiu.turk.newsletter.common.entity.NewsletterSubscriberEntity;
import com.tiu.turk.newsletter.common.repository.NewsletterSubscriberRepository;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NewsletterService {
    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    private final NewsletterSubscriberRepository repository;

    @Transactional
    public void subscribe(String email) {
        String clean = email == null ? "" : email.trim().toLowerCase();
        if (!EMAIL.matcher(clean).matches()) {
            throw new IllegalArgumentException("Please enter a valid email address.");
        }
        if (this.repository.existsByEmailIgnoreCase(clean)) {
            throw new IllegalArgumentException("This email is already subscribed.");
        }
        NewsletterSubscriberEntity subscriber = new NewsletterSubscriberEntity();
        subscriber.setEmail(clean);
        subscriber.setActive(true);
        this.repository.save(subscriber);
    }

    public List<NewsletterSubscriberEntity> getAll() {
        return this.repository.findAllByOrderByCreatedAtDesc();
    }

    public long count() {
        return this.repository.count();
    }
}
