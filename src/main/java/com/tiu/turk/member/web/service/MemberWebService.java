package com.tiu.turk.member.web.service;

import com.tiu.turk.common.exception.PageNotFoundException;
import com.tiu.turk.member.common.entity.CountryEntity;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.member.common.repository.CountryRepository;
import com.tiu.turk.member.common.repository.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Generated;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class MemberWebService {
    private final CountryRepository countryRepository;
    private final MemberRepository memberRepository;

    public List<CountryEntity> getAllCountries() {
        return this.countryRepository.findAll(Sort.by((String[])new String[]{"name"}).ascending());
    }

    public Map<Long, List<MemberEntity>> getAllMembersMappedByCountryId() {
        List<MemberEntity> members = this.memberRepository.findAll(Sort.by((String[])new String[]{"name"}).ascending());
        return members.stream().collect(Collectors.groupingBy(member -> member.getCountry().getId()));
    }

    public List<MemberEntity> getAllMembers() {
        return this.memberRepository.findAll(Sort.by((String[])new String[]{"name"}).ascending());
    }

    public MemberEntity getMemberById(Long id) {
        return this.memberRepository.findById(id).orElseThrow(() -> new PageNotFoundException("Member not found with ID: " + id));
    }

    @Generated
    public MemberWebService(CountryRepository countryRepository, MemberRepository memberRepository) {
        this.countryRepository = countryRepository;
        this.memberRepository = memberRepository;
    }
}

