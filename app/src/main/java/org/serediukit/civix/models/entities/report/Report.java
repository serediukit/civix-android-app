package org.serediukit.civix.models.entities.report;

import com.squareup.moshi.Json;

import org.serediukit.civix.models.entities.city.Location;

public class Report {
    @Json(name = "report_id")
    private String reportId;

    @Json(name = "user_id")
    private long userId;

    @Json(name = "create_time")
    private String createTime;

    @Json(name = "update_time")
    private String updateTime;

    @Json(name = "location")
    private Location location;

    @Json(name = "city_id")
    private String cityId;

    @Json(name = "description")
    private String description;

    @Json(name = "category_id")
    private int categoryId;

    @Json(name = "current_status_id")
    private int currentStatusId;

    public Report() {
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
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

    public ReportCategory getCategory() {
        return ReportCategory.fromValue(categoryId);
    }

    public void setCategory(ReportCategory category) {
        this.categoryId = category.getValue();
    }

    public int getCurrentStatusId() {
        return currentStatusId;
    }

    public void setCurrentStatusId(int currentStatusId) {
        this.currentStatusId = currentStatusId;
    }

    public ReportStatus getCurrentStatus() {
        return ReportStatus.fromValue(currentStatusId);
    }

    public void setCurrentStatus(ReportStatus status) {
        this.currentStatusId = status.getValue();
    }

    @Override
    public String toString() {
        return "Report{" +
                "userId=" + userId +
                ", location.lat=" + location.getLat() +
                ", location.lon=" + location.getLon() +
                ", cityId='" + cityId + '\'' +
                ", description='" + description + '\'' +
                ", categoryId=" + categoryId +
                ", currentStatusId=" + currentStatusId +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
