package com.tiu.turk.user.common.entity;

import com.tiu.turk.member.common.entity.MemberEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(name="email", nullable=false, length=255)
    private String email;
    @Column(name="password", nullable=false, length=512)
    private String password;
    @Column(name="first_name", nullable=false, length=50)
    private String firstName;
    @Column(name="last_name", length=50)
    private String lastName;
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="user_roles", joinColumns={@JoinColumn(name="user_id")}, inverseJoinColumns={@JoinColumn(name="role_id")})
    private Set<RoleEntity> roles = new HashSet<>();
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="author_id")
    private UserEntity author;
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="member_id")
    private MemberEntity member;
    @Column(name="enabled")
    private Boolean enabled = true;
    @UpdateTimestamp
    @Column(name="updated_at")
    private LocalDateTime updatedAt;
    @CreationTimestamp
    @Column(name="created_at")
    private LocalDateTime createdAt;

    public UserEntity(Long id) {
        this.id = id;
    }
}
