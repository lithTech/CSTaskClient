package ru.cs.cstaskclient.dto;

import java.util.List;

/**
 * Created by lithTech on 09.12.2016.
 */

public class Discuss extends BaseDTO {

    public String creatorFullName;
    public String message;
    public String createdDateTime;

    public List<File> files = null;

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(String createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }
}
