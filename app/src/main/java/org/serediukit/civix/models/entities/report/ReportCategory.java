package org.serediukit.civix.models.entities.report;

public enum ReportCategory {
    UNKNOWN(0),
    ROAD(1),
    SIDEWAY(2),
    ELECTRIC(3),
    WATER(4),
    GAS(5),
    HEAT(6);

    private final int value;

    ReportCategory(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static ReportCategory fromValue(int value) {
        for (ReportCategory category : ReportCategory.values()) {
            if (category.value == value) {
                return category;
            }
        }
        return UNKNOWN;
    }
}
