package com.tiu.turk.quota.admin.service;

import com.tiu.turk.member.common.entity.MemberEntity;
import com.tiu.turk.member.common.repository.MemberRepository;
import com.tiu.turk.quota.admin.dto.QuotaRowForm;
import com.tiu.turk.quota.common.dto.QuotaRowView;
import com.tiu.turk.quota.common.entity.OrhunQuotaEntity;
import com.tiu.turk.quota.common.repository.OrhunQuotaRepository;
import com.tiu.turk.scholarship.common.entity.ScholarshipEntity;
import com.tiu.turk.scholarship.common.repository.ScholarshipRepository;
import com.tiu.turk.user.common.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrhunQuotaAdminService {
    private final OrhunQuotaRepository quotaRepository;
    private final MemberRepository memberRepository;
    private final ScholarshipRepository scholarshipRepository;
    private final UserRepository userRepository;

    public List<ScholarshipEntity> getRounds() {
        List<ScholarshipEntity> rounds = this.scholarshipRepository.findAllByOrderByDisplayOrderAscIdAsc();
        return rounds.stream().sorted(Comparator.comparing(ScholarshipEntity::getId).reversed()).toList();
    }

    public ScholarshipEntity getRound(Long id) {
        return this.scholarshipRepository.findById(id).orElse(null);
    }

    /** Full grid for the super admin: every enabled member merged with any saved quota for the round. */
    @Transactional(readOnly=true)
    public List<QuotaRowView> buildGrid(Long scholarshipId) {
        Map<Long, OrhunQuotaEntity> byMember = this.quotaRepository.findForScholarshipWithMembers(scholarshipId).stream()
                .collect(Collectors.toMap(q -> q.getMember().getId(), Function.identity()));
        return this.memberRepository.findByEnabledTrueOrderByNameAsc().stream()
                .sorted(Comparator.comparing((MemberEntity m) -> m.getCountry() != null ? m.getCountry().getName() : "")
                        .thenComparing(MemberEntity::getName))
                .map(m -> {
                    OrhunQuotaEntity q = byMember.get(m.getId());
                    return new QuotaRowView(m.getId(), m.getName(),
                            m.getCountry() != null ? m.getCountry().getName() : "",
                            q != null ? q.getStudentAccept() : null,
                            q != null ? q.getStudentSend() : null,
                            q != null ? q.getTeacherAccept() : null,
                            q != null ? q.getTeacherSend() : null,
                            q != null ? q.getNote() : null);
                }).toList();
    }

    /** Single row for a university-member admin. */
    @Transactional(readOnly=true)
    public QuotaRowView buildMemberRow(Long scholarshipId, Long memberId) {
        MemberEntity m = this.memberRepository.findById(memberId).orElseThrow();
        OrhunQuotaEntity q = this.quotaRepository.findByScholarshipIdAndMemberId(scholarshipId, memberId).orElse(null);
        return new QuotaRowView(m.getId(), m.getName(),
                m.getCountry() != null ? m.getCountry().getName() : "",
                q != null ? q.getStudentAccept() : null,
                q != null ? q.getStudentSend() : null,
                q != null ? q.getTeacherAccept() : null,
                q != null ? q.getTeacherSend() : null,
                q != null ? q.getNote() : null);
    }

    @Transactional
    public void saveGrid(Long scholarshipId, List<QuotaRowForm> rows, Long authorId) {
        for (QuotaRowForm row : rows) {
            if (row == null || row.getMemberId() == null) continue;
            this.upsertRow(scholarshipId, row.getMemberId(), row, authorId);
        }
    }

    @Transactional
    public void saveForMember(Long scholarshipId, Long memberId, QuotaRowForm row, Long authorId) {
        this.upsertRow(scholarshipId, memberId, row, authorId);
    }

    private void upsertRow(Long scholarshipId, Long memberId, QuotaRowForm row, Long authorId) {
        OrhunQuotaEntity existing = this.quotaRepository.findByScholarshipIdAndMemberId(scholarshipId, memberId).orElse(null);
        if (row.isEmpty()) {
            // Emptied-out row deletes the stored quota so the public table stays clean.
            if (existing != null) {
                this.quotaRepository.delete(existing);
            }
            return;
        }
        OrhunQuotaEntity entity = existing != null ? existing : new OrhunQuotaEntity();
        if (existing == null) {
            entity.setMember(this.memberRepository.getReferenceById(memberId));
            entity.setScholarship(this.scholarshipRepository.getReferenceById(scholarshipId));
        }
        entity.setStudentAccept(row.getStudentAccept());
        entity.setStudentSend(row.getStudentSend());
        entity.setTeacherAccept(row.getTeacherAccept());
        entity.setTeacherSend(row.getTeacherSend());
        entity.setNote(row.getNote() != null && !row.getNote().isBlank() ? row.getNote().trim() : null);
        if (authorId != null) {
            entity.setAuthor(this.userRepository.getReferenceById(authorId));
        }
        this.quotaRepository.save(entity);
    }

    @Generated
    public OrhunQuotaAdminService(OrhunQuotaRepository quotaRepository, MemberRepository memberRepository, ScholarshipRepository scholarshipRepository, UserRepository userRepository) {
        this.quotaRepository = quotaRepository;
        this.memberRepository = memberRepository;
        this.scholarshipRepository = scholarshipRepository;
        this.userRepository = userRepository;
    }
}
