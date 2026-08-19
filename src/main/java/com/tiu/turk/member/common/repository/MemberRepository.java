package com.tiu.turk.member.common.repository;

import com.tiu.turk.member.common.entity.MemberEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository
extends JpaRepository<MemberEntity, Long> {
    List<MemberEntity> findByEnabledTrueOrderByNameAsc();
}

