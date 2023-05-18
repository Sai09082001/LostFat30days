package com.nhn.fitness.ui.lib.horizontalCalendar;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class HorizontalPaginationScroller extends RecyclerView.OnScrollListener  {
    LinearLayoutManager layoutManager;

    public HorizontalPaginationScroller(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();
        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

        if (!isLoading()) {
            if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                loadMoreItems();
            }
            if(layoutManager.findFirstCompletelyVisibleItemPosition()==0){
                loadMoreItemsOnLeft();
            }

        }
    }

    protected abstract void loadMoreItems();

    protected abstract void loadMoreItemsOnLeft();


    public abstract boolean isLoading();
}
