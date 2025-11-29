package org.serediukit.civix.models.entities.report;

import android.content.Context;

import org.serediukit.civix.R;

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

    public String getDisplayName(Context context) {
        switch (this) {
            case ROAD:
                return context.getString(R.string.category_road);
            case SIDEWAY:
                return context.getString(R.string.category_sideway);
            case ELECTRIC:
                return context.getString(R.string.category_electric);
            case WATER:
                return context.getString(R.string.category_water);
            case GAS:
                return context.getString(R.string.category_gas);
            case HEAT:
                return context.getString(R.string.category_heat);
            case UNKNOWN:
            default:
                return context.getString(R.string.category_unknown);
        }
    }
}
