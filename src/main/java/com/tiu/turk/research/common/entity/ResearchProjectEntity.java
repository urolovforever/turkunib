package com.tiu.turk.research.common.entity;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.research.common.ResearchProjectStatus;
import com.tiu.turk.user.common.entity.UserEntity;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.MapKey;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="research_projects")
@Getter
@Setter
@NoArgsConstructor
public class ResearchProjectEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy="researchProject", cascade={CascadeType.ALL}, orphanRemoval=true)
    @MapKey(name="locale")
    private Map<TranslationLocale, ResearchProjectTranslationEntity> translations = new EnumMap<>(TranslationLocale.class);
    @Column(name="title", nullable=false, length=512)
    private String title;
    @Column(name="subject_area", length=255)
    private String subjectArea;
    @Column(name="description", columnDefinition="text")
    private String description;
    @Column(name="content", columnDefinition="text")
    private String content;
    @Column(name="participating_universities", columnDefinition="text")
    private String participatingUniversities;
    @Column(name="start_year")
    private Integer startYear;
    @Column(name="end_year")
    private Integer endYear;
    @Enumerated(value=EnumType.STRING)
    @Column(name="status", nullable=false, length=64)
    private ResearchProjectStatus status = ResearchProjectStatus.ONGOING;
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
