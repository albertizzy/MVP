package com.zy.mvp.touchhelper;

public interface ItemTouchHelperAdapter {
    /**
     * 数据交换**
     *
     * @param fromPosition
     * @param toPosition
     */
//    void onItemMove(int fromPosition, int toPosition);

    /**
     * 数据删除
     *
     * @param position
     */
    void onItemDismiss(int position);
}