package ru.cs.cstaskclient.dto;

/**
 * Created by lithTech on 21.12.2016.
 */

public class GridQueryRequestAssignedTask extends GridQueryRequest {

    public Long userId;
    public String mode = "all";
    public String role = "developer";

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        mode = "all";
        role = "developer";
        if (userId == null) {
            mode = "notAppointed";
            role = "manager";
        }
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
