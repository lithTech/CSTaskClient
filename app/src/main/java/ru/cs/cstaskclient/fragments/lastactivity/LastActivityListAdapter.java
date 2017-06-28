package ru.cs.cstaskclient.fragments.lastactivity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.util.LruCache;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.Discuss;
import ru.cs.cstaskclient.dto.LastActivityMessage;
import ru.cs.cstaskclient.util.ImageHelper;

/**
 * Created by lithTech on 09.12.2016.
 */

public class LastActivityListAdapter extends ArrayAdapter<LastActivityMessage> {

    LruCache<Integer, BitmapDrawable> imgCache;

    public LastActivityListAdapter(Context context, int resource, List<LastActivityMessage> objects) {
        super(context, R.layout.list_last_activity_item, objects);

        //calculate cache size for images
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        final int cacheSize = maxMemory / 8;

        imgCache = new LruCache<Integer, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(Integer key, BitmapDrawable drawable) {
                return drawable.getBitmap().getByteCount() / 1024;
            }
        };

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_last_activity_item,
                    parent, false);

        final int maxW = parent.getMeasuredWidth();

        LastActivityMessage la = getItem(position);

        TextView tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
        TextView tvTaskTitle = (TextView) convertView.findViewById(R.id.tvTaskTitle);
        tvTaskTitle.setText("#"+la.vid+" "+la.title);
        TextView tvMessageDate = (TextView) convertView.findViewById(R.id.tvMessageDate);
        tvMessageDate.setText(la.created);


        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tvMessageAuthor);
        tvAuthor.setText(la.creator);

        tvMessage.setText(Html.fromHtml(la.descr, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String s) {
                if (TextUtils.isEmpty(s)) return null;
                BitmapDrawable img = imgCache.get(s.hashCode());
                if (img == null) {
                    img = ImageHelper.createDrawableByBase64Str(getContext(), s, maxW);
                    if (img == null) return null;
                    imgCache.put(s.hashCode(), img);
                }

                return img;
            }
        }, null));

        return convertView;
    }

}
