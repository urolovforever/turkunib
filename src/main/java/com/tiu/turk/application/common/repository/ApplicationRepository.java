package com.tiu.turk.application.common.repository;

import com.tiu.turk.application.common.ApplicationStatus;
import com.tiu.turk.application.common.entity.ApplicationEntity;
import java.util.Collection;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplicationRepository
extends JpaRepository<ApplicationEntity, Long> {
    @Query(value="select a from ApplicationEntity a order by a.createdAt desc limit :size")
    List<ApplicationEntity> findLatestApplications(@Param("size") Long size);

    @Query("select a from ApplicationEntity a where lower(a.institution) like lower(concat('%', :memberName, '%'))")
    Page<ApplicationEntity> findByMemberName(@Param("memberName") String memberName, Pageable pageable);

    List<ApplicationEntity> findByAuthor_IdOrderByCreatedAtDesc(Long authorId);

    /** Applications submitted FROM this member university (new FK rows + legacy free-text rows). */
    @Query("select a from ApplicationEntity a where a.homeUniversity.id = :memberId "
            + "or (a.homeUniversity is null and lower(a.institution) like lower(concat('%', :memberName, '%'))) "
            + "order by a.createdAt desc")
    List<ApplicationEntity> findOutgoingForMember(@Param("memberId") Long memberId, @Param("memberName") String memberName);

    /** One application per user per Orhun round: true if a non-rejected application already exists. */
    boolean existsByAuthor_IdAndScholarship_IdAndStatusNotIn(Long authorId, Long scholarshipId,
                                                             Collection<ApplicationStatus> statuses);

    /** Applications forwarded TO this member university (visible only after home approval). */
    @Query("select a from ApplicationEntity a where a.hostUniversity.id = :memberId and a.status in :statuses "
            + "order by a.createdAt desc")
    List<ApplicationEntity> findIncomingForMember(@Param("memberId") Long memberId,
                                                  @Param("statuses") Collection<ApplicationStatus> statuses);

    /** All applications with the associations the statistics page aggregates on. */
    @Query("select a from ApplicationEntity a "
            + "left join fetch a.homeUniversity left join fetch a.hostUniversity left join fetch a.scholarship")
    List<ApplicationEntity> findAllForStatistics();
}
