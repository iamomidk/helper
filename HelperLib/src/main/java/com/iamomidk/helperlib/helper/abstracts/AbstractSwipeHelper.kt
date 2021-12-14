package com.iamomidk.helperlib.helper.abstracts

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.iamomidk.helperlib.helper.abstracts.AbstractAdapter.Companion.NOT_SWEEPABLE

/**
 * swipeHelper for recyclerView
 * @author iamomidk
 * @param swipeDirs direction of swipe
 * @param icon drawableIcon of the
 */
abstract class AbstractSwipeHelper(
	private val swipeDirs: Int,
	private val icon: Drawable?,
	private val backgroundColor: Int = Color.parseColor("#ff8f3d"),
) : ItemTouchHelper.SimpleCallback(0, swipeDirs) {

	private val intrinsicWidth = icon?.intrinsicWidth
	private val intrinsicHeight = icon?.intrinsicHeight
	private val background = ColorDrawable()
	private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

	/**
	 * To disable "swipe" for specific item return 0.
	 * if (viewHolder?.itemViewType == ADAPTER) return 0
	 */
	override fun getMovementFlags(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
	): Int = when (viewHolder.itemViewType) {
		NOT_SWEEPABLE -> 0
		else -> super.getMovementFlags(recyclerView, viewHolder)
	}

	override fun onChildDraw(
		c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
		dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean,
	) {
		val itemView = (viewHolder.itemView)
		val itemHeight = itemView.bottom - itemView.top
		val isCanceled = dX == 0f && !isCurrentlyActive


		if ((swipeDirs == ItemTouchHelper.LEFT) or (swipeDirs == ItemTouchHelper.START)) {
			if (isCanceled) {
				itemView.apply {
					clearCanvas(
						c,
						right + dX,
						top.toFloat(),
						right.toFloat(),
						bottom.toFloat()
					)
				}
				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
				return
			}

			// Draw the icon background
			background.color = backgroundColor
			background.setBounds(
				itemView.right + dX.toInt(),
				itemView.top,
				itemView.right,
				itemView.bottom
			)
			background.draw(c)

			// Calculate position of delete icon
			val iconTop = itemView.top.plus((itemHeight - intrinsicHeight!!).div(2))
			val iconMargin = (itemHeight.minus(intrinsicHeight)).div(2)
			val iconLeft = itemView.right.minus(iconMargin).minus(intrinsicWidth!!)
			val iconRight = itemView.right.minus(iconMargin)
			val iconBottom = iconTop.plus(intrinsicHeight)

			// Draw the icon
			icon?.apply {
				setBounds(iconLeft, iconTop, iconRight, iconBottom)
				draw(c)
			}
		} else if ((swipeDirs == ItemTouchHelper.RIGHT) or (swipeDirs == ItemTouchHelper.END)) {
			if (isCanceled) {
				itemView.apply {
					clearCanvas(
						c = c,
						left = left.toFloat(),
						top = top.toFloat(),
						right = right.plus(dX),
						bottom = bottom.toFloat()
					)
				}
				super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
				return
			}

			// Draw the icon background
			background.color = backgroundColor
			background.setBounds(
				itemView.left,
				itemView.top,
				itemView.left.plus(dX.toInt()),
				itemView.bottom
			)
			background.draw(c)

			// Calculate position of delete icon
			val iconTop = itemView.top.plus((itemHeight.minus(intrinsicHeight!!)).div(2))
			val iconMargin = (itemHeight.minus(intrinsicHeight)).div(2)
			val iconLeft = itemView.left.plus(iconMargin)
			val iconRight = iconLeft.plus(intrinsicWidth!!)
			val iconBottom = iconTop.plus(intrinsicHeight)

			// Draw the icon
			icon?.apply {
				setBounds(iconLeft, iconTop, iconRight, iconBottom)
				draw(c)
			}
		}

		super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
	}

	private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) =
		c?.drawRect(left, top, right, bottom, clearPaint)

	override fun onMove(
		recyclerView: RecyclerView,
		viewHolder: RecyclerView.ViewHolder,
		target: RecyclerView.ViewHolder,
	): Boolean = false
}