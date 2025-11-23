package org.serediukit.civix.models.entities.report;

public enum ReportStatus {
    NEW(0),
    IN_PROGRESS(1),
    COMPLETED(2),
    REJECTED(3),
    CANCELED(4);

    private final int value;

    ReportStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ReportStatus fromValue(int value) {
        for (ReportStatus status : ReportStatus.values()) {
            if (status.value == value) {
                return status;
            }
        }
        return NEW;
    }
}
