package com.tiu.turk.applicationprocess.admin.service;

import com.tiu.turk.applicationprocess.admin.dto.CreateApplicationDto;
import com.tiu.turk.applicationprocess.common.ApplicationProcessStatus;
import com.tiu.turk.applicationprocess.common.entity.ApplicationProcessEntity;
import com.tiu.turk.applicationprocess.common.repository.ApplicationProcessRepository;
import com.tiu.turk.files.FileEntity;
import com.tiu.turk.files.FileService;
import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.member.common.repository.MemberRepository;
import com.tiu.turk.user.common.entity.UserEntity;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ApplicationProcessService {
    private static final String APPLICATION_PROCESS_MODULE = "APPLICATION_PROCESS";
    private final ApplicationProcessRepository applicationProcessRepository;
    private final MemberRepository memberRepository;
    private final FileService fileService;

    public List<ApplicationProcessEntity> getAllApplications() {
        return this.applicationProcessRepository.findAll();
    }

    public List<ApplicationProcessEntity> getApplicationsByMemberId(Long memberId) {
        return this.applicationProcessRepository.findByUniversitySentIdOrUniversityReceivedId(memberId, memberId);
    }

    public ApplicationProcessEntity getById(Long id) {
        return this.applicationProcessRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Application not found with id: " + id));
    }

    @Transactional
    public void updateStatus(Long id, ApplicationProcessStatus status) {
        ApplicationProcessEntity application = this.getById(id);
        application.setStatus(status);
        this.applicationProcessRepository.save(application);
    }

    @Transactional
    public void delete(Long id) {
        ApplicationProcessEntity application = this.getById(id);
        this.applicationProcessRepository.delete(application);
    }

    @Transactional
    public void createApplication(CreateApplicationDto processEntity, MultipartFile file, Long authorId) throws IOException {
        if (processEntity.getUniversitySentId().equals(processEntity.getUniversityReceivedId())) {
            throw new IllegalArgumentException("Sending and receiving universities must be different");
        }
        MemberEntity universitySent = this.memberRepository.findById(processEntity.getUniversitySentId()).orElseThrow(() -> new IllegalArgumentException("Sending university not found"));
        MemberEntity universityReceived = this.memberRepository.findById(processEntity.getUniversityReceivedId()).orElseThrow(() -> new IllegalArgumentException("Receiving university not found"));
        FileEntity storedFile = this.fileService.store(file, file.getOriginalFilename(), APPLICATION_PROCESS_MODULE, authorId);
        ApplicationProcessEntity application = new ApplicationProcessEntity();
        application.setUniversitySent(universitySent);
        application.setUniversityReceived(universityReceived);
        application.setFile(storedFile);
        application.setComment(processEntity.getComment());
        application.setStatus(ApplicationProcessStatus.NEW);
        application.setAuthor(new UserEntity(authorId));
        this.applicationProcessRepository.save(application);
    }
}
