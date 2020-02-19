package com.mobbile.paul.ui.entries

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobbile.paul.model.setSalesEntry
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.sales_entry_adapter.view.*
import kotlin.reflect.KFunction2


class EntriesAdapter(private var mItems: List<setSalesEntry>,
                     val clickListener: KFunction2<@ParameterName(name = "partItem") setSalesEntry, @ParameterName(
                         name = "containerView"
                     ) View, Unit>
) :
    RecyclerView.Adapter<EntriesAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.sales_entry_adapter, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item, clickListener)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    companion object {
        private val TAG = "SalesEntriesAdapter"
    }

    inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView),
        LayoutContainer {
        fun bind(
            item: setSalesEntry,
            clickListener: KFunction2<@ParameterName(name = "partItem") setSalesEntry, @ParameterName(
                name = "containerView"
            ) View, Unit>
        ) {

            containerView.tv_skus.text = item.product_name
            containerView.tv_soq.text = "${item.soq}"

            if (item.seperator.equals("3")) {
                containerView.tv_skus.setTextColor(Color.parseColor("#01579B"))
            }

            if (item.soq.isEmpty()) {
                containerView.tv_soq.text = "0"
            }

            containerView.mt_pricing.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        clickListener(item,containerView)
                    }
                }
            )

            containerView.mt_order.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        clickListener(item,containerView)
                    }
                }
            )

            containerView.mt_inventory.addTextChangedListener(
                object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {}
                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        clickListener(item,containerView)
                        //(this@EntriesAdapter.clickListener)(item,containerView)
                    }
                }
            )
        }
    }
}