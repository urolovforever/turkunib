package com.tiu.turk.application.admin.dto;

/** One aggregated row on the application statistics page (per university / semester / round / status). */
public record StatCountRow(String label, long students, long teachers, long total,
                           long accepted, long rejected, long pending) {

    public static StatCountRow of(String label, long students, long teachers,
                                  long accepted, long rejected, long pending) {
        return new StatCountRow(label, students, teachers, students + teachers, accepted, rejected, pending);
    }
}
