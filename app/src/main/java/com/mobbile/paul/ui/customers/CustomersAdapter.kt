package com.mobbile.paul.ui.customers

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mobbile.paul.model.customersEntity
import com.mobbile.paul.salesrepmobiletrader.R
import com.mobbile.paul.ui.attendant.Banks
import com.mobbile.paul.ui.attendant.Close
import com.mobbile.paul.ui.attendant.Resumption
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.outlet_adapter.view.*
import kotlin.reflect.KFunction2


class CustomersAdapter(

    private var mItems: List<customersEntity>,
    private var context: Context,
    private var clickListener: KFunction2<@ParameterName(name = "partItem") customersEntity, @ParameterName(name = "separator") Int, Unit>

) : RecyclerView.Adapter<CustomersAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.outlet_adapter, p0, false)
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
            item: customersEntity,
            itemClickListener: KFunction2<@ParameterName(name = "partItem") customersEntity, @ParameterName(name = "separator") Int, Unit>
        ) {
            val letter: String? = item.outletname.substring(0, 1)
            val generator = ColorGenerator.MATERIAL
            val drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.outletImageView.setImageDrawable(drawable)

            containerView.tv_cust_name.text = item.outletname
            containerView.tv_titles.text = ("URNO: ${item.urno}, VCL: ${item.volumeclass}")
            containerView.tv_sequence.text = "${item.sequenceno - 1}"
            containerView.timeago.text = item.entry_time

            containerView.cust_icons.setOnClickListener {
                showPopup(containerView, item, itemClickListener)
            }

            if(item.sort==1) {
                containerView.cust_icons.visibility = View.GONE
                containerView.tv_titles.text = item.notice
                containerView.tv_sequence.visibility = View.GONE
            }

            if(item.sort==2) {
                containerView.icons_image_d.visibility = View.GONE
            }

            if(item.sort==3) {
                containerView.cust_icons.visibility = View.GONE
                containerView.tv_sequence.visibility = View.GONE
                containerView.tv_titles.text = item.notice
            }

            if(item.sort==4) {
                containerView.cust_icons.visibility = View.GONE
                containerView.tv_sequence.visibility = View.GONE
                containerView.tv_titles.text = item.notice
            }

            containerView.setOnClickListener {
                when(item.sort) {
                    1->{
                        val intent = Intent(context, Resumption::class.java)
                        intent.putExtra("repid", item.rep_id)
                        intent.putExtra("outletlat", item.latitude)
                        intent.putExtra("outletlng", item.longitude)
                        intent.putExtra("sequenceno", item.sequenceno)
                        intent.putExtra("distance",item.distance)
                        intent.putExtra("duration",item.duration)
                        intent.putExtra("customer_code",item.customer_code)
                        intent.putExtra("sort",item.sort)
                        context.startActivity(intent)
                    }
                    3->{
                        val intent = Intent(context, Banks::class.java)
                        intent.putExtra("repid", item.rep_id)
                        intent.putExtra("outletlat", item.latitude)
                        intent.putExtra("outletlng", item.longitude)
                        intent.putExtra("sequenceno", item.sequenceno)
                        intent.putExtra("distance",item.distance)
                        intent.putExtra("duration",item.duration)
                        intent.putExtra("customer_code",item.customer_code)
                        intent.putExtra("sort",item.sort)
                        intent.putExtra("auto",item.auto)
                        context.startActivity(intent)
                    }
                    4->{
                        val intent = Intent(context, Close::class.java)
                        intent.putExtra("repid", item.rep_id)
                        intent.putExtra("outletlat", item.latitude)
                        intent.putExtra("outletlng", item.longitude)
                        intent.putExtra("sequenceno", item.sequenceno)
                        intent.putExtra("distance",item.distance)
                        intent.putExtra("duration",item.duration)
                        intent.putExtra("customer_code",item.customer_code)
                        intent.putExtra("sort",item.sort)
                        intent.putExtra("auto",item.auto)
                        context.startActivity(intent)
                    }
                }
            }
        }

        private fun showPopup(
            view: View, item: customersEntity,
            itemClickListener: KFunction2<@ParameterName(name = "partItem") customersEntity, @ParameterName(name = "separator") Int, Unit>
        ) {

            val popupMenu = PopupMenu(context, view.cust_icons)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.floatingcontextmenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.entries_id -> {
                        itemClickListener(item,100)
                    }
                    R.id.outlet_nav -> {
                        itemClickListener(item,200)
                    }
                    R.id.update_outlet -> {
                        itemClickListener(item,300)
                    }
                    R.id.close_outlet -> {
                        itemClickListener(item,400)
                    }
                    R.id.async -> {
                        itemClickListener(item,500)
                    }
                    R.id.details -> {
                        itemClickListener(item,600)
                    }
                }
                true
            }
            popupMenu.show()
        }
    }

    companion object {
        var TAG = "TYTYTYTYTTYYTTY"
    }
}
