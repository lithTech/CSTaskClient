package ru.cs.cstaskclient.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lithTech on 13.12.2016.
 */
public class GridSort {
    public String field;
    public GridSortDir dir;

    public GridSort(String field, GridSortDir dir) {
        this.field = field;
        this.dir = dir;
    }
}
