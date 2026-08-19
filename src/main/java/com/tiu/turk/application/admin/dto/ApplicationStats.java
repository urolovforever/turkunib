package com.tiu.turk.application.admin.dto;

import java.util.List;

/** Aggregated statistics over a set of Orhun Exchange applications. */
public record ApplicationStats(long total, long students, long teachers,
                               long accepted, long rejected, long pending,
                               List<StatCountRow> byHostUniversity,
                               List<StatCountRow> byHomeUniversity,
                               List<StatCountRow> bySemester,
                               List<StatCountRow> byRound,
                               List<StatCountRow> byStatus) {
}
