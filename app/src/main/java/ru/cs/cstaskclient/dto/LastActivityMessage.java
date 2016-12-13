package ru.cs.cstaskclient.dto;

import android.text.TextUtils;

/**
 * Created by lithTech on 13.12.2016.
 */

public class LastActivityMessage extends BaseDTO {

    public long taskId;
    public long msgId;
    public String created;
    public String creator;
    public String title;
    public String vid;
    public String descr;

}
