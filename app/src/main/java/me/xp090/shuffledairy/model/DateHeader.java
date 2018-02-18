package me.xp090.shuffledairy.model;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractHeaderItem;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.viewholders.FlexibleViewHolder;
import me.xp090.shuffledairy.R;

/**
 * Created by Xp090 on 16/01/2018.
 */

public class DateHeader extends AbstractHeaderItem<DateHeader.HeaderViewHolder> implements IHeader<DateHeader.HeaderViewHolder> {
    public static final String HEADER_TAG ="header_tag";
    public static final int VIEW_TYPE_HEADER = 0;

    public long noteDate;

    public DateHeader(long noteDate) {
        setHidden(false);
        setEnabled(true);
        setSelectable(false);
        this.noteDate = noteDate;
    }

    @Override
    public int hashCode() {
        return ((Long)noteDate).hashCode();
    }

    @Override
    public boolean equals(Object o) {
       if (o instanceof DateHeader) {
            DateHeader dateHeader = (DateHeader) o;
            return dateHeader.noteDate == this.noteDate;
     }
       return o == this;
    }

    @Override
    public int getItemViewType() {
        return VIEW_TYPE_HEADER;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.row_header;
    }

    @Override
    public HeaderViewHolder createViewHolder(View view, FlexibleAdapter adapter) {
        return new HeaderViewHolder(view, adapter, true);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, HeaderViewHolder holder, int position, List<Object> payloads) {
        holder.dayMonth.setText(DateFormat.format("dd MMM",noteDate));
        holder.year.setText(DateFormat.format("yyyy",noteDate));
    }

    public static class HeaderViewHolder extends FlexibleViewHolder {
        public TextView dayMonth;
        public TextView year;

        public HeaderViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
            dayMonth = view.findViewById(R.id.txt_day_month);
            year = view.findViewById(R.id.txt_year);
        }
    }
}
