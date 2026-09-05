package com.lichviet.vannien.ui.month

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Vẽ các đường kẻ ô (kẻ ngang và kẻ dọc) cho bảng lịch tháng
 */
class CalendarGridDividerDecoration(
    dividerColor: Int = Color.parseColor("#E2E8F0"),
    private val strokeWidthDp: Float = 1f
) : RecyclerView.ItemDecoration() {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = dividerColor
        style = Paint.Style.STROKE
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        if (childCount == 0) return

        val density = parent.context.resources.displayMetrics.density
        paint.strokeWidth = (strokeWidthDp * density).coerceAtLeast(1f)

        val layoutManager = parent.layoutManager as? GridLayoutManager ?: return
        val spanCount = layoutManager.spanCount
        val totalItems = parent.adapter?.itemCount ?: childCount
        val totalRows = (totalItems + spanCount - 1) / spanCount

        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val pos = parent.getChildAdapterPosition(child)
            if (pos == RecyclerView.NO_POSITION) continue

            val col = pos % spanCount
            val row = pos / spanCount

            // Kẻ đường dọc bên phải ô (cột 0 -> 5)
            if (col < spanCount - 1) {
                val x = child.right.toFloat()
                c.drawLine(x, child.top.toFloat(), x, child.bottom.toFloat(), paint)
            }

            // Kẻ đường ngang bên dưới ô (tất cả các hàng để đóng khung lưới)
            if (row < totalRows) {
                val y = child.bottom.toFloat()
                c.drawLine(child.left.toFloat(), y, child.right.toFloat(), y, paint)
            }
        }
    }
}
