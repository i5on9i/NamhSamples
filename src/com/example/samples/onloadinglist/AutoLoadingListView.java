package com.example.samples.onloadinglist;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.example.samples.R;

import java.util.Arrays;
import java.util.List;

public class AutoLoadingListView extends ListView implements AbsListView.OnScrollListener {

    private final String TAG = AutoLoadingListView.class.getSimpleName();

    protected boolean mLoading = false;
    private ViewGroup mFooterView;
    private AutoLoadingListViewAdapter mAdapter;

    private OnAutoLoadListener mAutoLoadListener = null;

    public AutoLoadingListView(Context context) {
        super(context);
        init(context);

    }

    public AutoLoadingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public AutoLoadingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    private void init(Context context){
        mFooterView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.autoloadinglistview_loading, null);
        setOnScrollListener(this);
    }



    public void setAdapter(ListAdapter adapter) {

        mAdapter = (AutoLoadingListViewAdapter) adapter;

        View dummy = new View(getContext());
        super.addFooterView(dummy);   // to make mFooterViewInfos.size() > 0
        super.setAdapter(adapter);
        super.removeFooterView(dummy);


    }

    public void addLoadingProgress(){
        mLoading = true;
        addFooterView(mFooterView);
    }

    public void removeLoadingProgress(){
        mLoading = false;
        removeFooterView(mFooterView);
    }


    public void addAll(List list) {
        mAdapter.addAll(list);
    }


    public void setAutoLoadListener(OnAutoLoadListener listener){

        mAutoLoadListener = listener;
    }





    ////////////////////////////////////////////////////////////////////////////
    ////    Implement OnScrollListener
    ////
    @Override
    public final void onScrollStateChanged(final AbsListView view, final int scrollState) {
        super.onScreenStateChanged(scrollState);

    }

    @Override
    public final void onScroll(final AbsListView view, final int firstVisibleItem,
                               final int visibleItemCount, final int totalItemCount) {

        if(mAutoLoadListener != null){


            if(isListViewAlmostScrolled(firstVisibleItem, visibleItemCount, totalItemCount))
            {
                if(!mLoading)   // not to invoke several time at the same position
                {
                    mAutoLoadListener.onAutoLoad(totalItemCount);
                }
            }
        }

    }


    private boolean isListViewAlmostScrolled(int firstIndex, int visible, int total) {

        // When the 2 pages including currently showed page remains.
        final int pages = 2;
        final int offset = 2;
        if(visible > 0 && visible < total
                && (firstIndex + (pages * visible) + offset > total) )
        {
            return true;
        }
        return false;
    }



    ////////////////////////////////////////////////////////////////////////////
    ////    OnAutoLoadListener
    ////
    public static interface OnAutoLoadListener{
        public void onAutoLoad(int totalItemCount);
    }
}
