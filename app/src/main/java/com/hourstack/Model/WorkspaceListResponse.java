package com.hourstack.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class WorkspaceListResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("hash")
    @Expose
    private String hash;
    @SerializedName("organization_name")
    @Expose
    private Object organizationName;
    @SerializedName("organization_address")
    @Expose
    private Object organizationAddress;
    @SerializedName("organization_vat")
    @Expose
    private Object organizationVat;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("active")
    @Expose
    private Integer active;
    @SerializedName("team_id")
    @Expose
    private Object teamId;
    @SerializedName("hours_in_week")
    @Expose
    private Integer hoursInWeek;
    @SerializedName("weekends")
    @Expose
    private Integer weekends;
    @SerializedName("hours_in_day1")
    @Expose
    private Integer hoursInDay1;
    @SerializedName("hours_in_day2")
    @Expose
    private Integer hoursInDay2;
    @SerializedName("hours_in_day3")
    @Expose
    private Integer hoursInDay3;
    @SerializedName("hours_in_day4")
    @Expose
    private Integer hoursInDay4;
    @SerializedName("hours_in_day5")
    @Expose
    private Integer hoursInDay5;
    @SerializedName("hours_in_day6")
    @Expose
    private Integer hoursInDay6;
    @SerializedName("hours_in_day7")
    @Expose
    private Integer hoursInDay7;
    @SerializedName("default_allocated_seconds")
    @Expose
    private Integer defaultAllocatedSeconds;
    @SerializedName("role")
    @Expose
    private String role;
    @SerializedName("permission_workspace")
    @Expose
    private Integer permissionWorkspace;
    @SerializedName("permission_workspace_users")
    @Expose
    private Integer permissionWorkspaceUsers;
    @SerializedName("permission_workspace_view")
    @Expose
    private Integer permissionWorkspaceView;
    @SerializedName("permission_workspace_entries")
    @Expose
    private Integer permissionWorkspaceEntries;
    @SerializedName("permission_own_entries")
    @Expose
    private Integer permissionOwnEntries;
    @SerializedName("permission_past_entries")
    @Expose
    private Integer permissionPastEntries;
    @SerializedName("permission_templates")
    @Expose
    private Integer permissionTemplates;
    @SerializedName("permission_projects")
    @Expose
    private Integer permissionProjects;
    @SerializedName("permission_labels")
    @Expose
    private Integer permissionLabels;
    @SerializedName("permission_teams")
    @Expose
    private Integer permissionTeams;
    @SerializedName("permission_reports")
    @Expose
    private Integer permissionReports;

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

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Object getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(Object organizationName) {
        this.organizationName = organizationName;
    }

    public Object getOrganizationAddress() {
        return organizationAddress;
    }

    public void setOrganizationAddress(Object organizationAddress) {
        this.organizationAddress = organizationAddress;
    }

    public Object getOrganizationVat() {
        return organizationVat;
    }

    public void setOrganizationVat(Object organizationVat) {
        this.organizationVat = organizationVat;
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

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public Object getTeamId() {
        return teamId;
    }

    public void setTeamId(Object teamId) {
        this.teamId = teamId;
    }

    public Integer getHoursInWeek() {
        return hoursInWeek;
    }

    public void setHoursInWeek(Integer hoursInWeek) {
        this.hoursInWeek = hoursInWeek;
    }

    public Integer getWeekends() {
        return weekends;
    }

    public void setWeekends(Integer weekends) {
        this.weekends = weekends;
    }

    public Integer getHoursInDay1() {
        return hoursInDay1;
    }

    public void setHoursInDay1(Integer hoursInDay1) {
        this.hoursInDay1 = hoursInDay1;
    }

    public Integer getHoursInDay2() {
        return hoursInDay2;
    }

    public void setHoursInDay2(Integer hoursInDay2) {
        this.hoursInDay2 = hoursInDay2;
    }

    public Integer getHoursInDay3() {
        return hoursInDay3;
    }

    public void setHoursInDay3(Integer hoursInDay3) {
        this.hoursInDay3 = hoursInDay3;
    }

    public Integer getHoursInDay4() {
        return hoursInDay4;
    }

    public void setHoursInDay4(Integer hoursInDay4) {
        this.hoursInDay4 = hoursInDay4;
    }

    public Integer getHoursInDay5() {
        return hoursInDay5;
    }

    public void setHoursInDay5(Integer hoursInDay5) {
        this.hoursInDay5 = hoursInDay5;
    }

    public Integer getHoursInDay6() {
        return hoursInDay6;
    }

    public void setHoursInDay6(Integer hoursInDay6) {
        this.hoursInDay6 = hoursInDay6;
    }

    public Integer getHoursInDay7() {
        return hoursInDay7;
    }

    public void setHoursInDay7(Integer hoursInDay7) {
        this.hoursInDay7 = hoursInDay7;
    }

    public Integer getDefaultAllocatedSeconds() {
        return defaultAllocatedSeconds;
    }

    public void setDefaultAllocatedSeconds(Integer defaultAllocatedSeconds) {
        this.defaultAllocatedSeconds = defaultAllocatedSeconds;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Integer getPermissionWorkspace() {
        return permissionWorkspace;
    }

    public void setPermissionWorkspace(Integer permissionWorkspace) {
        this.permissionWorkspace = permissionWorkspace;
    }

    public Integer getPermissionWorkspaceUsers() {
        return permissionWorkspaceUsers;
    }

    public void setPermissionWorkspaceUsers(Integer permissionWorkspaceUsers) {
        this.permissionWorkspaceUsers = permissionWorkspaceUsers;
    }

    public Integer getPermissionWorkspaceView() {
        return permissionWorkspaceView;
    }

    public void setPermissionWorkspaceView(Integer permissionWorkspaceView) {
        this.permissionWorkspaceView = permissionWorkspaceView;
    }

    public Integer getPermissionWorkspaceEntries() {
        return permissionWorkspaceEntries;
    }

    public void setPermissionWorkspaceEntries(Integer permissionWorkspaceEntries) {
        this.permissionWorkspaceEntries = permissionWorkspaceEntries;
    }

    public Integer getPermissionOwnEntries() {
        return permissionOwnEntries;
    }

    public void setPermissionOwnEntries(Integer permissionOwnEntries) {
        this.permissionOwnEntries = permissionOwnEntries;
    }

    public Integer getPermissionPastEntries() {
        return permissionPastEntries;
    }

    public void setPermissionPastEntries(Integer permissionPastEntries) {
        this.permissionPastEntries = permissionPastEntries;
    }

    public Integer getPermissionTemplates() {
        return permissionTemplates;
    }

    public void setPermissionTemplates(Integer permissionTemplates) {
        this.permissionTemplates = permissionTemplates;
    }

    public Integer getPermissionProjects() {
        return permissionProjects;
    }

    public void setPermissionProjects(Integer permissionProjects) {
        this.permissionProjects = permissionProjects;
    }

    public Integer getPermissionLabels() {
        return permissionLabels;
    }

    public void setPermissionLabels(Integer permissionLabels) {
        this.permissionLabels = permissionLabels;
    }

    public Integer getPermissionTeams() {
        return permissionTeams;
    }

    public void setPermissionTeams(Integer permissionTeams) {
        this.permissionTeams = permissionTeams;
    }

    public Integer getPermissionReports() {
        return permissionReports;
    }

    public void setPermissionReports(Integer permissionReports) {
        this.permissionReports = permissionReports;
    }
}
