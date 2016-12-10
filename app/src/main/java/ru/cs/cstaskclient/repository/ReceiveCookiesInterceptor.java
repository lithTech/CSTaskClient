package ru.cs.cstaskclient.repository;

import android.text.TextUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceiveCookiesInterceptor implements Interceptor {

    public static volatile Set<String> cookies = Collections.synchronizedSet(new HashSet<String>());

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            for (String header : originalResponse.headers("Set-Cookie")) {
              cookies.add(header);
            }
        }

        String url = originalResponse.networkResponse().request().url().toString();
        if (!TextUtils.isEmpty(url)) {
            url = url.toLowerCase();
            int pos = url.indexOf("jsessionid");
            if (pos > 0) {
                String jsessid = url.substring(pos);
                cookies.add(jsessid);
            }
        }

        return originalResponse;
    }

    public static String cookieToString() {
        return TextUtils.join(";", cookies);
    }
}