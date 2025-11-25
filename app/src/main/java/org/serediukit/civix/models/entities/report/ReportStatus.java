package org.serediukit.civix.models.entities.report;

import android.content.Context;

import org.serediukit.civix.R;

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

    public String getDisplayName(Context context) {
        switch (this) {
            case NEW:
                return context.getString(R.string.status_new);
            case IN_PROGRESS:
                return context.getString(R.string.status_in_progress);
            case COMPLETED:
                return context.getString(R.string.status_completed);
            case REJECTED:
                return context.getString(R.string.status_rejected);
            case CANCELED:
                return context.getString(R.string.status_canceled);
            default:
                return context.getString(R.string.status_new);
        }
    }
}
