package com.mobbile.paul.ui.orders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.model.allskuOrderd
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.order_history_recycle.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class OrderSummaryAdapter(private var mItems: List<allskuOrderd>):
    RecyclerView.Adapter<OrderSummaryAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.order_history_recycle, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: allskuOrderd) {
            val df = DecimalFormat("#.#")

            df.roundingMode = RoundingMode.FLOOR
            containerView.sku_tv.text = item.skuname.trim()
            containerView.qty_tv.text = item.qtyordered.toString().trim()
            containerView.price_tv.text = String.format("%,.1f", (df.format(item.amount*item.qtyordered).toDouble())).trim()

        }
    }
}