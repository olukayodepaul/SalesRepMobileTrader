package com.mobbile.paul.ui.comission


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.model.salesCommissionList
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.sales_commission_adapter.view.*


class SalesCommissionAdapter(private var mItems: List<salesCommissionList>) :
    RecyclerView.Adapter<SalesCommissionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.sales_commission_adapter, p0, false)
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
        private val TAG = "SalesCommissionAdapter"
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: salesCommissionList) {
            containerView.flue_float.text = item.commisssiontype.toLowerCase().capitalize()
            containerView.flue_float_amount.text = item.amount
            containerView.flue_float_date.text = item.dates
        }
    }
}


