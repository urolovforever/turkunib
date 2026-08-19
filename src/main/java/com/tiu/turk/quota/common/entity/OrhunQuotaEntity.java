package com.tiu.turk.quota.common.entity;

import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import com.tiu.turk.user.common.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="orhun_quotas", uniqueConstraints={@UniqueConstraint(columnNames={"member_id", "scholarship_id"})})
@Getter
@Setter
@NoArgsConstructor
public class OrhunQuotaEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="member_id", nullable=false)
    private MemberEntity member;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="scholarship_id", nullable=false)
    private ScholarshipEntity scholarship;

    @Column(name="student_accept")
    private Integer studentAccept;

    @Column(name="student_send")
    private Integer studentSend;

    @Column(name="teacher_accept")
    private Integer teacherAccept;

    @Column(name="teacher_send")
    private Integer teacherSend;

    @Column(name="note", length=512)
    private String note;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="author_id")
    private UserEntity author;

    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @CreationTimestamp
    @Column(name="created_at", updatable=false)
    private LocalDateTime createdAt;
}
