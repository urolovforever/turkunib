package com.tiu.turk.quota.common.dto;

public record QuotaRowView(
        Long memberId,
        String memberName,
        String countryName,
        Integer studentAccept,
        Integer studentSend,
        Integer teacherAccept,
        Integer teacherSend,
        String note) {

    public boolean hasNumbers() {
        return this.studentAccept != null || this.studentSend != null
                || this.teacherAccept != null || this.teacherSend != null;
    }
}
