package com.tiu.turk.contacts.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="feedbacks")
public class FeedbackEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="full_name", length=512, nullable=false)
    private String fullName;
    @Column(name="email", nullable=false)
    private String email;
    @Column(name="subject", length=1024, nullable=false)
    private String subject;
    @Column(name="message", columnDefinition="TEXT", nullable=false)
    private String message;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Generated
    public Long getId() {
        return this.id;
    }

    @Generated
    public String getFullName() {
        return this.fullName;
    }

    @Generated
    public String getEmail() {
        return this.email;
    }

    @Generated
    public String getSubject() {
        return this.subject;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Generated
    public void setId(Long id) {
        this.id = id;
    }

    @Generated
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Generated
    public void setEmail(String email) {
        this.email = email;
    }

    @Generated
    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Generated
    public void setMessage(String message) {
        this.message = message;
    }

    @Generated
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

