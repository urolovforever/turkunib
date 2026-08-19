package com.tiu.turk.quota.web.service;

import com.tiu.turk.quota.common.dto.QuotaRowView;
import com.tiu.turk.quota.common.repository.OrhunQuotaRepository;
import java.util.List;
import lombok.Generated;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrhunQuotaWebService {
    private final OrhunQuotaRepository quotaRepository;

    /** Saved quotas for a round, ordered by country then university, for the public table. */
    @Transactional(readOnly=true)
    public List<QuotaRowView> getForRound(Long scholarshipId) {
        return this.quotaRepository.findForScholarshipWithMembers(scholarshipId).stream()
                .map(q -> new QuotaRowView(q.getMember().getId(), q.getMember().getName(),
                        q.getMember().getCountry() != null ? q.getMember().getCountry().getName() : "",
                        q.getStudentAccept(), q.getStudentSend(),
                        q.getTeacherAccept(), q.getTeacherSend(), q.getNote()))
                .filter(QuotaRowView::hasNumbers)
                .toList();
    }

    @Generated
    public OrhunQuotaWebService(OrhunQuotaRepository quotaRepository) {
        this.quotaRepository = quotaRepository;
    }
}
