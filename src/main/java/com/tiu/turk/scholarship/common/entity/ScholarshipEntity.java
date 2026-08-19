package com.tiu.turk.scholarship.common.entity;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="scholarships")
@Getter
@Setter
@NoArgsConstructor
public class ScholarshipEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy="scholarship", cascade={CascadeType.ALL}, orphanRemoval=true)
    @MapKey(name="locale")
    private Map<TranslationLocale, ScholarshipTranslationEntity> translations = new EnumMap<>(TranslationLocale.class);
    @Column(name="title", nullable=false, length=512)
    private String title;
    @Column(name="provider", length=512)
    private String provider;
    @Column(name="description", columnDefinition="text")
    private String description;
    @Column(name="content", columnDefinition="text")
    private String content;
    @Column(name="coverage", columnDefinition="text")
    private String coverage;
    @Column(name="eligibility", columnDefinition="text")
    private String eligibility;
    @Column(name="amount", length=512)
    private String amount;
    @Column(name="deadline")
    private LocalDate deadline;
    @Column(name="academic_year", length=20)
    private String academicYear;
    @Column(name="note", columnDefinition="text")
    private String note;
    @Column(name="application_start")
    private LocalDate applicationStart;
    @Column(name="timeline1_start")
    private LocalDate timeline1Start;
    @Column(name="timeline1_end")
    private LocalDate timeline1End;
    @Column(name="timeline2_start")
    private LocalDate timeline2Start;
    @Column(name="timeline2_end")
    private LocalDate timeline2End;
    @Column(name="timeline3_start")
    private LocalDate timeline3Start;
    @Column(name="timeline3_end")
    private LocalDate timeline3End;
    @Column(name="timeline4_start")
    private LocalDate timeline4Start;
    @Column(name="timeline4_end")
    private LocalDate timeline4End;
    @Column(name="timeline5_date")
    private LocalDate timeline5Date;
    @Column(name="apply_url", length=1024)
    private String applyUrl;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="document_id")
    private FileEntity document;
    @Column(name="display_order")
    private Integer displayOrder = 0;
    @Column(name="enabled")
    private Boolean enabled = true;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="author_id")
    private UserEntity author;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
