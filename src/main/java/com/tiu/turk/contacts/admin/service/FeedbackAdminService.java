package com.tiu.turk.contacts.admin.service;

import com.tiu.turk.contacts.common.entity.FeedbackEntity;
import com.tiu.turk.contacts.common.repository.FeedbackRepository;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class FeedbackAdminService {
    private final FeedbackRepository feedbackRepository;

    public Page<FeedbackEntity> getAllFeedbacks(int page, int size) {
        return this.feedbackRepository.findAll((Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public FeedbackEntity getFeedbackById(Long id) {
        return (FeedbackEntity)this.feedbackRepository.findById(id).orElseThrow(() -> new RuntimeException("Feedback not found with ID: " + id));
    }

    public void deleteFeedbackById(Long id) {
        FeedbackEntity feedbackEntity = this.getFeedbackById(id);
        this.feedbackRepository.delete(feedbackEntity);
    }

    @Generated
    public FeedbackAdminService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }
}

