package com.tiu.turk.webinar.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Entity
@Table(name="webinar_registrations")
@Getter
@Setter
@NoArgsConstructor
public class WebinarRegistrationEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="webinar_id", nullable=false)
    private WebinarEntity webinar;
    @Column(name="full_name", nullable=false, length=512)
    private String fullName;
    @Column(name="email", nullable=false, length=255)
    private String email;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;
}
