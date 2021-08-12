package com.buslaev.myfinance.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.buslaev.myfinance.R
import com.buslaev.myfinance.entities.Categories
import javax.inject.Inject

class CategoriesAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private var mList = emptyList<Categories>()
    private var currentItemPos: Int? = null

    inner class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title_categories_item)
        var icon: ImageView = itemView.findViewById(R.id.icon_categories)
        var item: CardView = itemView.findViewById(R.id.categories_card_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.categories_item, parent, false)
        return CategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val currentPos = mList[position]
        holder.apply {
            title.text = currentPos.title
            glide
                .load(currentPos.icon)
                .into(icon)
            val color = Color.parseColor(currentPos.backgroundColor)
            icon.background.setTint(color)
            title.setTextColor(color)

            if (currentItemPos == adapterPosition) {
                item.elevation = 8F
            } else {
                item.elevation = 0F
            }
            item.setOnClickListener {
                val oldItemPos = currentItemPos
                currentItemPos = adapterPosition

                onItemClickListener?.let { currentPos.idCategory?.let { idCategory -> it(idCategory) } }

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

    private var onItemClickListener: ((Int) -> Unit)? = null

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setList(list: List<Categories>) {
        mList = list
        notifyDataSetChanged()
    }
}