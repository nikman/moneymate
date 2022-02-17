package com.niku.moneymate.uiutils

import android.app.AlertDialog
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.util.Log
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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
private const val TAG = "BaseSwipeHelper"

class BaseSwipeHelper<T: BaseListItem> constructor (context: Context) {

    private var mItems: List<T>? = null
    private var mContext: Context? = null
    private lateinit var mRecyclerView: RecyclerView
    private var mDirection: Int? = null
    private lateinit var mOnSwipeAction: (item: T) -> Unit
    private lateinit var mOnCancelAction: (item: T) -> Unit
    private lateinit var mOnUndoAction: (item: T) -> Unit
    private lateinit var mSwipeRightHelper: ItemTouchHelper

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

    /*fun setOnCancelAction(action: (item: T) -> Unit): BaseSwipeHelper<T> {
        mOnCancelAction = action
        return this
    }*/

    fun setOnUndoAction(action: (item: T) -> Unit): BaseSwipeHelper<T> {
        mOnUndoAction = action
        return this
    }

    fun detach() {
        mSwipeRightHelper.attachToRecyclerView(null)
    }

    fun build(): BaseSwipeHelper<T> {

        if (mContext == null) {
            throw IllegalArgumentException("context not initialized")
        }

        if (mItems == null) {
            throw IllegalArgumentException("Items not initialized")
        }

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

                if (mItems!!.isEmpty()) {
                    Log.d(TAG, "no need to handle actions")
                    return
                }

                val item = mItems!![viewHolder.bindingAdapterPosition]

                val builder = AlertDialog.Builder(mContext)
                    .setTitle("Delete")
                    .setOnCancelListener { _ ->
                        //mOnCancelAction(item)
                        mRecyclerView.adapter?.notifyItemChanged(viewHolder.bindingAdapterPosition)
                    }
                    .setMessage(
                        "You want to delete '${item.getItemTitle()}'?")
                    .setPositiveButton("YES"){ _, _ ->
                        mOnSwipeAction(item)
                        mRecyclerView.adapter?.notifyItemRemoved(viewHolder.bindingAdapterPosition)

                        val snackbar = Snackbar
                            .make(
                                mRecyclerView,
                                "'${item.getItemTitle()}' removed!",
                                Snackbar.LENGTH_LONG)
                            .setAction("UNDO", View.OnClickListener
                                { _, ->
                                    mOnUndoAction(item)
                                    mRecyclerView.adapter?.notifyItemInserted(viewHolder.bindingAdapterPosition)
                                })
                            .setActionTextColor(Color.YELLOW)
                            .show()
                    }
                    .setNegativeButton("No"){ _, _ ->
                        //mOnCancelAction(item)
                        mRecyclerView.adapter?.notifyItemChanged(viewHolder.bindingAdapterPosition)
                    }
                    .setNeutralButton("Cancel"){_,_ ->
                        //mRecyclerView.adapter?.notifyItemChanged(viewHolder.bindingAdapterPosition)
                        //mOnCancelAction(item)
                        mRecyclerView.adapter?.notifyItemChanged(viewHolder.bindingAdapterPosition)
                        //Toast.makeText(mContext,"Cancel pressed",Toast.LENGTH_LONG).show()
                    }
                    .setCancelable(false)

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
        val mSwipeRightHelper = ItemTouchHelper(swipeRightCallback)
        mSwipeRightHelper.attachToRecyclerView(mRecyclerView)
        return this
    }
}