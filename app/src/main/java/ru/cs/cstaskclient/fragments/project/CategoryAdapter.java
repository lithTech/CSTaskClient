package ru.cs.cstaskclient.fragments.project;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.Category;

/**
 * Created by lithTech on 08.12.2016.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {
    public CategoryAdapter(Context context, List<Category> objects) {
        super(context, R.layout.list_category_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_category_item,
                    parent, false);

        Category category = getItem(position);

        TextView tvCategory = (TextView) convertView.findViewById(R.id.tvCategory);
        tvCategory.setText(category.title);

        int color = Color.parseColor(category.color);
        tvCategory.setTextColor(color);

        return convertView;
    }
}
