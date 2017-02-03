package ru.cs.cstaskclient.repository;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;

/**
 * Created by lithTech on 06.12.2016.
 */

public final class SessionCookieInterceptor implements Interceptor {
    public static volatile String sessionCookie;

    public static String getSessionId() {
        String s = sessionCookie.toLowerCase();
        int pos = s.indexOf("jsessionid=");
        if (s.contains(";"))
            return s.substring(pos + 11, s.indexOf(";", pos));
        else return s.substring(pos + 11);
    }

    public static void setSessionCookie(String cookieString) {
        sessionCookie = cookieString;
    }

    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (this.sessionCookie != null) {
            request = request.newBuilder()
                    .header("Cookie", this.sessionCookie)
                    .build();
        }
        return chain.proceed(request);
    }
}