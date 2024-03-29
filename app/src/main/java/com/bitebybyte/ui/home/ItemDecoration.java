package com.bitebybyte.ui.home;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Adds consistent margins between the recycler items and avoids doubling up on the margins.
 */
public class ItemDecoration extends RecyclerView.ItemDecoration {
    private final int columns;
    private final int margin;

    public ItemDecoration(int margin, int columns) {
        this.margin = margin;
        this.columns = columns;
    }

    public ItemDecoration() {
        this.margin = 32;
        this.columns = 1;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildLayoutPosition(view);
        //set right margin to all
        outRect.right = margin;
        //set bottom margin to all
        outRect.bottom = margin;
        //we only add top margin to the first row
        if (position < columns) {
            outRect.top = margin;
        }
        //add left margin only to the first column
        if (position % columns == 0) {
            outRect.left = margin;
        }
    }

}
