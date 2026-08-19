package com.tiu.turk.contacts.web.service;

import com.tiu.turk.contacts.common.entity.FeedbackEntity;
import com.tiu.turk.contacts.common.repository.FeedbackRepository;
import java.time.LocalDateTime;
import lombok.Generated;
import org.springframework.stereotype.Service;

@Service
public class ContactsWebService {
    private final FeedbackRepository feedbackRepository;

    public void saveFeedback(FeedbackEntity newFeedback) {
        newFeedback.setCreatedAt(LocalDateTime.now());
        this.feedbackRepository.save(newFeedback);
    }

    @Generated
    public ContactsWebService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }
}

