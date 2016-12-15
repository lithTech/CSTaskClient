package ru.cs.cstaskclient.repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.cs.cstaskclient.dto.GridQueryRequest;
import ru.cs.cstaskclient.dto.GridQueryResultUsers;
import ru.cs.cstaskclient.dto.User;

/**
 * Created by lithTech on 07.12.2016.
 */

public interface UserApi {

    @POST("/ui/grid/UsersGrid/data")
    Call<GridQueryResultUsers> findUsers(@Body GridQueryRequest request);

    @GET("/users")
    Call<List<User>> getUsersReference();
}
