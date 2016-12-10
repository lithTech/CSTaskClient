package ru.cs.cstaskclient.repository;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.cs.cstaskclient.dto.Category;

/**
 * Created by lithTech on 08.12.2016.
 */

public interface CategoryApi {

    @GET("/ui/tree/ProjectCategory/data")
    public Call<List<Category>> getCategory(@Query("projectId") long projectId,
                                            @Query("id") long categoryId);

}
