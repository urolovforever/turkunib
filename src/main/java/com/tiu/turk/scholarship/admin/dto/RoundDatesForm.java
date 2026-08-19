package com.tiu.turk.scholarship.admin.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Per-round dates shown on the Orhun Exchange page announcement. The announcement
 * texts are static (i18n); only the academic year and these dates change per round.
 */
@Getter
@Setter
public class RoundDatesForm {
    private String academicYear;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeline1Start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeline1End;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeline2Start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeline2End;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeline3Start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeline3End;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeline4Start;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeline4End;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate timeline5Date;
}
