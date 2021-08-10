package com.buslaev.myfinance.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.buslaev.myfinance.R
import com.buslaev.myfinance.entities.Categories
import javax.inject.Inject

class CategoriesAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    private var mList = emptyList<Categories>()

    inner class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.title_categories_item)
        var icon: ImageView = itemView.findViewById(R.id.icon_categories)
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
            icon.setBackgroundColor(currentPos.backgroundColor)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setList(list: List<Categories>) {
        mList = list
        notifyDataSetChanged()
    }
}