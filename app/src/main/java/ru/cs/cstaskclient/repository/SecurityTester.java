package ru.cs.cstaskclient.repository;

import android.app.Activity;

import retrofit2.Call;
import retrofit2.Response;
import ru.cs.cstaskclient.dto.WorkTime;
import ru.cs.cstaskclient.util.ApiCall;
import ru.cs.cstaskclient.util.Callback;

/**
 * Created by lithTech on 10.02.2017.
 */

public class SecurityTester {

    public static void assignedTasksAvailable(Activity activity,
                                                 FunctionApi functionApi,
                                                 final Callback callback) {
        Call<String> resp = functionApi.getTaskAssignmentManager();
        resp.enqueue(new ApiCall<String>(activity) {
            @Override
            public void onResponse(Response<String> r) {
                if (r.body() != null && !r.body().contains("Access is denied")) {
                    callback.done(true);
                    return;
                }

                callback.done(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.done(false);
            }
        });
    }

    public static void workTimesAvailable(Activity activity,
                                                 FunctionApi functionApi,
                                                 final Callback callback) {
        Call<String> resp = functionApi.getTasks();
        resp.enqueue(new ApiCall<String>(activity) {
            @Override
            public void onResponse(Response<String> r) {
                if (r.body() != null) {
                    callback.done(true);
                    return;
                }

                callback.done(false);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                callback.done(false);
            }
        });
    }

}
