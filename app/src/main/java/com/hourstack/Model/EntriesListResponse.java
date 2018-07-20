package com.hourstack.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EntriesListResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("allocated_seconds")
    @Expose
    private Integer allocatedSeconds;
    @SerializedName("allocated_hours")
    @Expose
    private String allocatedHours;
    @SerializedName("actual_seconds")
    @Expose
    private Integer actualSeconds;
    @SerializedName("actual_hours")
    @Expose
    private String actualHours;
    @SerializedName("complete")
    @Expose
    private Integer complete;
    @SerializedName("project_id")
    @Expose
    private Integer projectId;
    @SerializedName("label_id")
    @Expose
    private Integer labelId;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("timing")
    @Expose
    private Integer timing;
    @SerializedName("date_last_timed")
    @Expose
    private String dateLastTimed;
    @SerializedName("task_source")
    @Expose
    private Object taskSource;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("project_name")
    @Expose
    private String projectName;
    @SerializedName("project_color")
    @Expose
    private String projectColor;
    @SerializedName("project_archived")
    @Expose
    private Integer projectArchived;
    @SerializedName("label_name")
    @Expose
    private String labelName;
    @SerializedName("label_color")
    @Expose
    private String labelColor;
    @SerializedName("label_archived")
    @Expose
    private Integer labelArchived;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("team_name")
    @Expose
    private Object teamName;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getAllocatedSeconds() {
        return allocatedSeconds;
    }

    public void setAllocatedSeconds(Integer allocatedSeconds) {
        this.allocatedSeconds = allocatedSeconds;
    }

    public String getAllocatedHours() {
        return allocatedHours;
    }

    public void setAllocatedHours(String allocatedHours) {
        this.allocatedHours = allocatedHours;
    }

    public Integer getActualSeconds() {
        return actualSeconds;
    }

    public void setActualSeconds(Integer actualSeconds) {
        this.actualSeconds = actualSeconds;
    }

    public String getActualHours() {
        return actualHours;
    }

    public void setActualHours(String actualHours) {
        this.actualHours = actualHours;
    }

    public Integer getComplete() {
        return complete;
    }

    public void setComplete(Integer complete) {
        this.complete = complete;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getLabelId() {
        return labelId;
    }

    public void setLabelId(Integer labelId) {
        this.labelId = labelId;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getTiming() {
        return timing;
    }

    public void setTiming(Integer timing) {
        this.timing = timing;
    }

    public String getDateLastTimed() {
        return dateLastTimed;
    }

    public void setDateLastTimed(String dateLastTimed) {
        this.dateLastTimed = dateLastTimed;
    }

    public Object getTaskSource() {
        return taskSource;
    }

    public void setTaskSource(Object taskSource) {
        this.taskSource = taskSource;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectColor() {
        return projectColor;
    }

    public void setProjectColor(String projectColor) {
        this.projectColor = projectColor;
    }

    public Integer getProjectArchived() {
        return projectArchived;
    }

    public void setProjectArchived(Integer projectArchived) {
        this.projectArchived = projectArchived;
    }

    public String getLabelName() {
        return labelName;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelColor() {
        return labelColor;
    }

    public void setLabelColor(String labelColor) {
        this.labelColor = labelColor;
    }

    public Integer getLabelArchived() {
        return labelArchived;
    }

    public void setLabelArchived(Integer labelArchived) {
        this.labelArchived = labelArchived;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Object getTeamName() {
        return teamName;
    }

    public void setTeamName(Object teamName) {
        this.teamName = teamName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
