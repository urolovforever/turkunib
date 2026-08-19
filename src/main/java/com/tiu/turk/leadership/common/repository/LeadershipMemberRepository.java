package com.tiu.turk.leadership.common.repository;

import com.tiu.turk.leadership.common.entity.LeadershipMemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeadershipMemberRepository
extends JpaRepository<LeadershipMemberEntity, Long> {
    List<LeadershipMemberEntity> findByEnabledTrueOrderByDisplayOrderAscIdAsc();

    List<LeadershipMemberEntity> findAllByOrderByDisplayOrderAscIdAsc();

    @EntityGraph(attributePaths={"translations", "photo"})
    @Query("select m from LeadershipMemberEntity m where m.enabled = true order by m.displayOrder asc, m.id asc")
    List<LeadershipMemberEntity> findEnabledWithTranslations();

    @EntityGraph(attributePaths={"translations", "photo"})
    @Query("select m from LeadershipMemberEntity m where m.id = :id")
    Optional<LeadershipMemberEntity> findByIdWithTranslations(@Param("id") Long id);
}
