package ru.cs.cstaskclient.repository;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * Created by lithTech on 06.12.2016.
 */
public class ApiManager {

    public static String API_URL = "https://tasks.cstechnology.ru";

    private static Converter.Factory jsonFactory;
    private static Converter.Factory stringFactory;

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonFactory = JacksonConverterFactory.create(objectMapper);

        stringFactory = new StringConverterFactory();
    }

    public static ProjectApi getProjectApi() {
        Retrofit retrofit = getRetrofit(jsonFactory);

        return retrofit.create(ProjectApi.class);
    }

    public static AuthApi getAuthApi() {
        return getRetrofit(stringFactory).create(AuthApi.class);
    }

    public static WorkTimeApi getWorkTimeApi() {
        return getRetrofit(jsonFactory).create(WorkTimeApi.class);
    }
    public static CategoryApi getCategoryApi() {
        return getRetrofit(jsonFactory).create(CategoryApi.class);
    }
    public static DiscussApi getDiscussApi() {
        return getRetrofit(jsonFactory).create(DiscussApi.class);
    }
    public static TaskApi getTaskApi() {
        return getRetrofit(jsonFactory).create(TaskApi.class);
    }
    public static UserApi getUserApi() {
        return getRetrofit(jsonFactory).create(UserApi.class);
    }
    public static LastActivityApi getLastActivityApi() {
        return getRetrofit(jsonFactory).create(LastActivityApi.class);
    }

    @NonNull
    public static Retrofit getRetrofit(Converter.Factory converterFactory) {
        //Interceptor logInterceptor = new HttpLoggingInterceptor();
        //((HttpLoggingInterceptor) logInterceptor).setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new SessionCookieInterceptor())
                .addInterceptor(new ReceiveCookiesInterceptor())
        //        .addInterceptor(logInterceptor)
                .build();
        Retrofit.Builder rb = new Retrofit.Builder()
                .baseUrl(API_URL)
                .callFactory(okHttpClient);
        if (converterFactory != null)
            rb.addConverterFactory(converterFactory);
        return rb.build();
    }

}
