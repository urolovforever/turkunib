package com.tiu.turk.contacts.common.repository;

import com.tiu.turk.contacts.common.entity.FeedbackEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FeedbackRepository
extends JpaRepository<FeedbackEntity, Long> {
    @Query(value="select f from FeedbackEntity f\norder by f.createdAt desc\nlimit :size\n")
    public List<FeedbackEntity> findLatestFeedbacks(@Param("size") Long size);
}

