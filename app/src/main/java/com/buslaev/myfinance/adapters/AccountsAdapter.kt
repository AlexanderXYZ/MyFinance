package com.buslaev.myfinance.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.buslaev.myfinance.R
import com.buslaev.myfinance.entities.Account
import com.buslaev.myfinance.utilits.changeMoney
import javax.inject.Inject

class AccountsAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {

    private var mList = emptyList<Account>()

    inner class AccountsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_item)
        val image: ImageView = itemView.findViewById(R.id.image_item)
        val money: TextView = itemView.findViewById(R.id.money_item)
        val currency: TextView = itemView.findViewById(R.id.currency_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        return AccountsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_graph_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        val currentPos = mList[position]
        holder.apply {
            title.text = currentPos.title
            glide
                .load(currentPos.icon)
                .into(image)
            val color = Color.parseColor(currentPos.backgroundColor)
            image.background.setTint(color)
            money.text = changeMoney(currentPos.money)
            currency.text = currentPos.currency
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setList(list: List<Account>) {
        mList = list
        notifyDataSetChanged()
    }
}