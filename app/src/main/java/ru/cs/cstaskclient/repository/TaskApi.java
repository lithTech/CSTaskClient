package ru.cs.cstaskclient.repository;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.cs.cstaskclient.dto.GridQueryRequestAssignedTask;
import ru.cs.cstaskclient.dto.GridQueryRequestTask;
import ru.cs.cstaskclient.dto.GridQueryResultTask;
import ru.cs.cstaskclient.dto.Tag;
import ru.cs.cstaskclient.dto.Task;
import ru.cs.cstaskclient.dto.TaskCreateRequest;
import ru.cs.cstaskclient.dto.TaskStatus;

/**
 * Created by lithTech on 08.12.2016.
 */

public interface TaskApi {

    @POST("/ui/grid/ProjectCategoryTask/data")
    public Call<GridQueryResultTask> getTasks(@Body GridQueryRequestTask request);

    @GET("/tasks/favorite")
    public Call<GridQueryResultTask> getFavoriteTasks();

    @GET("/tasks/statuses")
    public Call<List<TaskStatus>> getTaskStatuses();

    @GET("/tags")
    public Call<List<Tag>> getTags();

    @FormUrlEncoded
    @POST("/tasks")
    public Call<Task> create(@FieldMap Map<String, Object> request,
                             @Field("tagIds") List<Long> tagIds);

    @POST("/ui/grid/ProjectCategoryTask/data")
    public Call<GridQueryResultTask> getAssignedTasks(@Body GridQueryRequestAssignedTask request);
}
