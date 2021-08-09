package com.mobbile.paul.ui.attendant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.mobbile.paul.model.GetAllAgentDetailByRoute
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.agent_adapter.view.*
import kotlinx.android.synthetic.main.agent_adapter.view.agentImageIcon
import kotlinx.android.synthetic.main.outlet_adapter.view.*
import kotlin.reflect.KFunction2


class AgentAdapter(private var mItems: List<GetAllAgentDetailByRoute>, private var context: Context,
                   private var clickListener: KFunction2<GetAllAgentDetailByRoute, Int, Unit>
) :
    RecyclerView.Adapter<AgentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.agent_adapter, p0, false)
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
        fun bind(item: GetAllAgentDetailByRoute,
                 itemClickListener: KFunction2<GetAllAgentDetailByRoute, Int, Unit>) {
            val letter: String? = item.outletname.substring(0, 1).toUpperCase()
            val generator = ColorGenerator.MATERIAL
            val drawable = TextDrawable.builder()
                .buildRound(letter, generator.getRandomColor())
            containerView.agentImageIcon.setImageDrawable(drawable)
            containerView.tv_agent_name.text = item.outletname.toUpperCase()
            containerView.agents_icons_images.setOnClickListener {
                showPopup(containerView, item, itemClickListener)
            }
        }

        private fun showPopup(
            view: View, item: GetAllAgentDetailByRoute,
            itemClickListener:  KFunction2<GetAllAgentDetailByRoute, Int, Unit>
        ) {

            val popupMenu = PopupMenu(context, view.agents_icons_images)
            val inflater = popupMenu.menuInflater
            inflater.inflate(R.menu.agentmenu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.agentlocation -> {
                        itemClickListener(item,1)
                    }
                    R.id.outlet_details -> {
                        itemClickListener(item,2)
                    }
                }
                true
            }
            popupMenu.show()
        }
    }


}