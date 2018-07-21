package com.archidni.archidni.UiUtils;

import android.support.v4.widget.NestedScrollView;
import android.view.View;

import com.sothree.slidinguppanel.ScrollableViewHelper;

public class NestedScrollableHelper extends ScrollableViewHelper {
    public int getScrollableViewScrollPosition(View scrollableView, boolean isSlidingUp) {
        if (scrollableView instanceof NestedScrollView) {
            if(isSlidingUp){
                return scrollableView.getScrollY();
            } else {
                NestedScrollView nsv = ((NestedScrollView) scrollableView);
                View child = nsv.getChildAt(0);
                return (child.getBottom() - (nsv.getHeight() + nsv.getScrollY()));
            }
        } else {
            return 0;
        }
    }
}
