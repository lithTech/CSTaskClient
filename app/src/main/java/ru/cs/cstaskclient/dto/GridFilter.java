package ru.cs.cstaskclient.dto;

import java.util.List;

/**
 * Created by lithTech on 07.12.2016.
 */

public class GridFilter {
    public String logic = "and";

    public List<GridFilterElement> filters;
}
