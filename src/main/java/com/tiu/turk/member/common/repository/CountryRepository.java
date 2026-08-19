package com.tiu.turk.member.common.repository;

import com.tiu.turk.member.common.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository
extends JpaRepository<CountryEntity, Long> {
}

