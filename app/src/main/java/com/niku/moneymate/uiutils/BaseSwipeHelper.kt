package com.niku.moneymate.uiutils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.niku.moneymate.BaseListItem
import com.niku.moneymate.R

/**
 * Defines actions in OnSwipe actions in RecyclerView.
 * <p>
 * Direction must be ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
 * <p>
 * Usage:
 * <pre>
 *     val sw: BaseSwipeHelper<T> = BaseSwipeHelper<T>(requireContext())
 *      .setRecyclerView(rv)
 *      .setDirection(ItemTouchHelper.LEFT)
 *      .setItems(List<T>)
 *      .setOnSwipeAction { ... }
 *      .setOnCancelAction { ... }
 *      .build()
 * </pre>
 */
class BaseSwipeHelper<T: BaseListItem> constructor (context: Context) {

    private var mItems: List<T>? = null
    private var mContext: Context? = null
    private lateinit var mRecyclerView: RecyclerView
    private var mDirection: Int? = null
    private lateinit var mOnSwipeAction: (item: T) -> Unit
    private lateinit var mOnCancelAction: (item: T) -> Unit

    init {
        mContext = context
    }

    fun setRecyclerView(rv: RecyclerView): BaseSwipeHelper<T> {
        mRecyclerView = rv
        return this
    }

    fun setDirection(direction: Int): BaseSwipeHelper<T> {
        mDirection = direction
        return this
    }

    fun setItems(items: List<T>): BaseSwipeHelper<T> {
        mItems = items.map { it }
        return this
    }

    fun setOnSwipeAction(action: (item: T) -> Unit): BaseSwipeHelper<T> {
        mOnSwipeAction = action
        return this
    }

    fun setOnCancelAction(action: (item: T) -> Unit): BaseSwipeHelper<T> {
        mOnCancelAction = action
        return this
    }

    fun build(): BaseSwipeHelper<T> {

        if (mContext == null) {
            throw IllegalArgumentException("context not initialized")
        }

        if (mItems == null) {
            throw IllegalArgumentException("Items not initialized")
        }

        // swipe actions
        val trashBinIcon =
            mContext?.resources?.let {
                ResourcesCompat.getDrawable(
                    it,
                    R.drawable.ic_baseline_delete_forever_24, null)
            }

        val swipeRightCallback = object: ItemTouchHelper.SimpleCallback(
            0, mDirection ?: ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val item = mItems!![viewHolder.bindingAdapterPosition]

                val builder = AlertDialog.Builder(mContext)
                builder.setTitle("Delete")

                // Display a message on alert dialog
                builder.setMessage(
                    "You want to delete '${item.getItemTitle()}'?")

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES"){ _, _ ->

                    mOnSwipeAction(item)

                    // showing snack bar with Undo option
                    val snackbar = Snackbar
                        .make(
                            mRecyclerView,
                            "'${item.getItemTitle()}' removed!",
                            Snackbar.LENGTH_LONG)
                        .setAction("UNDO", View.OnClickListener
                            { _, ->
                                mOnCancelAction(item)
                            })
                        .setActionTextColor(Color.YELLOW)
                        .show()

                }
                // Display a negative button on alert dialog
                builder.setNegativeButton("No"){ _, _ ->
                    mRecyclerView.adapter?.notifyItemChanged(viewHolder.bindingAdapterPosition)
                }
                // Display a neutral button on alert dialog
                builder.setNeutralButton("Cancel"){_,_ ->
                    mRecyclerView.adapter?.notifyItemChanged(viewHolder.bindingAdapterPosition)
                    //Toast.makeText(applicationContext,"You cancelled the dialog.",Toast.LENGTH_SHORT).show()
                }
                val dialog: AlertDialog = builder.create()
                dialog.show()

            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                c.clipRect(
                    viewHolder.itemView.right.toFloat() + dX,
                    viewHolder.itemView.top.toFloat(),
                    viewHolder.itemView.right.toFloat(),
                    viewHolder.itemView.bottom.toFloat())
                c.drawColor(Color.RED)

                trashBinIcon?.let {
                    it.bounds =
                        Rect(
                            viewHolder.itemView.right - trashBinIcon.intrinsicWidth - 16,
                            viewHolder.itemView.top + 32,
                            viewHolder.itemView.right - 16,
                            viewHolder.itemView.top + trashBinIcon.intrinsicHeight + 32
                        )
                    it.draw(c)
                }
            }
        }
        val swipeRightHelper = ItemTouchHelper(swipeRightCallback)
        swipeRightHelper.attachToRecyclerView(mRecyclerView)
        return this
    }
}