package ru.cs.cstaskclient.repository;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.cs.cstaskclient.dto.WorkTime;

/**
 * Created by lithTech on 07.12.2016.
 */

public interface WorkTimeApi {

    @GET(value = "/tasks/workTimes/today/data")
    public Call<WorkTime> getTasks();

    @FormUrlEncoded
    @POST("/tasks/workTimes")
    public Call<ResponseBody> createWorkTime(@Field("workDescr") String workDescr,
                                             @Field("workTime") int workTime,
                                             @Field("workTimeDim") String workTimeDim,
                                             @Field("creatorId") long creatorId,
                                             @Field("taskId") long taskId);

    @FormUrlEncoded
    @PUT("/tasks/workTimes/{id}")
    public Call<ResponseBody> updateWorkTime(@Field("workDescr") String workDescr,
                                             @Field("workTime") int workTime,
                                             @Field("workTimeDim") String workTimeDim,
                                             @Field("creatorId") long creatorId,
                                             @Field("taskId") long taskId,
                                             @Path("id") long workTimeId,
                                             @Field("id") long workTimeIdTheSame);
}
