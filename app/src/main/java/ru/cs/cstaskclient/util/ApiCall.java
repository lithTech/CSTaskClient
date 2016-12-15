package ru.cs.cstaskclient.util;

import android.app.Activity;
import android.widget.Toast;

import retrofit2.*;
import ru.cs.cstaskclient.R;

/**
 * Created by lithTech on 15.12.2016.
 */

public abstract class ApiCall<T> implements retrofit2.Callback<T> {

    protected Activity activity;

    public ApiCall(Activity activity) {
        this.activity = activity;
    }

    public abstract void onResponse(Response<T> r);

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.body() != null)
            onResponse(response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Toast.makeText(activity, activity.getString(R.string.error_api_operation), Toast.LENGTH_LONG)
                .show();
        t.printStackTrace();
    }
}
