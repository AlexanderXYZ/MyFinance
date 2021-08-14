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
import com.buslaev.myfinance.entities.Operation
import com.buslaev.myfinance.entities.OperationBySum
import javax.inject.Inject
import kotlin.math.roundToInt

class MainAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<MainAdapter.MainViewHolder>() {

    private var mList = emptyList<OperationBySum>()
    private var mAllMoney: Double = 0.0

    inner class MainViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title_item)
        val image: ImageView = itemView.findViewById(R.id.image_item)
        val percent: TextView = itemView.findViewById(R.id.percent_item)
        val money: TextView = itemView.findViewById(R.id.money_item)
        val currency: TextView = itemView.findViewById(R.id.valute_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.main_graph_item, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currentPos = mList[position]
        holder.apply {
            glide
                .load(currentPos.iconCategory)
                .into(image)
            val color = Color.parseColor(currentPos.backgroundColor)
            image.background.setTint(color)

            percent.text = "${(currentPos.value.div(mAllMoney)).times(100).roundToInt()} %"
            money.text = changeMoney(currentPos.value)
        }
    }

    private fun changeMoney(value: Double): String {
        var valueString = value.toString()
        if (value >= 1_000) {
            valueString = value.roundToInt().toString()
            val firstPart = valueString.substring(0,valueString.length - 3)
            val secondPart = valueString.substring(valueString.length - 3)
            return "$firstPart $secondPart"
        }
        return valueString
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setList(list: List<OperationBySum>) {
        mAllMoney = list.sumOf { it.value }
        mList = list
        notifyDataSetChanged()
    }
}