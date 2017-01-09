package ru.cs.cstaskclient.fragments.discuss;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.cs.cstaskclient.R;
import ru.cs.cstaskclient.dto.Discuss;
import ru.cs.cstaskclient.util.ImageHelper;

/**
 * Created by lithTech on 09.12.2016.
 */

public class DiscussListAdapter extends ArrayAdapter<Discuss> {

    LruCache<Integer, BitmapDrawable> imgCache;

    public DiscussListAdapter(Context context, int resource, List<Discuss> objects) {
        super(context, R.layout.list_discuss_item, objects);

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

    public int getPositionByMsgId(long msgId) {
        for(int i = 0; i < getCount(); i++) {
            if (getItem(i).id == msgId)
                return i;
        }
        return -1;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_discuss_item,
                    parent, false);

        final int maxW = parent.getMeasuredWidth();

        Discuss discuss = getItem(position);

        TextView tvMessage = (TextView) convertView.findViewById(R.id.tvMessage);
        TextView tvMessageDate = (TextView) convertView.findViewById(R.id.tvMessageDate);
        tvMessageDate.setText(discuss.createdDateTime);

        TextView tvAuthor = (TextView) convertView.findViewById(R.id.tvMessageAuthor);
        tvAuthor.setText(discuss.creatorFullName);

        TextView files = (TextView) convertView.findViewById(R.id.tvMessageFileList);
        files.setVisibility(discuss.files == null || discuss.files.isEmpty() ? View.GONE : View.VISIBLE);
        if (discuss.files != null)
            files.setText(TextUtils.join("\n", discuss.files));

        tvMessage.setText(Html.fromHtml(discuss.message, new Html.ImageGetter() {
            @Override
            public Drawable getDrawable(String s) {
                BitmapDrawable img = imgCache.get(s.hashCode());
                if (img == null) {
                    img = ImageHelper.createDrawableByBase64Str(getContext(), s, maxW);
                    imgCache.put(s.hashCode(), img);
                }

                return img;
            }
        }, null));

        return convertView;
    }

}
