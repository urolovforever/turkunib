package com.tiu.turk.quota.admin.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class QuotaRowForm {
    private Long memberId;
    private Integer studentAccept;
    private Integer studentSend;
    private Integer teacherAccept;
    private Integer teacherSend;
    private String note;

    public boolean isEmpty() {
        return this.studentAccept == null && this.studentSend == null
                && this.teacherAccept == null && this.teacherSend == null
                && (this.note == null || this.note.isBlank());
    }
}
