package me.xp090.shuffledairy.design;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import me.xp090.shuffledairy.R;
import me.xp090.shuffledairy.model.DairyNoteItem;
import me.xp090.shuffledairy.model.DateHeader;


public class TimelineDecor extends FlexibleItemDecoration {
    private DecorPrams decorPrams;
    private float linkX;
    private float noteLeftMargin;
    private float noteRightMargin;
    private float sx;
    private float ex;
    private float cx;
    private float cy;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);


    public TimelineDecor(DecorPrams decorPrams) {
        super(decorPrams.context);
        this.decorPrams = decorPrams;
        init();
    }

    private void init() {

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int position = recyclerView.getChildAdapterPosition(view);
        int viewType = ((FlexibleAdapter) recyclerView.getAdapter()).getItemViewType(position);
        if (viewType == DateHeader.VIEW_TYPE_HEADER) {
            outRect.bottom = - (int) (decorPrams.context.getResources().getDimension(R.dimen.first_item_top_margin)/2)  ;
        } else if (viewType == DairyNoteItem.VIEW_TYPE_ITEM) {
            outRect.bottom = (int) decorPrams.innerSpace;
        }


    }



    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        // outer
        c.save();
        float sy = 0;
        float ey = parent.getBottom();
        float top;
        float bottom;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ViewGroup currentChild = (ViewGroup) parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(currentChild);
            int viewType = ((FlexibleAdapter) parent.getAdapter()).getItemViewType(position);
            if (viewType == DateHeader.VIEW_TYPE_HEADER) {


                if (linkX == 0) {
                    View container = currentChild.getChildAt(0);
                    View linkView = ((ViewGroup) container).getChildAt(1);
                    linkX = linkView.getLeft();
                    sx = linkX + (decorPrams.outerDotRadius / 2) + (decorPrams.outerLineWidth*0.85f);
                    ex = sx;
                    paint.setColor(decorPrams.outerLineColor);

                }
                if (currentChild.getChildCount() > 0) {
                    top = currentChild.getTop();
                    bottom = currentChild.getBottom();


                } else {
                    top = currentChild.getBottom();
                    bottom = top + currentChild.getHeight();
                }
                cx = sx;
                cy = top + ((bottom - top) / 2);


            } else if (viewType == DairyNoteItem.VIEW_TYPE_ITEM) {
                if (noteLeftMargin == 0) {
                    View noteView = ((ViewGroup) currentChild).getChildAt(1);
                    float width = noteView.getWidth();
                    noteLeftMargin = noteView.getLeft() + width / 5;
                    noteRightMargin = noteView.getRight() - width / 6;
                }
                    View nextChild = parent.getChildAt(i + 1);
                    drawInnerLink(c, currentChild, nextChild, parent);
                    drawInnerLink(c, currentChild, nextChild, parent);
            }
            paint.setStrokeWidth(decorPrams.outerLineWidth);
            c.drawLine(sx, sy, ex, ey, paint);
         /*   paint.setColor(Color.BLACK);
            c.drawCircle(cx, cy, (decorPrams.outerDotRadius / 2), paint);
            paint.setColor(decorPrams.context.getResources().getColor(R.color.colorPrimary));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10f);
            c.drawCircle(cx, cy, (decorPrams.outerDotRadius / 2) - 10, paint);*/

            paint.setStyle(Paint.Style.FILL);

        }




