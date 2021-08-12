package com.buslaev.myfinance.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.buslaev.myfinance.R
import com.buslaev.myfinance.other.Constants.COLOR_DEFAULT_ICON
import javax.inject.Inject

class IconsAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<IconsAdapter.IconsViewHolder>() {

    private var mList = emptyList<Int>()
    private var currentItemPos: Int? = null
    private var changedColor: String = ""

    inner class IconsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconItem: ConstraintLayout = itemView.findViewById(R.id.icon_item)
        val icon: ImageView = itemView.findViewById(R.id.image_icon_item)
        val background: ImageView = itemView.findViewById(R.id.image_background)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.icon_item, parent, false)
        return IconsViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconsViewHolder, position: Int) {
        val currentPos = mList[position]
        holder.apply {
            glide
                .load(currentPos)
                .into(icon)
            if (currentItemPos == adapterPosition) {
                background.visibility = View.VISIBLE
                if (changedColor.isNotEmpty()) {
                    icon.background.setTint(Color.parseColor(changedColor))
                }
            } else {
                background.visibility = View.GONE
                icon.background.setTint(Color.parseColor(COLOR_DEFAULT_ICON))
            }
            icon.setOnClickListener {
                val oldItemPos = currentItemPos
                currentItemPos = adapterPosition
                onItemClickListener?.let { it(currentPos) }
                if (oldItemPos != null) {
                    updateOldItemAndNewItem(oldItemPos, currentItemPos!!)
                } else {
                    updateItem(currentItemPos!!)
                }


            }
        }
    }

    private fun updateOldItemAndNewItem(oldItemPos: Int, newItemPos: Int) {
        notifyItemChanged(oldItemPos)
        notifyItemChanged(newItemPos)
    }

    private fun updateItem(itemPos: Int) {
        notifyItemChanged(itemPos)
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    fun setList(list: List<Int>) {
        mList = list
        notifyDataSetChanged()
    }

    fun updateColor(colorHex: String) {
        changedColor = colorHex
        updateItem(currentItemPos!!)
    }
}