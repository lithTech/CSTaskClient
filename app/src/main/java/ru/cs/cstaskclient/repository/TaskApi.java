package ru.cs.cstaskclient.repository;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.cs.cstaskclient.dto.GridQueryRequestTask;
import ru.cs.cstaskclient.dto.GridQueryResultTask;

/**
 * Created by lithTech on 08.12.2016.
 */

public interface TaskApi {

    @POST("/ui/grid/ProjectCategoryTask/data")
    public Call<GridQueryResultTask> getTasks(@Body GridQueryRequestTask request);

    @GET("/tasks/favorite")
    public Call<GridQueryResultTask> getFavoriteTasks();
}
