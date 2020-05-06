package com.mobbile.paul.ui.orders


import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.mobbile.paul.model.allCustomerProductOrder
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.extensions.LayoutContainer
import android.content.Context

class OrdersAdapter(private var mItems: List<allCustomerProductOrder>, private var contexts: Context,
                    val clickListener: (allCustomerProductOrder) -> Unit): RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.customersorders, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item,clickListener)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: allCustomerProductOrder, clickListener: (allCustomerProductOrder) -> Unit) {

        }
    }
}