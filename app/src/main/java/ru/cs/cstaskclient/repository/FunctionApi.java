package ru.cs.cstaskclient.repository;

import retrofit2.Call;
import retrofit2.http.GET;
import ru.cs.cstaskclient.dto.WorkTime;

/**
 * Created by lithTech on 10.02.2017.
 */

public interface FunctionApi {

    @GET("/usersTasks")
    Call<String> getTaskAssignmentManager();

    @GET(value = "/tasks/workTimes/today/data")
    public Call<String> getTasks();}
