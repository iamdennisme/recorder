package com.dennisce.recorder.tools.recyclerview

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dennisce.recorder.R


class SpacesItemDecoration(private val space: Int, private val orientation: Int, val color: Int = R.color.white) : RecyclerView.ItemDecoration() {


    companion object {
        const val HORIZONTAL = RecyclerView.HORIZONTAL

        const val VERTICAL = RecyclerView.VERTICAL

    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (orientation == VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    private fun drawVertical(c: Canvas, parent: RecyclerView) {
        val paint = Paint()
        paint.color = ContextCompat.getColor(parent.context, color)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val left = 0
            val top = child.bottom
            val right = child.right
            val bottom = child.bottom + space
            c.drawRect(Rect(left, top, right, bottom), paint)
        }
    }

    private fun drawHorizontal(c: Canvas, parent: RecyclerView) {
        val paint = Paint()
        paint.color = ContextCompat.getColor(parent.context, color)
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val left = child.right
            val top = 0
            val right = child.right + space
            val bottom = child.bottom
            c.drawRect(Rect(left, top, right, bottom), paint)
        }
    }

    override fun getItemOffsets(outRect: Rect, view: View,
                                parent: RecyclerView, state: RecyclerView.State) {
        when (orientation) {
            HORIZONTAL -> {
                outRect.right = space
            }
            VERTICAL -> {
                outRect.bottom = space
            }
        }
    }
}
