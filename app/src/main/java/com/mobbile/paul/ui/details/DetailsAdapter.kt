package com.mobbile.paul.ui.details

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.model.AllDetailsForEachSales
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.details_adapter.view.*


class DetailsAdapter(private var mItems: List<AllDetailsForEachSales>) :
    RecyclerView.Adapter<DetailsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.details_adapter, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    companion object {
        private val TAG = "BankAdapter"
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: AllDetailsForEachSales) {

            if (item.product_category.equals("competition")) {
                containerView.tv_sku_q.setTextColor(Color.parseColor("#01579B"))
            }


            containerView.tv_sku_q.text = item.product_name
            containerView.order_tv_q.text = "${item.inventory}"
            containerView.amt_tv_q.text = "${item.pricing}"
            containerView.tv_aty_q.text = "${item.qty}"
        }
    }
}