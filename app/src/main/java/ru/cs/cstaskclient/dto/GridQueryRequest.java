package ru.cs.cstaskclient.dto;

import java.util.ArrayList;

/**
 * Created by lithTech on 07.12.2016.
 */

public class GridQueryRequest {
    public int page = 1;
    public int pageSize = 15;
    public int skip = 0;
    public int take = 15;

    public GridFilter filter;


    public static GridQueryRequest getSimple(String field, String operator, String value) {
        GridQueryRequest r = new GridQueryRequest();
        r.filter = new GridFilter();
        r.filter.filters = new ArrayList<>(1);
        GridFilterElement gr = new GridFilterElement();
        r.filter.filters.add(gr);
        gr.field = field;
        gr.operator = operator;
        gr.value = value;
        return r;
    }

}
