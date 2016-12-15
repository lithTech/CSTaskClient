package ru.cs.cstaskclient.dto;

/**
 * Created by lithTech on 15.12.2016.
 */

public class TaskCreateRequest {

    public long projectId;
    public long categoryId;
    public String title;
    public String creatorLogin;
    public String assigneeUserLogin;
    public String status;
    public String priority = "2middle";
    public String plannedEndDate;
    public String startDate;
    public String plannedLabor;
    public String vcsLink;
    public String tagIds;
    public String tagNames;
    public String descr;
    public String assignmentRoles;
    public String assignmentUsers;

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatorLogin() {
        return creatorLogin;
    }

    public void setCreatorLogin(String creatorLogin) {
        this.creatorLogin = creatorLogin;
    }

    public String getAssigneeUserLogin() {
        return assigneeUserLogin;
    }

    public void setAssigneeUserLogin(String assigneeUserLogin) {
        this.assigneeUserLogin = assigneeUserLogin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(String plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPlannedLabor() {
        return plannedLabor;
    }

    public void setPlannedLabor(String plannedLabor) {
        this.plannedLabor = plannedLabor;
    }

    public String getVcsLink() {
        return vcsLink;
    }

    public void setVcsLink(String vcsLink) {
        this.vcsLink = vcsLink;
    }

    public String getTagIds() {
        return tagIds;
    }

    public void setTagIds(String tagIds) {
        this.tagIds = tagIds;
    }

    public String getTagNames() {
        return tagNames;
    }

    public void setTagNames(String tagNames) {
        this.tagNames = tagNames;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getAssignmentRoles() {
        return assignmentRoles;
    }

    public void setAssignmentRoles(String assignmentRoles) {
        this.assignmentRoles = assignmentRoles;
    }

    public String getAssignmentUsers() {
        return assignmentUsers;
    }

    public void setAssignmentUsers(String assignmentUsers) {
        this.assignmentUsers = assignmentUsers;
    }
}
