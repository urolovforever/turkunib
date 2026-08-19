package com.tiu.turk.application.web.dto;

import com.tiu.turk.application.common.Semester;

public record StudentApplicationDto(String fullName, String studentId, Long homeUniversityId,
                                    String department, String phoneNumber, Long hostUniversityId,
                                    Long scholarshipId, Semester semester) {
}
