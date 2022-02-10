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

class BaseSwipeHelper<T> {

    fun onSwipeItem(
        items: List<T>,
        context: Context,
        recyclerView: RecyclerView,
        direction: Int,
        actionDelete: (item: T) -> Unit,
        actionRestore: (item: T) -> Unit) {

        // swipe actions
        val trashBinIcon =
            ResourcesCompat.getDrawable(context.resources,
                R.drawable.ic_baseline_delete_forever_24, null)

        val swipeRightCallback = object: ItemTouchHelper.SimpleCallback(
            0, direction) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                val item = items[viewHolder.bindingAdapterPosition]

                val builder = AlertDialog.Builder(context)
                builder.setTitle("Delete?")

                // Display a message on alert dialog
                builder.setMessage(
                    "Are you want to delete '${(item as BaseListItem).getItemTitle()}'?")

                // Set a positive button and its click listener on alert dialog
                builder.setPositiveButton("YES"){ _, _ ->

                    actionDelete(item)

                    // showing snack bar with Undo option
                    val snackbar: Snackbar = Snackbar
                        .make(
                            recyclerView,
                            "'${(item as BaseListItem).getItemTitle()}' removed!",
                            Snackbar.LENGTH_LONG
                        )
                    snackbar.setAction("UNDO", View.OnClickListener
                    { _, ->
                        actionRestore(item)
                    })

                    snackbar.setActionTextColor(Color.YELLOW)
                    snackbar.show()

                }

                // Display a negative button on alert dialog
                builder.setNegativeButton("No"){ _, _ ->

                }

                // Display a neutral button on alert dialog
                builder.setNeutralButton("Cancel"){_,_ ->
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
        swipeRightHelper.attachToRecyclerView(recyclerView)
    }
}