package ru.cs.cstaskclient.repository;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import ru.cs.cstaskclient.dto.Discuss;
import ru.cs.cstaskclient.dto.GridQueryRequest;
import ru.cs.cstaskclient.dto.GridQueryResultDiscuss;

/**
 * Created by lithTech on 09.12.2016.
 */

public interface DiscussApi {

    @POST("tasks/{taskId}/discusses")
    public Call<GridQueryResultDiscuss> getDiscuss(@Path("taskId") long taskId,
                                                   @Body GridQueryRequest request);

    @Multipart
    @POST("/discusses")
    public Call<Discuss> sendMessage(@Part("taskId") long taskId,
                               @Part("message") String message);

}
