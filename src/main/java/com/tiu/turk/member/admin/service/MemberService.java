package com.tiu.turk.member.admin.service;

import com.tiu.turk.image.entity.ImageEntity;
import com.tiu.turk.image.service.ImageService;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.member.common.repository.MemberRepository;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.time.LocalDateTime;
import lombok.Generated;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final ImageService imageService;

    public Page<MemberEntity> getAllMembers(int page, int size) {
        return this.memberRepository.findAll((Pageable)PageRequest.of((int)page, (int)size, (Sort)Sort.by((String[])new String[]{"id"}).descending()));
    }

    public MemberEntity getMemberById(Long memberId) {
        return (MemberEntity)this.memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));
    }

    public MemberEntity createMember(MultipartFile file, MemberEntity member, Long authorId) throws IOException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
            ImageEntity storedImage = this.imageService.store(file, fileName, "member", authorId);
            member.setImage(storedImage);
        }
        member.setAuthor(new UserEntity(authorId));
        member.setCreatedAt(LocalDateTime.now());
        member.setUpdatedAt(LocalDateTime.now());
        return (MemberEntity)this.memberRepository.save(member);
    }

    public MemberEntity updateMember(Long memberId, MultipartFile file, MemberEntity member, Long authorId) throws IOException {
        MemberEntity existingMember = this.getMemberById(memberId);
        Long imageIdToDelete = null;
        if (file != null && !file.isEmpty()) {
            if (existingMember.getImage() != null) {
                imageIdToDelete = existingMember.getImage().getId();
            }
            String fileName = file.getOriginalFilename() != null ? file.getOriginalFilename() : "image";
            ImageEntity storedImage = this.imageService.store(file, fileName, "member", authorId);
            existingMember.setImage(storedImage);
        }
        existingMember.setName(member.getName());
        existingMember.setFounded(member.getFounded());
        existingMember.setCountry(member.getCountry());
        existingMember.setAddress(member.getAddress());
        existingMember.setUrl(member.getUrl());
        existingMember.setFacultiesNumber(member.getFacultiesNumber());
        existingMember.setStudentsNumber(member.getStudentsNumber());
        existingMember.setLatitude(member.getLatitude());
        existingMember.setLongitude(member.getLongitude());
        existingMember.setAdditionalInfo(member.getAdditionalInfo());
        existingMember.setEnabled(member.getEnabled());
        existingMember.setAuthor(new UserEntity(authorId));
        existingMember.setUpdatedAt(LocalDateTime.now());
        existingMember = (MemberEntity)this.memberRepository.save(existingMember);
        if (imageIdToDelete != null) {
            this.imageService.deleteImage(imageIdToDelete);
        }
        return existingMember;
    }

    public void deleteMember(Long memberId) throws IOException {
        MemberEntity member = this.getMemberById(memberId);
        Long imageIdToDelete = member.getImage() != null ? member.getImage().getId() : null;
        this.memberRepository.delete(member);
        if (imageIdToDelete != null) {
            this.imageService.deleteImage(imageIdToDelete);
        }
    }

    @Generated
    public MemberService(MemberRepository memberRepository, ImageService imageService) {
        this.memberRepository = memberRepository;
        this.imageService = imageService;
    }
}

