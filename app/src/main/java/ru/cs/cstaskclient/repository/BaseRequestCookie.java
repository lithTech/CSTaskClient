package ru.cs.cstaskclient.repository;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * Created by lithTech on 06.12.2016.
 */

class BaseRequestCookie extends CookieManager {

    public static String JSESSIONID;

    @Override
    public void put(URI uri, Map<String, List<String>> stringListMap) throws IOException {
        super.put(uri, stringListMap);
        if (stringListMap != null && stringListMap.get("Set-Cookie") != null)
            for (String string : stringListMap.get("Set-Cookie")) {
                if (string.contains("JSESSIONID"))
                    JSESSIONID = string;
                else if (string.contains("jsessionid"))
                    JSESSIONID = string;
            }
    }
}