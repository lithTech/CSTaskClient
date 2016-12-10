package ru.cs.cstaskclient.repository;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by lithTech on 06.12.2016.
 */

public final class CookieInterceptor implements Interceptor {
    private static volatile String cookie;

    public static void setSessionCookie(String cookieString) {
        cookie = cookieString;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (this.cookie != null) {
            request = request.newBuilder()
                    .header("Cookie", this.cookie)
                    .build();
        }
        return chain.proceed(request);
    }
}