package com.farasatnovruzov.recyclerlist.notification;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class SpeedyLinearLayoutManager extends LinearLayoutManager {

    private static final float MILISECONDS_PER_INCH = 35f;//default is 25f (bigger = slower)
    public SpeedyLinearLayoutManager(Context context){
        super(context);
    }

    public SpeedyLinearLayoutManager(Context context,int orientation, boolean reverseLayout){
        super(context,orientation,reverseLayout);
    }

    public SpeedyLinearLayoutManager(Context context, AttributeSet attrs,int defStyleAttr, int defStyleRes){
        super(context,attrs,defStyleAttr,defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//        super.smoothScrollToPosition(recyclerView, state, position);
        final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()){
            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return super.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return super.calculateSpeedPerPixel(displayMetrics);
            }
        };

        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);
    }
}