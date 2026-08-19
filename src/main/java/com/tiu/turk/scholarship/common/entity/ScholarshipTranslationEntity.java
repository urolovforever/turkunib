package com.tiu.turk.scholarship.common.entity;

import com.tiu.turk.common.enums.TranslationLocale;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="scholarship_i18n")
@Getter
@Setter
@NoArgsConstructor
public class ScholarshipTranslationEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="scholarship_id", nullable=false)
    private ScholarshipEntity scholarship;
    @Column(name="locale", nullable=false, length=10)
    @Enumerated(value=EnumType.STRING)
    private TranslationLocale locale;
    @Column(name="title", length=512)
    private String title;
    @Column(name="provider", length=512)
    private String provider;
    @Column(name="description", columnDefinition="text")
    private String description;
    @Column(name="note", columnDefinition="text")
    private String note;
    @Column(name="content", columnDefinition="text")
    private String content;
    @Column(name="coverage", columnDefinition="text")
    private String coverage;
    @Column(name="eligibility", columnDefinition="text")
    private String eligibility;
    @Column(name="amount", length=512)
    private String amount;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
