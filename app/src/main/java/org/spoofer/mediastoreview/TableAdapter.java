package org.spoofer.mediastoreview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.ViewHolder> {

    private final int DEFAULT_CELL_WIDTH = 100;

    private final List<Title> titles = new ArrayList<>();
    private final List<List<String>> rows = new ArrayList<>();
    private TableAdapter.ViewHolder titleViewHolder;
    private OnClickTableListener onClickTableListener;

    private final LayoutInflater layoutInflater;

    public TableAdapter(Context context) {
        this.layoutInflater = LayoutInflater.from(context);
    }

    public void setOnClickTableListener(OnClickTableListener onClickTableListener) {
        this.onClickTableListener = onClickTableListener;
    }

    public List<List<String>> getRows() {
        return rows;
    }

    public List<String> getTitles() {
        List<String> titles = new ArrayList<>();
        for (Title title : this.titles)
            titles.add(title.title);
        return titles;
    }

    public void setTitlesView(ViewGroup titlesView) {
        titleViewHolder = new TitleViewHolder(titlesView);
    }

    public void setTitles(List<String> titles) {
        clear();
        for (String t : titles) {
            this.titles.add(new Title(t));
        }
        if (null != titleViewHolder) {
            titleViewHolder.bind(titles);
            titleViewHolder.itemView.requestLayout();
        }
    }

    public void setRows(List<List<String>> rows) {
        this.rows.clear();
        this.rows.addAll(rows);
    }

    public void clear() {
        titles.clear();
        rows.clear();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.table_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(rows.get(position));
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ViewGroup rowParent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rowParent = (ViewGroup) itemView;
        }

        public void bind(List<String> row) {

            rowParent.removeAllViews();
            int index = 0;
            for (String value : row) {

                TextView tv = makeCell(rowParent, index);
                tv.setText(value);
                rowParent.addView(tv);
                index++;
            }
            rowParent.requestLayout();
        }

        protected TextView makeCell(ViewGroup parent, int index) {
            TextView tv = (TextView) layoutInflater.
                    inflate(R.layout.table_cell, parent, false);
            tv.setWidth(getColumnWidth(index));
            return tv;
        }

    }

    public class TitleViewHolder extends ViewHolder {
        @ColorInt
        private final int cellColour;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
            cellColour = itemView.getResources().getColor(R.color.colorPrimaryShade);
        }

        @Override
        protected TextView makeCell(ViewGroup parent, int index) {
            TextView tv = (TextView) layoutInflater.
                    inflate(R.layout.title_cell, parent, false);
            tv.setMinWidth(DEFAULT_CELL_WIDTH);
            tv.setTag(index);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null != onClickTableListener) {
                        int position = (int) view.getTag();
                        onClickTableListener.onClick(position, titles.get(position).title);
                    }
                }
            });
            return tv;
        }
    }

    private class Title {
        final String title;
        int width = DEFAULT_CELL_WIDTH;

        public Title(String title) {
            this.title = title;
        }
    }

    private int getColumnWidth(int position) {
        ViewGroup parentView = (ViewGroup) titleViewHolder.itemView;

        int width = position < parentView.getChildCount() ?
                parentView.getChildAt(position).getMeasuredWidth() : -1;
        return Math.max(width, DEFAULT_CELL_WIDTH);
    }

    public interface OnClickTableListener {
        void onClick(int column, String columnName);
    }
}
