package com.tiu.turk.leadership.common.entity;

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
@Table(name="leadership_member_i18n")
@Getter
@Setter
@NoArgsConstructor
public class LeadershipMemberTranslationEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="leadership_member_id", nullable=false)
    private LeadershipMemberEntity leadershipMember;
    @Column(name="locale", nullable=false, length=10)
    @Enumerated(value=EnumType.STRING)
    private TranslationLocale locale;
    @Column(name="position", length=512)
    private String position;
    @Column(name="organization", length=512)
    private String organization;
    @Column(name="bio", columnDefinition="text")
    private String bio;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
