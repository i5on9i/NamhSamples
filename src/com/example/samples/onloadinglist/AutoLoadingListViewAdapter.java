package com.example.samples.onloadinglist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.samples.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: namh
 * Date: 13. 10. 1
 * Time: 오후 5:05
 * To change this template use File | Settings | File Templates.
 */
public class AutoLoadingListViewAdapter extends BaseAdapter {

    private final LayoutInflater mInflater;
    private ArrayList<String> mData = new ArrayList<String>();

    public AutoLoadingListViewAdapter(Context context) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public void addAll(List<String> newData) {
        mData.addAll(newData);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView = null;
        ViewHolder holder = null;

        if(convertView == null){

            holder = new ViewHolder();

            convertView = mInflater.inflate(R.layout.autoloadinglistview_item, null);
            holder.textView = (TextView)convertView.findViewById(R.id.textview);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
        }

        String entry = mData.get(position);
        holder.textView.setText(entry);

        return convertView;
    }

    private static class ViewHolder {
        public TextView textView;

    }
}