/*
        for (int i = 0; i < childCount; i++) {
            View currentChild = parent.getChildAt(i);
            View rootLayout;
            if (currentChild instanceof FrameLayout) {
                rootLayout = ((FrameLayout) currentChild).getChildAt(0);

            } else {
                rootLayout = currentChild;
            }
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) rootLayout.getLayoutParams();
            int leftMargin = params.leftMargin;

            //c.drawLine(cx,cy,currentChild.getLeft() + leftMargin,cy,paint);
        }*/
    c.restore();
    }

    private void drawInnerLink(Canvas canvas, View currentChild, View nextChild, RecyclerView parent) {
        int position = parent.getChildAdapterPosition(currentChild);
        int viewType = ((FlexibleAdapter) parent.getAdapter()).getItemViewType(position+1);
        if (viewType == DairyNoteItem.VIEW_TYPE_ITEM) {
            float top = currentChild.getBottom() -15;
            float bottom;

            paint.setColor(decorPrams.context.getResources().getColor(R.color.colorBackground));
            canvas.drawCircle(noteLeftMargin, top, decorPrams.innerDotRadius *1.5f, paint);
            canvas.drawCircle(noteRightMargin, top, decorPrams.innerDotRadius *1.5f, paint);
            paint.setColor(Color.WHITE);
            canvas.drawCircle(noteLeftMargin, top, decorPrams.innerDotRadius , paint);
            canvas.drawCircle(noteRightMargin, top, decorPrams.innerDotRadius , paint);
            if (nextChild != null) {
                bottom = nextChild.getTop() + 15;
                paint.setColor(decorPrams.context.getResources().getColor(R.color.colorBackground));
                canvas.drawCircle(noteLeftMargin, bottom, decorPrams.innerDotRadius *1.5f, paint);
                canvas.drawCircle(noteRightMargin, bottom, decorPrams.innerDotRadius *1.5f, paint);
                paint.setColor(Color.WHITE);
                canvas.drawCircle(noteLeftMargin, bottom, decorPrams.innerDotRadius , paint);
                canvas.drawCircle(noteRightMargin, bottom, decorPrams.innerDotRadius , paint);
            } else {
                bottom = parent.getBottom();
            }
            paint.setStrokeWidth(decorPrams.innerLineWidth);
            canvas.drawLine(noteLeftMargin, top, noteLeftMargin, bottom, paint);
            canvas.drawLine(noteRightMargin, top, noteRightMargin, bottom, paint);

        }



    }

    public static class DecorPrams {

        private Context context;
        private float outerLineWidth;
        private float outerDotRadius;
        private int outerLineColor;
        private int outerDotColor;
        private float innerLineWidth;
        private float innerDotRadius;
        private int innerLineColor;
        private int innerDotColor;
        private float innerSpace;

        public DecorPrams(Builder builder) {
            this.context = builder.context;
            this.outerLineWidth = builder.outerLineWidth;
            this.outerDotRadius = builder.outerDotRadius;
            this.outerLineColor = builder.outerLineColor;
            this.outerDotColor = builder.outerDotColor;
            this.innerLineWidth = builder.innerLineWidth;
            this.innerDotRadius = builder.innerDotRadius;
            this.innerLineColor = builder.innerLineColor;
            this.innerDotColor = builder.innerDotColor;
            this.innerSpace = builder.innerSpace;
        }

        public static class Builder {

            private Context context;
            private float outerLineWidth;
            private float outerDotRadius;
            private int outerLineColor;
            private int outerDotColor;
            private float innerLineWidth;
            private float innerDotRadius;
            private int innerLineColor;
            private int innerDotColor;
            private float innerSpace;

            public Builder(Context context) {
                this.context = context;
            }

            public Builder setOuterLineWidth(float outerLineWidth) {
                this.outerLineWidth = outerLineWidth;
                return this;
            }

            public Builder setOuterDotRadius(float outerDotRadius) {
                this.outerDotRadius = outerDotRadius;
                return this;
            }

            public Builder setOuterLineColor(int outerLineColor) {
                this.outerLineColor = outerLineColor;
                return this;
            }

            public Builder setOuterDotColor(int outerDotColor) {
                this.outerDotColor = outerDotColor;
                return this;
            }

            public Builder setInnerLineWidth(float innerLineWidth) {
                this.innerLineWidth = innerLineWidth;
                return this;
            }

            public Builder setInnerDotRadius(float innerDotRadius) {
                this.innerDotRadius = innerDotRadius;
                return this;
            }

            public Builder setInnerLineColor(int innerLineColor) {
                this.innerLineColor = innerLineColor;
                return this;
            }

            public Builder setInnerDotColor(int innerDotColor) {
                this.innerDotColor = innerDotColor;
                return this;
            }

            public Builder setinnerSpace(float innerSpace) {
                this.innerSpace = innerSpace;
                return this;
            }

            public DecorPrams build() {
                return new DecorPrams(this);
            }
        }


    }


}
