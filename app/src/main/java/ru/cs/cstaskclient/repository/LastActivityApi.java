package ru.cs.cstaskclient.repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.cs.cstaskclient.dto.GridQueryRequest;
import ru.cs.cstaskclient.dto.GridQueryResultLastActivity;
import ru.cs.cstaskclient.dto.LastActivityMessage;

/**
 * Created by lithTech on 13.12.2016.
 */

public interface LastActivityApi {

    @POST("/ui/grid/TaskDiscuss/data")
    public Call<GridQueryResultLastActivity> getLastActivity(@Body GridQueryRequest request);

}
