package com.madrecipes.app.madrecipes.TouchHelper;

//Tay Hui Lin
//Team 6
//10178003J

//interface for reorder and swipe to delete
public interface ItemTouchHelperAdapter {
    boolean onItemMove(int fromPosition, int toPosition);
    void onItemDismiss(int position);
}

