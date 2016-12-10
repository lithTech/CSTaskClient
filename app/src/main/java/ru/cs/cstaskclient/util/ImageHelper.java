package ru.cs.cstaskclient.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.Base64;

/**
 * Created by lithTech on 09.12.2016.
 */

public class ImageHelper {

    @Nullable
    public static BitmapDrawable createDrawableByBase64Str(Context context, String base64String, int maxW) {
        int pos = base64String.indexOf("base64,");
        if (!base64String.startsWith("data:image") && pos < 0)
            return null;
        String dataStr = base64String.substring(pos + 7);
        byte[] data = Base64.decode(dataStr, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        BitmapDrawable drawable = new BitmapDrawable(context.getResources(), bitmap);

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        if (w > maxW)
        {
            h = (int) (((double) h / (double) w) * maxW);
            w = maxW;
        }

        drawable.setBounds(0, 0, w, h);
        return drawable;
    }
}
