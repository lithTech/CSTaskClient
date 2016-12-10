package ru.cs.cstaskclient.repository;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import ru.cs.cstaskclient.dto.GridQueryRequest;
import ru.cs.cstaskclient.dto.GridQueryResult;
import ru.cs.cstaskclient.dto.GridQueryResultUsers;
import ru.cs.cstaskclient.dto.SessionUser;

/**
 * Created by lithTech on 06.12.2016.
 */

public interface AuthApi {

    @GET("/login")
    Call<String> jSessionIdCall();

    @FormUrlEncoded
    @POST(value = "/j_spring_security_check")
    Call<String> auth(@Field("j_username") String user, @Field("j_password") String password);

}
