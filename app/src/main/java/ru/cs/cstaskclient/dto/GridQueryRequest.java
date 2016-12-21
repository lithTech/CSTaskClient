package ru.cs.cstaskclient.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lithTech on 07.12.2016.
 */

public class GridQueryRequest {
    public int page = 1;
    public int pageSize = 15;
    public int skip = 0;
    public int take = 15;

    public GridFilter filter;

    public List<GridSort> sort = new ArrayList<>();

    public GridQueryRequest() {
    }

    public static <T extends GridQueryRequest> T getSimple(Class<T> tClass, String field, String operator, String value) {
        T r = null;
        try {
            r = tClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        r.filter = new GridFilter();
        r.filter.filters = new ArrayList<>(1);
        GridFilterElement gr = new GridFilterElement();
        r.filter.filters.add(gr);
        gr.field = field;
        gr.operator = operator;
        gr.value = value;
        return r;
    }

    public static <T extends GridQueryRequest> T getSimple(Class<T> tClass,
                                                           String field, String operator, String value,
                                                           String sortField, GridSortDir sortDir) {
        T r = getSimple(tClass, field, operator, value);

        GridSort e = new GridSort(sortField, sortDir);

        r.sort.add(e);

        return r;
    }

    public static <T extends GridQueryRequest> T getSimple(Class<T> tClass,
                                                           String sortField,
                                                           GridSortDir sortDir) {
        T r = null;
        try {
            r = tClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        GridSort e = new GridSort(sortField, sortDir);

        r.sort.add(e);

        return r;
    }

}
