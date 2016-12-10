package ru.cs.cstaskclient.repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.cs.cstaskclient.dto.Project;

/**
 * Created by lithTech on 06.12.2016.
 */

public interface ProjectApi {


    @GET("/projects")
    Call<List<Project>> getProjects(@Query("status") String status);



}
