package ru.cs.cstaskclient.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;

/**
 * Created by lithTech on 15.12.2016.
 */

public class InstantMultiAutoComplete extends MultiAutoCompleteTextView {
    public InstantMultiAutoComplete(Context context) {
        super(context);
    }

    public InstantMultiAutoComplete(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InstantMultiAutoComplete(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InstantMultiAutoComplete(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean enoughToFilter() {
        return true;
    }

    @Override
    public boolean performClick() {
        super.performClick();

        showDropDown();

        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if (focused && getAdapter() != null) {
            performFiltering(getText(), 0);
            showDropDown();
        }
    }
}
