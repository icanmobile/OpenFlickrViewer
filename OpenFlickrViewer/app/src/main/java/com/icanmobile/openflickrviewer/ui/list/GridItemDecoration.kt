package com.icanmobile.openflickrviewer.ui.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.right = space
        outRect.bottom = space

        val position = parent.getChildLayoutPosition(view)
        if (position == 0 || position == 1) {
            outRect.top = space
        } else {
            outRect.top = 0
        }

        if (position % 2 == 0) {
            outRect.left = space
        } else {
            outRect.left = 0
        }
    }
}