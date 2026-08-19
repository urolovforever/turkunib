package com.tiu.turk.quota.common.repository;

import com.tiu.turk.quota.common.entity.OrhunQuotaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrhunQuotaRepository extends JpaRepository<OrhunQuotaEntity, Long> {

    @Query("select q from OrhunQuotaEntity q join fetch q.member m left join fetch m.country c where q.scholarship.id = :scholarshipId order by c.name, m.name")
    List<OrhunQuotaEntity> findForScholarshipWithMembers(@Param("scholarshipId") Long scholarshipId);

    Optional<OrhunQuotaEntity> findByScholarshipIdAndMemberId(Long scholarshipId, Long memberId);
}
