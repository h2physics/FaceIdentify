package com.vietanhs0817.faceidentify;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

/**
 * Created by YukiNoHara on 10/29/2017.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration{
    private static final String LOG_TAG = GridItemDecoration.class.getSimpleName();
    private int spanCount;
    private int spacing;
    private boolean includeEdge;
    private int headerNum;

    public GridItemDecoration(int spanCount, int spacing, boolean includeEdge, int headerNum) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
        this.headerNum = headerNum;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (parent.getChildAdapterPosition(view) < 0){
            return;
        }
        int position = parent.getChildAdapterPosition(view) - headerNum;

        if (position >= 0){
            int column = position % spanCount;
            if (includeEdge){
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;
                if (position < spanCount){
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position > spanCount){
                    outRect.top = spacing;
                }
            }
        }
    }
}
