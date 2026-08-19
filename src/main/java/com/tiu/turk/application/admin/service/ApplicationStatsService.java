package com.tiu.turk.application.admin.service;

import com.tiu.turk.application.admin.dto.ApplicationStats;
import com.tiu.turk.application.admin.dto.StatCountRow;
import com.tiu.turk.application.common.ApplicantType;
import com.tiu.turk.application.common.ApplicationStatus;
import com.tiu.turk.application.common.Semester;
import com.tiu.turk.application.common.entity.ApplicationEntity;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Builds the aggregated numbers for /admin/application/statistics.
 * Applications are aggregated in memory: the whole table is small (an exchange
 * program), and legacy rows (free-text institution, no FK, no round) need
 * Java-side normalization that would be awkward in JPQL.
 */
@Service
public class ApplicationStatsService {

    public List<ApplicationEntity> filter(List<ApplicationEntity> apps, Long scholarshipId,
                                          Semester semester, ApplicantType type) {
        return apps.stream()
                .filter(a -> scholarshipId == null
                        || (a.getScholarship() != null && scholarshipId.equals(a.getScholarship().getId())))
                .filter(a -> semester == null || a.getSemester() == semester)
                .filter(a -> type == null || effectiveType(a) == type)
                .toList();
    }

    public ApplicationStats build(List<ApplicationEntity> apps) {
        long students = apps.stream().filter(a -> effectiveType(a) == ApplicantType.STUDENT).count();
        long accepted = apps.stream().filter(a -> isAccepted(a.getStatus())).count();
        long rejected = apps.stream().filter(a -> isRejected(a.getStatus())).count();
        return new ApplicationStats(apps.size(), students, apps.size() - students,
                accepted, rejected, apps.size() - accepted - rejected,
                groupBy(apps, ApplicationStatsService::hostUniversityLabel),
                groupBy(apps, ApplicationStatsService::homeUniversityLabel),
                groupBy(apps, ApplicationStatsService::semesterLabel),
                groupBy(apps, ApplicationStatsService::roundLabel),
                groupBy(apps, ApplicationStatsService::statusLabel));
    }

    private List<StatCountRow> groupBy(List<ApplicationEntity> apps, Function<ApplicationEntity, String> key) {
        Map<String, List<ApplicationEntity>> grouped = apps.stream()
                .collect(Collectors.groupingBy(key, LinkedHashMap::new, Collectors.toList()));
        return grouped.entrySet().stream()
                .map(e -> row(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingLong(StatCountRow::total).reversed()
                        .thenComparing(StatCountRow::label))
                .toList();
    }

    private StatCountRow row(String label, List<ApplicationEntity> apps) {
        long students = apps.stream().filter(a -> effectiveType(a) == ApplicantType.STUDENT).count();
        long accepted = apps.stream().filter(a -> isAccepted(a.getStatus())).count();
        long rejected = apps.stream().filter(a -> isRejected(a.getStatus())).count();
        return StatCountRow.of(label, students, apps.size() - students,
                accepted, rejected, apps.size() - accepted - rejected);
    }

    /** Rows created before applicant_type existed are students. */
    private static ApplicantType effectiveType(ApplicationEntity a) {
        return a.getApplicantType() == null ? ApplicantType.STUDENT : a.getApplicantType();
    }

    private static boolean isAccepted(ApplicationStatus s) {
        return s == ApplicationStatus.ACCEPTED || s == ApplicationStatus.APPROVED;
    }

    private static boolean isRejected(ApplicationStatus s) {
        return s == ApplicationStatus.REJECTED || s == ApplicationStatus.HOME_REJECTED;
    }

    private static String hostUniversityLabel(ApplicationEntity a) {
        return a.getHostUniversity() != null ? a.getHostUniversity().getName() : "Not specified";
    }

    private static String homeUniversityLabel(ApplicationEntity a) {
        if (a.getHomeUniversity() != null) {
            return a.getHomeUniversity().getName();
        }
        return a.getInstitution() == null || a.getInstitution().isBlank() ? "Not specified" : a.getInstitution();
    }

    private static String semesterLabel(ApplicationEntity a) {
        if (a.getSemester() == null) {
            return "Not specified";
        }
        return a.getSemester() == Semester.FALL ? "Fall" : "Spring";
    }

    private static String roundLabel(ApplicationEntity a) {
        return a.getScholarship() != null ? a.getScholarship().getTitle() : "No round (legacy)";
    }

    private static String statusLabel(ApplicationEntity a) {
        return switch (a.getStatus()) {
            case SUBMITTED -> "Submitted";
            case UNDER_REVIEW -> "Under review";
            case HOME_APPROVED -> "Forwarded to host";
            case HOME_REJECTED -> "Rejected by home";
            case ACCEPTED -> "Accepted";
            case APPROVED -> "Approved (legacy)";
            case REJECTED -> "Rejected by host";
        };
    }
}
