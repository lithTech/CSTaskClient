package ru.cs.cstaskclient.dto;

/**
 * Created by lithTech on 06.12.2016.
 */

public class BaseDTO {

    public long id;
    public long _id;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
        this.id = _id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
        this._id = id;
    }
}
