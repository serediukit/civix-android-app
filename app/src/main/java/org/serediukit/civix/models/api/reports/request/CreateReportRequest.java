package org.serediukit.civix.models.api.reports.request;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.entities.city.Location;

public class CreateReportRequest {
    @Json(name = "location")
    private Location location;

    @Json(name = "description")
    private String description;

    @Json(name = "category_id")
    private int categoryId;

    public CreateReportRequest(Location location, String description, int categoryId) {
        this.location = location;
        this.description = description;
        this.categoryId = categoryId;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}
