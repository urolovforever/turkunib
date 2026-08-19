package com.tiu.turk.application.common;

/**
 * Orhun Exchange application lifecycle:
 * SUBMITTED → (home university admin) HOME_APPROVED | HOME_REJECTED
 * HOME_APPROVED → (host university admin) ACCEPTED | REJECTED
 * UNDER_REVIEW and APPROVED remain for records created before the two-stage flow.
 */
public enum ApplicationStatus {
    SUBMITTED,
    UNDER_REVIEW,
    HOME_APPROVED,
    HOME_REJECTED,
    ACCEPTED,
    APPROVED,
    REJECTED;
}
