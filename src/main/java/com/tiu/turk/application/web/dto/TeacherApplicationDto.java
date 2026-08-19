package com.tiu.turk.application.web.dto;

import com.tiu.turk.application.common.Semester;

public record TeacherApplicationDto(String fullName, Long homeUniversityId, String department,
                                    String position, String phoneNumber, Long hostUniversityId,
                                    Long scholarshipId, Semester semester) {
}
