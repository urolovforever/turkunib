package com.tiu.turk.faq.common.entity;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.faq.common.FaqCategory;
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
@Table(name="faqs")
@Getter
@Setter
@NoArgsConstructor
public class FaqEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy="faq", cascade={CascadeType.ALL}, orphanRemoval=true)
    @MapKey(name="locale")
    private Map<TranslationLocale, FaqTranslationEntity> translations = new EnumMap<>(TranslationLocale.class);
    @Column(name="question", nullable=false, length=1024)
    private String question;
    @Column(name="answer", columnDefinition="text")
    private String answer;
    @Enumerated(value=EnumType.STRING)
    @Column(name="category", nullable=false, length=64)
    private FaqCategory category = FaqCategory.GENERAL;
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
