package com.tiu.turk.keydate.common.entity;

import com.tiu.turk.common.enums.TranslationLocale;
import com.tiu.turk.keydate.common.KeyDateType;
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
@Table(name="key_dates")
@Getter
@Setter
@NoArgsConstructor
public class KeyDateEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @OneToMany(mappedBy="keyDate", cascade={CascadeType.ALL}, orphanRemoval=true)
    @MapKey(name="locale")
    private Map<TranslationLocale, KeyDateTranslationEntity> translations = new EnumMap<>(TranslationLocale.class);
    @Column(name="title", nullable=false, length=512)
    private String title;
    @Column(name="event_date", nullable=false)
    private LocalDate eventDate;
    @Column(name="end_date")
    private LocalDate endDate;
    @Column(name="description", columnDefinition="text")
    private String description;
    @Enumerated(value=EnumType.STRING)
    @Column(name="type", nullable=false, length=64)
    private KeyDateType type = KeyDateType.EVENT;
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
