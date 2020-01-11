package com.mobbile.paul.ui.attendant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.model.Products
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.attendant_adapter.view.*


class ResumptionAdapter(private var mItems: List<Products>):
    RecyclerView.Adapter<ResumptionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.attendant_adapter, p0, false)
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
        private val TAG = "Attendantadapter"
    }

    inner class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(item: Products) {
            containerView.t_sku.text = item.productname
            containerView.t_aty.text = item.qty
        }
    }
}
