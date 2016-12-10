package ru.cs.cstaskclient.dto;

public class WorkTimeElem {
    public String taskDate;
    public String link;
    public TimeElem workTimeDTO;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link.replaceAll("<[^>]*>", "");
    }
}
