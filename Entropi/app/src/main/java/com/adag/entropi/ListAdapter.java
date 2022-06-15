package com.adag.entropi;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{

    private ArrayList<String> listContent;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView itemText;
        public ViewHolder(TextView view) {
            super(view);
            itemText = view;
        }
    }

    public ListAdapter(ArrayList<String> dataList) {
        listContent = dataList;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.text_item, parent, false);
        ViewHolder vh = new ViewHolder(tv);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.itemText.setText(listContent.get(position));
    }

    @Override
    public int getItemCount() {
        return listContent.size();
    }
}
