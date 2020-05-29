package com.mobbile.paul.ui.modules

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.mobbile.paul.model.modulesEntity
import com.mobbile.paul.salesrepmobiletrader.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.modules_adapter.view.*
import kotlin.reflect.KFunction2

private lateinit var database: FirebaseDatabase


class modulesAdapter(private var notify: Int, private var contexts: Context, var mItems: List<modulesEntity>,
                         val clickListener: KFunction2<@ParameterName(name = "partItem") modulesEntity,
                                 @ParameterName(
                                     name = "containerView"
                                 ) View, Unit>
    ) :
        RecyclerView.Adapter<modulesAdapter.ViewHolder>() {

    var orderNotification: Int = 0

    fun setNotification(notifyOrder: Int) {
        orderNotification = notifyOrder
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context)
            .inflate(R.layout.modules_adapter, p0, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = mItems[p1]
        p0.bind(item,clickListener)
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    companion object {
        private val TAG = "ModulesActivity"
    }


    inner class ViewHolder(override val containerView: View): RecyclerView.ViewHolder(containerView),

        LayoutContainer {
        fun bind(
            item: modulesEntity,
            clickListener: KFunction2<@ParameterName(name = "partItem") modulesEntity,
                    @ParameterName(name = "containerView") View, Unit>
        ) {

            database = FirebaseDatabase.getInstance()

            containerView.tv_name.text = item.name

            containerView.tv_count.visibility = View.INVISIBLE

            if(item.nav==6) {
                if(orderNotification==0){
                    containerView.tv_count.visibility = View.INVISIBLE
                }else{
                    containerView.tv_count.visibility = View.VISIBLE
                    containerView.tv_count.text = orderNotification.toString()
                }
            }

            Glide.with(contexts)
                .load(item.imageurl)
                .override(50,50)
                .into(containerView.imageView)

            containerView.setOnClickListener {
                clickListener(item, containerView)
            }
        }
    }
}