package ru.cs.cstaskclient.dto;

import java.util.List;

/**
 * Created by lithTech on 08.12.2016.
 */

public class Task extends BaseDTO {

    public String vid;
    public String title;
    public String statusLocaleName;
    public String startDate;
    public String endDate;
    public String plannedEndDate;
    public String plannedLabor;
    public String assigneeUserFullName;
    public String creatorFullName;

    public int progress;

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public String getVid() {
        return vid;
    }

    public void setVid(String vid) {
        this.vid = vid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatusLocaleName() {
        return statusLocaleName;
    }

    public void setStatusLocaleName(String statusLocaleName) {
        this.statusLocaleName = statusLocaleName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(String plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public String getPlannedLabor() {
        return plannedLabor;
    }

    public void setPlannedLabor(String plannedLabor) {
        this.plannedLabor = plannedLabor;
    }

    public String getAssigneeUserFullName() {
        return assigneeUserFullName;
    }

    public void setAssigneeUserFullName(String assigneeUserFullName) {
        this.assigneeUserFullName = assigneeUserFullName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }
}
