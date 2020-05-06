package com.mobbile.paul.ui.orders


import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.mobbile.paul.model.allCustomerProductOrder
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.extensions.LayoutContainer
import android.content.Context
import android.util.Log
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import kotlinx.android.synthetic.main.customersorders.view.*
import kotlin.reflect.KFunction2



class OrdersAdapter(private var mItems: List<allCustomerProductOrder>, private var contexts: Context,
                    private val clickListener: KFunction2<@ParameterName(name = "partItem") allCustomerProductOrder, @ParameterName(
                        name = "containerView"
                    ) View, Unit>
    ):
        RecyclerView.Adapter<OrdersAdapter.ViewHolder>() {


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
        fun bind(
            item: allCustomerProductOrder, clickListener: KFunction2<@ParameterName(
                name = "partItem"
            ) allCustomerProductOrder, @ParameterName(name = "containerView") View, Unit>
        ) {

            Log.d(TAG, item.toString())

            val letter: String? = item.outletname.substring(0, 1)
            val generator = ColorGenerator.MATERIAL
            val drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.imageView.setImageDrawable(drawable)
            containerView.tv_name.text = item.outletname
            containerView.tv_titles.text = ("URNO: ${item.urno}")

            containerView.menu_icon.setOnClickListener {
                clickListener(item, containerView)
            }
        }
    }

    companion object {
        val TAG = "OrdersAdapter"
    }

}